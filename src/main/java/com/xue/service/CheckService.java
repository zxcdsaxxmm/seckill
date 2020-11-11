package com.xue.service;

import com.xue.entity.SuccessKill;

public interface CheckService {
    /**
     * 检验用户
     * @param order
     * @return
     */
    public boolean CheckSeckillUser(SuccessKill order);

}
