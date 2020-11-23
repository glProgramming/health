package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import com.itheima.service.CheckGroupService;
import com.itheima.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组控制层 contrl+alt+o 去除多余的包
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    //引用服务
    @Reference
    private RoleService roleService;


    /**
     *  功能三 : 新增角色项
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ROLE_ADD')")
    public Result add(@RequestBody Role role, Integer[] permissionIds, Integer[] userIds, Integer[] menuIds) {
        try {
            Role roleCheckout = roleService.add(role,permissionIds,userIds,menuIds);
            if(roleCheckout==null) {
                return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
            }
            return new Result(false, MessageConstant.ADD_ROLE_FAIL, roleCheckout);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
    }

    /**
     *  功能一: 角色分页查询(包含条件)
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = roleService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }


   /**
    * 功能四:  4.1 根据 角色 id查询 角色详细信息(回显)
    */
   @RequestMapping(value = "/findById", method = RequestMethod.GET)
   public Result findById(Integer roleId) {
       try {
           Role role = roleService.findById(roleId);
           return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,role);
       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
       }
   }

    /**
     * 功能四:  4.2 根据 角色 id查询  权限的 ids
     */
    @RequestMapping(value = "/findPermissionIdsByRoleId", method = RequestMethod.GET)
    public List<Integer> findPermissionIdsByRoleId(Integer roleId) {
        return roleService.findPermissionIdsByRoleId(roleId);
    }

    /**
     * 功能四:  4.3 根据 角色 id查询  用户的 ids
     */
    @RequestMapping(value = "/findUserIdsByRoleId", method = RequestMethod.GET)
    public List<Integer> findUserIdsByRoleId(Integer roleId) {
        return roleService.findUserIdsByRoleId(roleId);
    }
    /**
     * 功能四:  4.4 根据 角色 id查询  菜单的 ids
     */
    @RequestMapping(value = "/findMenuIdsByRoleId", method = RequestMethod.GET)
    public List<Integer> findMenuIdsByRoleId(Integer roleId) {
        return roleService.findMenuIdsByRoleId(roleId);
    }


    /**
     * 功能四: 4.5 编辑角色项
     */
    @RequestMapping(value="/edit", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    public Result edit(@RequestBody Role role,Integer[] permissionIds, Integer[] userIds, Integer[] menuIds) {
        try {
            roleService.edit(role,permissionIds,userIds,menuIds);
            return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
    }

    /**
     * 功能二: 根据角色id删除 角色项
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public Result deleteById(Integer id) {
        try {
            roleService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }
//
//
//    /**
//     * 查询所有检查组
//     */
//    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
//    public Result findAll() {
//        try {
//            List<CheckGroup> checkGroupList = checkGroupService.findAll();
//            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
//        }catch (Exception e) {
//            e.printStackTrace();
//            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
//        }
//    }



}
