package com.xue.service;

import com.alibaba.fastjson.JSON;
import com.xue.common.Result;
import com.xue.entity.Seckill;
import com.xue.entity.SuccessKill;


import java.util.List;


public interface SeckillService {

    void seckill(SuccessKill order);

    /**
     * 获取所有数据
     */
    List<Seckill> getAll();

    /**
     * 通过id或名字查询商品
     */
    Seckill findById(Long id);

    /**
     * 更新库存
     */
    void updateNumber(Long seckill_id);

    /**
     * 开始秒杀
     */
    void startKill(Long userId, Long seckillId);

    boolean startKill2(Long seckillId,Long userId) throws Exception;
}
