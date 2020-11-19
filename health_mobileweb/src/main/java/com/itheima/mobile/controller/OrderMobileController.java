package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * 提交预约
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;


    @Reference
    private OrderService orderService;

    /**
     * 套餐列表
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result submit(@RequestBody Map map) {
        try {
            //{"setmealId":"12","sex":"1","orderDate":"2020-07-16",
               //     "name":"张三","telephone":"13510773927","validateCode":"1234","idCard":"512501197203035172"}
            String telephone = (String)map.get("telephone");
            String validateCode = (String)map.get("validateCode");
            //1.验证码校验
            String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
            //2.校验通过
            if(!StringUtils.isEmpty(validateCode) && !StringUtils.isEmpty(redisCode) && validateCode.equals(redisCode)){
                //3.调用服务
                //设置预约类型 微信预约
                map.put("orderType",Order.ORDERTYPE_WEIXIN);
                Result result = orderService.submitOrder(map);
                //4.成功 发送通知短信
                if(result.isFlag()){
                    //发送短信
                    System.out.println("预约成功，发送短信也成功了。。。。");
                    if(false){
                         Order order = (Order)result.getData();
                        SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate()));
                    }
                }
                return result;
            }
            //4.校验失败 返回错误信息
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }

    /**
     * 预约成功页面数据展示
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById4Detail(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
