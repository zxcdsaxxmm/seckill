package com.xue.service.impl;

import com.alibaba.fastjson.JSON;
import com.xue.common.Result;
import com.xue.entity.Seckill;
import com.xue.entity.SuccessKill;
import com.xue.mapper.SeckillMapper;
import com.xue.mapper.SuckillMapper;
import com.xue.service.SeckillService;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillServiceImpl implements SeckillService {

    private static final Logger logger = LoggerFactory.getLogger(SeckillService.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private SuckillMapper suckillMapper;

    @Autowired
    private Environment env;

    @Resource
    private SeckillMapper seckillMapper;

    @Override
    public List<Seckill> getAll() {
        return seckillMapper.getAll();
    }

    @Override
    public Seckill findById(Long id) {

        return seckillMapper.findById(id);
    }

    @Override
    public void updateNumber(Long seckill_id) {
        seckillMapper.updateNumber(seckill_id);
    }


    /**
     * 秒杀时异步发送Mq消息
     *
     * @param order
     */
    @Override
    public void seckill(SuccessKill order) {
       /* //设置交换机
        rabbitTemplate.setExchange(env.getProperty("successSeckill.mq.exchange.name"));
        //设置routing.key
        rabbitTemplate.setRoutingKey(env.getProperty("successSeckill.mq.routing.key"));
        //创建消息体
        Message msg = MessageBuilder.withBody(JSON.toJSONString(order).getBytes()).build();
        //发送消息
        rabbitTemplate.convertAndSend(msg);*/

        try {
            if (order != null) {
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(env.getProperty("mq.success.kill.queue.exchange"));
                rabbitTemplate.setRoutingKey(env.getProperty("mq.success.kill.queue.routing.key"));
                rabbitTemplate.convertAndSend(order, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties mp = message.getMessageProperties();
                        mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, SuccessKill.class);
                        return message;
                    }
                });
            }
        } catch (Exception e) {
            logger.error("秒杀时异步发送Mq消息-发生异常，消息为：{}", order, e.fillInStackTrace());
        }
    }


    /**
     * 下单，操作数据库
     *
     * @param userId
     * @param seckillId
     */

    @Transactional()
    @Override
    public void startKill(Long userId, Long seckillId) {
        //该商品库存-1（当库存>0时）
        long count = seckillMapper.updateNumber(seckillId);
        //更新成功，表明秒杀成功，写入秒杀成功表，支付状态设为0-支付成功
        if (count > 0) {
            SuccessKill Record = new SuccessKill();
            Record.setSeckillId(seckillId);
            Record.setUserId(userId);
            Record.setState(0);
            suckillMapper.insertRecord(Record);
            String json = JSON.toJSONString(Record);
            Message msg = MessageBuilder.withBody(json.getBytes()).build();
            rabbitTemplate.convertAndSend(msg);
        }
    }


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 商品秒杀核心业务逻辑的处理-redis的分布式锁
     *
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    public boolean startKill2(Long seckillId, Long userId) throws Exception {
        Boolean result = false;

        if (seckillMapper.getCount(seckillId) <= 0) {
            //TODO:借助Redis的原子操作实现分布式锁-对共享操作-资源进行控制
            ValueOperations operations = stringRedisTemplate.opsForValue();
            String key = new StringBuffer().append(seckillId).append(userId).append("-RedisLock").toString();
            Boolean cacheRes = operations.setIfAbsent(key, "1");
            //TODO:redis部署节点宕机了
            if (cacheRes) {

                stringRedisTemplate.expire(key, 30, TimeUnit.SECONDS);

                Seckill seckill = seckillMapper.findById(seckillId);
                if (seckill != null && seckill.getNumber() > 0) {
                    long res = seckillMapper.updateNumber(seckillId);
                    if (res > 0) {
                        SuccessKill Record = new SuccessKill();
                        Record.setSeckillId(seckillId);
                        Record.setUserId(userId);
                        Record.setState(0);
                        Record.setCreate_time(new Timestamp(System.currentTimeMillis()));
                        suckillMapper.insertRecord(Record);
                        result = true;
                    }
                }
            }
        }
        return result;
    }


}