package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Menu;

import java.util.List;

public interface MenuService {

    /**
     * 查询所有菜单项数据
     * @return
     */
    List<Menu> findAll(String username);

    /**
     * 新增菜单项数据
     * @param menu
     * @return
     */
    Result add(Menu menu, String username);

    /**
     * 查询新增菜单上级菜单列表
     * @return
     */
    List<Menu> parentMenus();

    /**
     * 根据id查询菜单项数据
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 根据id更新菜单项数据
     * @param menu
     * @return
     */
    Result edit(Menu menu);

    /**
     * 根据id删除菜单项
     * @param id
     * @return
     */
    void deleteById(Integer id);

    /**
     * 获取当前登录用户菜单列表
     * @return
     */
    List<Menu> getMenuList(String username);

    /**
     * 菜单项查询
     * @param queryString
     * @return
     */
    List<Menu> findPage(String queryString, String username);

    /**
     *  1 新增角色项的回显(查询所有的菜单项)
     */
    List<Menu> findAll1();
}
