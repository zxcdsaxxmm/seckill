package com.xue.service.impl;

import com.xue.entity.SuccessKill;
import com.xue.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("CheckService")
public class CheckServiceImpl implements CheckService {

    @Autowired
    private Environment env;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户检验：保证每个用户只能抢购一次
     */
   /* @Override
    public boolean CheckSeckillUser(Long seckillId) {
        String key = env.getProperty("seckill.redis.key.prefix") + order.getUserId() + order.getSeckillId();
        return redisTemplate.opsForValue().setIfAbsent(key, "1");
    }*/

}