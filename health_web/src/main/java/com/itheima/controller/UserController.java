package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;
    /**
     * 获取用户名 从SecurityContextHolder获取用户数据
     * @return
     */
    @RequestMapping(value = "/getUserName", method = RequestMethod.GET)
    public Result getUserName() {
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /**
     *  功能三: 新增角色项 (关联的用户项回显,查询所有)
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<com.itheima.pojo.User> userList = userService.findAll();
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS,userList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }
}
