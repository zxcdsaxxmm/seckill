package com.xue.contoller;

import com.xue.common.BaseResponse;
import com.xue.common.Result;
import com.xue.entity.Seckill;
import com.xue.entity.SuccessKill;
import com.xue.service.SeckillService;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private final static Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Resource
    private SeckillService seckillService;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/getAll")
    public List<Seckill> getAll() {
        return seckillService.getAll();
    }

    @GetMapping("getNumber/{id}")
    public Long getNum(@PathVariable long id) {
        return seckillService.getNumber(id);
    }

    @GetMapping("/query/{id}")
    public Seckill query(@PathVariable Long id) {
        return seckillService.findById(id);
    }

    @GetMapping("/update/{seckill_id}")
    public void updateNumber(@PathVariable long seckill_id) {
        seckillService.updateNumber(seckill_id);
    }

    @PostMapping("/startSeckill")
    public BaseResponse startSecondkill(@Param("seckill_Id") long seckillId,
                                        @Param("user_Id") long userId,
                                        @Param("state") int state) throws Exception {
        long num = seckillService.getNumber(seckillId);
        if (num <= 0) {
            return new BaseResponse(500,"秒杀结束!");
        } else {
            BaseResponse response = seckillService.startKill(seckillId, userId, state);
            return response;
        }
    }
}

