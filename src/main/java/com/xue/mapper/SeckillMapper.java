package com.xue.mapper;

import com.xue.entity.Seckill;

import java.util.List;


public interface SeckillMapper {

    List<Seckill> getAll();

    Seckill findById(Long id);

    long updateNumber(long serckillId);

    long getCount(long seckillId);

}
