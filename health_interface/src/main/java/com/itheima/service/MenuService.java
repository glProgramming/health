package com.itheima.service;

import com.itheima.pojo.Menu;

import java.util.List;

public interface MenuService {

    /**
     * 查询所有菜单项数据
     * @return
     */
    List<Menu> findAll();

    /**
     * 新增菜单项数据
     * @param menu
     * @return
     */
    void add(Menu menu);

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
    void edit(Menu menu);

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
    List<Menu> getMenuList();

}
