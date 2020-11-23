package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 检查项控制层
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    //引用服务
    @Reference
    private PermissionService permissionService;


    /**
     *  一、 权限项的分页查询
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    // @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = permissionService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

      /**
       *  二、新增权限项
       * */

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //@PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限校验
    public Result add(@RequestBody Permission permission) {
        try {
            Permission permissionCheckout = permissionService.add(permission);
            if(permissionCheckout==null) {
                return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
            }
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL, permissionCheckout);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }





    /**
     * 三  根据id删除权限
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    //@PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验，使用CHECKITEM_DELETE123测试
    public Result deleteById(Integer permissionId) {
        try {
            permissionService.deleteById(permissionId);
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }



    /**   四、 回显, 根据编辑行的id, 把查询到的权限信息 返回前端
     * 根据id查询权限项数据
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(Integer permissionId) {
        try {
            Permission permission = permissionService.findById(permissionId);
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

       /**
        * 四、 4.2 根据权限id更新权限详情
        */
       @RequestMapping(value = "/edit", method = RequestMethod.POST)
       //@PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限校验
       public Result edit(@RequestBody Permission permission) {
           try {
               permissionService.edit(permission);
               return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
           } catch (Exception e) {
               e.printStackTrace();
               return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
           }
       }
    /**
     *  5.1 新增角色项的回显(查询所有的权限项)
     */

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Permission> permissionList = permissionService.findAll();
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }
}
