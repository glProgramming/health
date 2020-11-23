package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;

import java.util.List;
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

    /**
     * 第一个 功能进行权限页面的分页查询
     */
    Page<Permission> selectByCondition(String queryString);

    /**
     *  第二功能 2.1 根据前端新增数据， 查询 有无对应的permission
     */
    Permission findPermissionCountBy(Permission permission);

    /**
     *  第二功能 2.2 新增permission 项
     */
    void add(Permission permission);

    /**
     * 三、根据id删除权限
     * 3.1 查询中间表有没有
     */
    int findCountByPermissionId(Integer permissionId);

    /**
     * 第三步：如果中间表没有记录  根据检查项id删除检查项记录
     */

    void deleteById(Integer permissionId);

    /**
     * 4.1回显, 根据编辑行的id, 把查询到的权限信息 返回前端
     * @param permissionId
     * @return
     */
    Permission findById(Integer permissionId);

    /**
     * 四、 4.2 根据权限id更新权限详情
     * @param permission
     */
    void edit(Permission permission);
    /**
     *  5.1 新增角色项的回显(查询所有的权限项)
     */
    List<Permission> findAll();
}
