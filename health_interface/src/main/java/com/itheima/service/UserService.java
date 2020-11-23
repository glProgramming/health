package com.itheima.service;

import com.itheima.pojo.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 根据用户查询用户对象
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     *  功能三: 新增角色项 (关联的用户项回显,查询所有)
     */
    List<User> findAll();
}
