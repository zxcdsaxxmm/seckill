package com.xue.service;

import com.xue.common.BaseResponse;
import com.xue.entity.Seckill;


import java.util.List;


public interface SeckillService {


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
    void updateNumber(Long seckillId);

    /**
     * 获取库存数
     */
    long getNumber(long seckillId);

    /**
     * 开始秒杀
     */
    BaseResponse startKill(Long userId, Long seckillId, int state) throws Exception;
}
