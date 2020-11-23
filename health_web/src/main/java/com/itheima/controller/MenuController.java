package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guanpx
 * @create 2020/11/20 16:01
 * @description
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    /**
     * 获取当前登录用户名
     * @return
     */
    public String getUserName(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

    /**
     * 查询所有菜单项数据
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        try {
            //调用业务服务
            List<Menu> menuList = menuService.findAll(getUserName());
            //处理结果，查询成功
            return new Result(true, MessageConstant.GET_MENU_SUCCESS,menuList);
        } catch (Exception e) {
            e.printStackTrace();
            //查询失败
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    /**
     * 菜单项分页查询
     * @param
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            List<Menu> menuList = menuService.findPage(queryPageBean.getQueryString(),getUserName());
            return new Result(true,MessageConstant.GET_MENULIST_SUCCESS,menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENULIST_FAIL);
        }
    }

    /**
     * 新增菜单项数据
     * @param menu
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            //调用业务服务
            return menuService.add(menu,getUserName());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_MENU_FAIL);
        }
    }

    /**
     * 查询新增菜单上级菜单列表
     * @return
     */
    @GetMapping("/parentMenus")
    public Result parentMenus(){
        try {
            //调用业务服务
            List<Menu> parentMenusList = menuService.parentMenus();
            //处理结果，查询成功
            return new Result(true,"查询菜单列表成功",parentMenusList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询菜单列表失败");
        }
    }

    /**
     * 根据id查询菜单项数据
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer id){
        try {
            //调用业务服务
            Menu menu = menuService.findById(id);
            //处理结果，查询成功
            return new Result(true,MessageConstant.GET_MENU_SUCCESS,menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
    }

    /**
     * 根据id更新菜单项数据
     * @param menu
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            //调用业务服务,返回结果
            return menuService.edit(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MENU_FAIL);
        }
    }

    /**
     * 根据id删除菜单项
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            //调用业务服务
            menuService.deleteById(id);
            //处理结果，查询成功
            return new Result(true,MessageConstant.DELETE_MENU_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_MENU_FAIL);
        }
    }

    /**
     * 获取当前登录用户菜单列表
     * @return
     */
    @GetMapping("/getMenuList")
    public Result getMenuList(){
        try {
            //调用业务服务
            List<Menu> menuList = menuService.getMenuList(getUserName());
            //处理结果，查询成功
            return new Result(true,MessageConstant.GET_MENULIST_SUCCESS,menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENULIST_FAIL);
        }
    }
    /**
     *  1 新增角色项的回显(查询所有的菜单项)
     */
    @RequestMapping("/findAll1")
    public Result findAll1() {
        try {
            List<Menu> menuList = menuService.findAll1();
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS,menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }
}
