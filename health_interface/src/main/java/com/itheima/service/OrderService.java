package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map; /**
 * 体检预约接口
 */
public interface OrderService {
    /**
     * 体检预约方法
     * @param map
     * @return
     */
    Result submitOrder(Map map) throws Exception;
    /**
     * 预约成功页面数据展示
     */
    Map findById4Detail(Integer id);
}
