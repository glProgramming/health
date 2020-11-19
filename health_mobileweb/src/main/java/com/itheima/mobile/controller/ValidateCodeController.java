package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 发送验证码
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 套餐列表
     */
    @RequestMapping(value = "/send4Order", method = RequestMethod.POST)
    public Result send4Order(String telephone) {
        try {
             //1.生成验证码（调用验证码工具类）
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //2.调用阿里云短信工具类（手机号码 验证码模板 验证码）
            if(false){
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
            }
            System.out.println("telephone:::"+telephone+"::::"+code.toString());
            //3.将验证码存入redis (提交体检预约验证使用)
            //key:业务类型区别+唯一标识
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,5*60,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 手机号码快速登录-验证码
     */
    @RequestMapping(value = "/send4Login", method = RequestMethod.POST)
    public Result send4Login(String telephone) {
        try {
            //1.生成验证码（调用验证码工具类）
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //2.调用阿里云短信工具类（手机号码 验证码模板 验证码）
            if(false){
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
            }
            System.out.println("telephone:::"+telephone+"::::"+code.toString());
            //3.将验证码存入redis (提交体检预约验证使用)
            //key:业务类型区别+唯一标识 5分钟 10分钟
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone,5*60,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

}
