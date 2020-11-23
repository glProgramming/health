package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Role;

import java.util.List;

/**
 * @Author: gl
 * @Date: 2020/11/22 10:57
 */
public interface RoleService {
    /**
     *  功能一: 角色分页查询(包含条件)
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 功能二: 根据角色id删除 角色项
     */
    void deleteById(Integer id);

    /**
     *  功能三 : 新增角色项
     */
    Role add(Role role, Integer[] permissionIds, Integer[] userIds, Integer[] menuIds);

    /**
     * 功能四: 4.1 根据 角色 id查询 角色详细信息(回显)
     */
    Role findById(Integer roleId);

    /**
     * 功能四:  4.2 根据 角色 id查询  权限的 ids
     */
    List<Integer> findPermissionIdsByRoleId(Integer roleId);

    /**
     * 功能四:  4.3 根据 角色 id查询  用户的 ids
     */
    List<Integer> findUserIdsByRoleId(Integer roleId);

    /**
     * 功能四:  4.4 根据 角色 id查询  菜单的 ids
     */
    List<Integer> findMenuIdsByRoleId(Integer roleId);

    /**
     * 功能四: 4.5 编辑角色项
     */
    void edit(Role role, Integer[] permissionIds, Integer[] userIds, Integer[] menuIds);
}
