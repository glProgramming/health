package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MenuDao;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author guanpx
 * @create 2020/11/20 16:40
 * @description
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    /**
     * 查询所有菜单项数据
     * @return
     */
    @Override
    public List<Menu> findAll() {
        //List<Menu> menuList = menuDao.findAll();
        List<Menu> parentList = menuDao.findMenuByLevel1();
        //遍历menuList
        for (Menu parent : parentList) {
            List<Menu> childrenList = menuDao.findMenuByLevel2AndParentId(parent.getId());
            parent.setChildren(childrenList);
        }
        return parentList;
    }

    /**
     * 新增菜单项数据
     * @param menu
     * @return
     */
    @Override
    public void add(Menu menu) {
        String parentPath = getParentPath(menu);
        //菜单中插入路径
        menu.setPath(parentPath);
        menuDao.add(menu);
    }

    /**
     * 提取方法:
     * 查询二级菜单所属菜单路径
     * @param menu
     * @return
     */
    private String getParentPath(Menu menu) {
        Integer parentMenuId = menu.getParentMenuId();
        String parentPath = null;
        if (parentMenuId != null) {
            parentPath = menuDao.findParentPathByParentMenuId(parentMenuId);
        }
        return parentPath;
    }

    /**
     * 查询新增菜单上级菜单列表
     * @return
     */
    @Override
    public List<Menu> parentMenus() {
        return menuDao.parentMenus();
    }

    /**
     * 根据id查询菜单项数据
     * @param id
     * @return
     */
    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    /**
     * 根据id更新菜单项数据
     * @param menu
     * @return
     */
    @Override
    public void edit(Menu menu) {
        String parentPath = getParentPath(menu);
        //菜单中插入路径
        menu.setPath(parentPath);
        menuDao.edit(menu);
    }

    /**
     * 根据id删除菜单项
     * @param id
     * @return
     */
    @Override
    public void deleteById(Integer id) {
        menuDao.deleteById(id);
    }

    /**
     * 获取当前登录用户菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenuList() {
        List<Menu> parentList = menuDao.findMenuByLevel1();
        //遍历menuList
        for (Menu parent : parentList) {
            parent.setTitle(parent.getName());
            List<Menu> childrenList = menuDao.findMenuByLevel2AndParentId(parent.getId());
            for (Menu children : childrenList) {
                children.setTitle(children.getName());
            }
            parent.setChildren(childrenList);
        }
        return parentList;
    }
}
