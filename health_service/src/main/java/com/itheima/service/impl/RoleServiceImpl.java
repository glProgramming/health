package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: gl
 * @Date: 2020/11/22 10:57
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    /**
     *  功能一: 角色分页查询(包含条件)
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第二步：查询数据库（代码一定要紧跟设置分页代码  没有手动分页 select * from table where name = 'xx'  ）
        Page<Role> rolePage = roleDao.selectByCondition(queryString);
        return new PageResult(rolePage.getTotal(),rolePage.getResult());
    }

    /**
     * 功能二: 根据角色id删除 角色项
     */
    @Override
    public void deleteById(Integer id) {
        //1.根据角色id查询角色权限中间表（count(*)）
        int count1 = roleDao.findCountPermissionByRoleId(id);
        if(count1>0){
            throw new RuntimeException(MessageConstant.DELETE_PERMISSION_FAIL2);
        }
        //2.根据角色id查询 角色 - 菜单中间表（count(*)）
        int count2 = roleDao.findCountMenuByRoleId(id);
        if(count2>0){
            throw new RuntimeException(MessageConstant.DELETE_MENU_FAIL3);
        }
        //3 .根据角色id查询 角色 - 用户中间表（count(*)）
        int count3 = roleDao.findCountUserByRoleId(id);
        if(count3>0){
            throw new RuntimeException(MessageConstant.DELETE_ROLE_FAIL2);
        }
        //4.根据角色id删除角色id记录
        roleDao.deleteById(id);
    }

    /**
     *  功能三 : 新增角色项
     */
    @Override
    public Role add(Role role, Integer[] permissionIds, Integer[] userIds, Integer[] menuIds) {
        Role roleCheckout =null;
        if (role !=null) {
            roleCheckout =  roleDao.findRoleCountBy(role);
            if (roleCheckout == null) {
                //说明没有重复。可以正常添加
                roleDao.add(role);
                //第二步：获取角色id
                Integer roleId = role.getId();
                //第三步：往角色权限中间表 遍历插入关系数据
                setRoleAndPermission(roleId,permissionIds);
                //第三步：往角色用户中间表 遍历插入关系数据
                setRoleAndUser(roleId,userIds);
                //第四步：往角色菜单中间表 遍历插入关系数据
                setRoleAndMenu(roleId,menuIds);
            }
        }
        return roleCheckout;
    }

    /**
     * 功能四: 4.1 根据 角色 id查询 角色详细信息(回显)
     */
    @Override
    public Role findById(Integer roleId) {
        return  roleDao.findById(roleId);
    }
    /**
     * 功能四:  4.2 根据 角色 id查询  权限的 ids
     */
    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer roleId) {
        return roleDao.findPermissionIdsByRoleId(roleId);
    }

    /**
     * 功能四:  4.3 根据 角色 id查询  用户的 ids
     */
    @Override
    public List<Integer> findUserIdsByRoleId(Integer roleId) {
        return roleDao.findUserIdsByRoleId(roleId);
    }

    /**
     * 功能四:  4.4 根据 角色 id查询  菜单的 ids
     */
    @Override
    public List<Integer> findMenuIdsByRoleId(Integer roleId) {
        return roleDao.findMenuIdsByRoleId(roleId);
    }

    /**
     * 功能四: 4.5 编辑角色项
     */
    @Override
    public void edit(Role role, Integer[] permissionIds, Integer[] userIds, Integer[] menuIds) {
        //1.先根据角色id从 角色 权限 中间表 删除原有的关系数据
        roleDao.deleteRoleAndPermissionByRoleId(role.getId());
        //2.根据页面传入的权限ids 和重新建立 角色 权限  关系
        setRoleAndPermission(role.getId(),permissionIds);

        //1.先根据角色id从 角色 用户 中间表 删除原有的关系数据
        roleDao.deleteUserAndRoleByRoleId(role.getId());
        //2.根据页面传入的权限ids 和重新建立 角色 用户  关系
        setRoleAndUser(role.getId(),userIds);

        //1.先根据角色id从 角色 菜单 中间表 删除原有的关系数据
        roleDao.deleteRoleAndMenuByRoleId(role.getId());
        //2.根据页面传入的权限ids 和重新建立 角色 菜单  关系
        setRoleAndMenu(role.getId(),menuIds);

        //3.根据角色id 更新角色项数据
        roleDao.edit(role);
    }

    /**
     * 功能三 : 往角色菜单中间表 遍历插入关系数据
     */
    private void setRoleAndMenu(Integer roleId, Integer[] menuIds) {
        if(menuIds != null && menuIds.length>0){
            for (Integer menuId : menuIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("menuId",menuId);
                map.put("roleId",roleId);
                roleDao.setRoleAndMenu(map);
            }
        }
    }

    /**
     * 往角色用户中间表 遍历插入关系数据
     */
    private void setRoleAndUser(Integer roleId, Integer[] userIds) {
        if(userIds != null && userIds.length>0){
            for (Integer userId : userIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("userId",userId);
                map.put("roleId",roleId);
                roleDao.setRoleAndUser(map);
            }
        }
    }

    /**
     * 往角色权限中间表 遍历插入关系数据
     */
    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        if(permissionIds != null && permissionIds.length>0){
            for (Integer permissionId : permissionIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("permissionId",permissionId);
                map.put("roleId",roleId);
                roleDao.setRoleAndPermission(map);
            }
        }
    }
}
