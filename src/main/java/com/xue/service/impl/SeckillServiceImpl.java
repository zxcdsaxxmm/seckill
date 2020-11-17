package com.xue.service.impl;

import com.xue.common.BaseResponse;
import com.xue.common.Result;
import com.xue.entity.Seckill;
import com.xue.entity.SuccessKill;
import com.xue.mapper.SeckillMapper;
import com.xue.mapper.SuckillMapper;
import com.xue.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    private static final Logger logger = LoggerFactory.getLogger(SeckillService.class);

    private final String key = "killKey";

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SuckillMapper suckillMapper;

    @Resource
    private SeckillMapper seckillMapper;

    @Override
    public List<Seckill> getAll() {
        List<Seckill> list = redisTemplate.boundHashOps("killKey").values();
        if (list == null || list.size() == 0) {
            list = seckillMapper.getAll();
            for (Seckill seckill : list) {
                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(), seckill);
                logger.info("--从数据库读取数据到redis中--");
            }
        } else {
            logger.info("--从redis中读取--");
        }
        return list;
    }

    @Override
    public Seckill findById(Long id) {
        return seckillMapper.findById(id);
    }

    @Override
    public void updateNumber(Long seckillId) {
        seckillMapper.updateNumber(seckillId);
    }

    @Override
    public long getNumber(long seckillId) {
        return seckillMapper.getCount(seckillId);
    }

    /**
     * 下单，操作数据库
     *
     * @param userId
     * @param seckillId
     * @Param state
     */

    @Transactional
    @Override
    public BaseResponse startKill(Long seckillId, Long userId, int state) throws Exception {
        if (userId == null || userId <= 0) {
            throw new Exception("用户信息错误！");
        }

        int record = suckillMapper.insertRecord(seckillId, userId, state);
        if (record <= 0) {
            //return new BaseResponse(500, "秒杀重复操作!");
            throw new Exception("秒杀重复操作");
        } else {
            int count = seckillMapper.updateNumber(seckillId);
            if (count < 0) {
                return new BaseResponse(500, "秒杀结束");
            } else {
                //秒杀成功
                //更新缓存
                Seckill seckill = (Seckill) redisTemplate.boundHashOps(key).get(seckillId);
                if (seckill == null) {
                    seckill = seckillMapper.findById(seckillId);
                    seckill.setNumber(seckill.getNumber() - 1);
                    redisTemplate.boundHashOps(key).put(seckillId, seckill);
                    logger.info("抢购成功!");
                } else {
                    seckill.setNumber(seckill.getNumber() - 1);
                    redisTemplate.boundHashOps(key).put(seckillId, seckill);
                }
                return new BaseResponse(200, "恭喜你抢购成功！");

            }
        }
    }
}