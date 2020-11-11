package com.xue.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xue.entity.SuccessKill;
import com.xue.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("SucListener")
@Service
public class SucListener /*implements ChannelAwareMessageListener*/ {

    private static final Logger logger = LoggerFactory.getLogger(SucListener.class);

    @Autowired
    private SeckillService secService;

    /**
     * 处理接收到的消息
     *
     * @param message 消息体
     * @param channel 通道，确认消费用
     * @throws Exception
     */
   /* @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            //获取交付tag
            long tag = message.getMessageProperties().getDeliveryTag();
            String str = new String(message.getBody(), "utf-8");
            logger.info("接收到的消息：{}", str);
            JSONObject obj = JSONObject.parseObject(str);
            //下单，操作数据库
            secService.startKill(obj.getLong("userId"), obj.getLong("seckillId"));
            //确认消费
            channel.basicAck(tag, true);
        } catch (Exception e) {
            logger.error("消息监听确认机制发生异常：", e.fillInStackTrace());
        }
    }*/

    /**
     * 接收消息
     */
    @RabbitListener(queues = "${mq.env}.success.kill.queue", containerFactory = "multiListenerContainer")
    public void consume(SuccessKill order) {
        try {
            if (order != null)
                //redis分布式锁
                secService.startKill2(order.getSeckillId(), order.getUserId());
        } catch (Exception e) {
            logger.error("用户秒杀成功后发生异常：", e.fillInStackTrace());
        }
    }
}

