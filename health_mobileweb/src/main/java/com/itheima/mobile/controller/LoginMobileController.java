package com.itheima.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 移动端-登录
 */
@RestController
@RequestMapping("/login")
public class LoginMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登录方法
     */
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Result login(@RequestBody Map map, HttpServletResponse response){
        //1.接收登录数据（手机号、验证码）
        String telephone =(String)map.get("telephone");
        String validateCode =(String)map.get("validateCode");
        //2.校验验证码
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
        if(!StringUtils.isEmpty(validateCode) && !StringUtils.isEmpty(redisCode) && validateCode.equals(redisCode)){
            //3.成功，会员是否存在， 存在跳转index  不存在自动注册
            //根据手机号码查询会员表 看记录是否存在
            Member member = memberService.findByTelephone(telephone);
            if(member == null){
                //自动注册会员
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //往cookie写入数据 返回登录页面
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//所有的页面都可以使用cookie数据
            cookie.setMaxAge(60*60*24*30);//
            response.addCookie(cookie);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }
        else
        {
            //4.失败，返回页面
            return new Result(true,MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
