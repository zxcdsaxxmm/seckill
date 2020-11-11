package com.xue.contoller;

import com.xue.common.BaseResponse;
import com.xue.common.Result;
import com.xue.entity.Seckill;
import com.xue.entity.SuccessKill;
import com.xue.service.CheckService;
import com.xue.service.SeckillService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private final static Logger logger = LoggerFactory.getLogger(SeckillController.class);
    @Resource
    private SeckillService seckillService;

    @Resource
    private CheckService checkService;

    @GetMapping("/getAll")
    public List<Seckill> getAll() {
        return seckillService.getAll();
    }

    @GetMapping("/query")
    public Seckill query(@RequestParam Long id) {
        return seckillService.findById(id);
    }

    @GetMapping("/update/{seckill_id}")
    public void updateNumber(@PathVariable long seckill_id) {
        seckillService.updateNumber(seckill_id);
    }

    @PostMapping("/startSeckill")
    public BaseResponse startSecondkill(HttpSession session, @RequestBody SuccessKill order) {
        // session.getAttribute("order");
        BaseResponse baseResponse = new BaseResponse(Result.Success);
        try {
            //判断该用户是否首次秒杀该商品
            if (checkService.CheckSeckillUser(order)) {
                seckillService.seckill(order);

            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            baseResponse = new BaseResponse(Result.Fail);
        }
        return baseResponse;
    }
}

