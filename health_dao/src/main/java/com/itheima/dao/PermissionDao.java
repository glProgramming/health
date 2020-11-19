package com.itheima.dao;

import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.Set;

/**
 * 权限持久层接口
 */
public interface PermissionDao {

    /**
     * 根据角色id关联查询权限集合
     * @param roleId
     * @return
     */
    public Set<Permission> findPermissionsByRoleId(Integer roleId);
}
