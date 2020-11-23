package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色持久层接口
 */
public interface RoleDao {
    /**功能二：
     * 2.根据角色id查询 角色 - 菜单中间表（count(*)）
     */
    int findCountMenuByRoleId(Integer id);

    /** 功能二：
     * 1.根据角色id查询角色权限中间表（count(*)）
     */
    int findCountPermissionByRoleId(Integer id);

    /**
     * 根据用户id关联查询角色集合
     * @param userId
     * @return
     */
     Set<Role> findRolesByUserId(Integer userId);

    /**
     *  功能一: 角色分页查询(包含条件)
     */
    Page<Role> selectByCondition(String queryString);


    /**功能二：
     * 3 .根据角色id查询 角色 - 用户中间表（count(*)）
     */
    int findCountUserByRoleId(Integer id);

    /**
     * 功能二：4.根据角色id 删除 角色项
     */
    void deleteById(Integer id);

    /**
     * 功能三: 1.唯一性校验: 根据 Role查询数据库中有无该角色项存在
     */
    Role findRoleCountBy(Role role);

    /**
     *  功能三:2.根据 Role  正常添加 Role 角色
     * @param role
     */
    void add(Role role);

    /**
     *  功能三:3.根据 Map(关系数据 Map) 插入中间表
     */
    void setRoleAndPermission(Map<String, Object> map);

    /**
     * 功能三:4.往角色菜单中间表 遍历插入关系数据
     */
    void setRoleAndMenu(Map<String, Object> map);

    /**
     * 功能三:5. 往角色用户中间表 遍历插入关系数据
     */
    void setRoleAndUser(Map<String, Object> map);

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
     * 功能四: 4.5 根据角色id从 角色 权限 中间表 删除原有的关系数据
     */
    void deleteRoleAndPermissionByRoleId(Integer id);

    /**
     * 功能四: 4.6  根据角色id从 角色 用户 中间表 删除原有的关系数据
     */
    void deleteUserAndRoleByRoleId(Integer id);

    /**
     * 功能四: 4.7 根据角色id从 角色 菜单 中间表 删除原有的关系数据
     */
    void deleteRoleAndMenuByRoleId(Integer id);
    /**
     * 功能四: 4.8 根据角色id 更新角色项数据
     */
    void edit(Role role);
}
