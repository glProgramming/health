package com.itheima.dao;

import com.itheima.pojo.User;

import java.util.List;

/**
 * 用户持久层接口
 */
public interface UserDao {
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
