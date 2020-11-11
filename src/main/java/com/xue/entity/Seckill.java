package com.xue.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Data
public class Seckill {
    @Id
    private long seckill_id;
    private String name;
    private Integer number;
    private Timestamp start_time;
    private Timestamp end_time;
    private Timestamp create_time;
    private Integer version;
}
