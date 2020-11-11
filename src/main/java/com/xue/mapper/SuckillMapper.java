package com.xue.mapper;

import com.xue.entity.SuccessKill;

public interface SuckillMapper {

    /**
     * 秒杀成功
     * 保存信息
     * @param record
     */
    void insertRecord(SuccessKill record);
}
