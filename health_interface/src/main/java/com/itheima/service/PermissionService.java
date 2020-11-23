package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;

import java.util.List;

/**
 * @Author: gl
 * @Date: 2020/11/21 15:43
 */
public interface PermissionService {
    /**
     * 进行权限页面的分页查询
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     *  二、新增权限项
     * */
    Permission add(Permission permission);

    /**
     * 三、根据id删除权限
     */
    void deleteById(Integer permissionId);

    /**
     * 四、 4.1回显, 根据编辑行的id, 把查询到的权限信息 返回前端
     */
    Permission findById(Integer permissionId);

    /**
     *  四、 4.2 根据权限id更新权限详情
     */
    void edit(Permission permission);

    /**
     *  5.1 新增角色项的回显(查询所有的权限项)
     */
    List<Permission> findAll();
}
