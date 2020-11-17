package com.xue.mapper;

import com.xue.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeckillMapper {

    List<Seckill> getAll();

    Seckill findById(Long id);

    int updateNumber(long serckillId);

    long getCount(long seckillId);

}
