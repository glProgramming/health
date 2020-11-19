package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * 角色持久层接口
 */
public interface RoleDao {

    /**
     * 根据用户id关联查询角色集合
     * @param userId
     * @return
     */
    public Set<Role> findRolesByUserId(Integer userId);
}
