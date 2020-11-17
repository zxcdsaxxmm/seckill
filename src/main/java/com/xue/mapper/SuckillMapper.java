package com.xue.mapper;

import com.xue.entity.SuccessKill;
import org.apache.ibatis.annotations.Param;


public interface SuckillMapper {

    /**
     * 秒杀成功
     * 保存信息
     *
     * @param
     */
    int insertRecord(@Param("seckill_id") long seckillId,
                      @Param("user_id") long userId,
                      @Param("state") int state);


    SuccessKill findById(Long seckillId);
}
