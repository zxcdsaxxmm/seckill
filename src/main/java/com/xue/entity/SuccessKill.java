package com.xue.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class SuccessKill implements Serializable {
    @Id
    private Long seckillId;
    @Id
    private Long userId;
    private Integer state;
    private Timestamp create_time;
}
