package com.xue.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@Data
public class SuccessKill {
    @Id
    private Long seckillId;
    private Long userId;
    private Integer state;
    private Timestamp create_time;
}
