package com.xue.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Seckill implements Serializable {
    @Id
    private long seckillId;
    private String name;
    private int number;
    private Timestamp start_time;
    private Timestamp end_time;
    private Timestamp create_time;
}
