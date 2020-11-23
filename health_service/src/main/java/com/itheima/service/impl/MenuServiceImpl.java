package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MenuDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
     * 获取当前登录用户名
     * @return
     */
    /*public String getUserName(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }*/

    /**
     * 查询所有菜单项数据
     * @return
     */
    @Override
    public List<Menu> findAll(String username) {
        //获取菜单id集合
        List<Integer> menuIdList = getMenuIdList(username);
        List<Menu> parentList = new ArrayList<>();
        for (Integer menuId : menuIdList) {
            Menu level1Menu = menuDao.findMenuByLevel1(menuId);
            if (level1Menu != null) {
                parentList.add(level1Menu);
            }
        }
        //遍历menuList
        for (Menu parent : parentList) {
            List<Menu> childrenList = new ArrayList<>();
            for (Integer menuId : menuIdList) {
                Menu level2Menu = menuDao.findMenuByLevel2AndParentId(parent.getId(),menuId);
                if (level2Menu != null) {
                    childrenList.add(level2Menu);
                }
            }
            parent.setChildren(childrenList);
        }
        return parentList;
    }

    /**
     * 获取当前登录用户权限对应的菜单列表id集合
     * @param username
     * @return
     */
    private List<Integer> getMenuIdList(String username) {
        Integer userId = menuDao.findUserIdByUsername(username);
        Integer roleId = menuDao.findRoleIdByUserId(userId);
        return menuDao.findMenuIdByRoleId(roleId);
    }

    /**
     * 获取当前登录用户对应的角色id
     * @return
     */
    private Integer getRoleId(String username) {
        Integer userId = menuDao.findUserIdByUsername(username);
        return menuDao.findRoleIdByUserId(userId);
    }

    /**
     * 新增菜单项数据
     * @param menu
     * @return
     */
    @Override
    public Result add(Menu menu,String username) {
        String parentPath = getParentPath(menu);
        //根据名称查询菜单表中是否已经有此菜单
        Menu menuByName = menuDao.findByName(menu.getName());
        if (menuByName != null) {
            return new Result(false, MessageConstant.HAS_MENU);
        }else {
            //菜单中插入路径
            menu.setPath(parentPath);
            menuDao.add(menu);
            //设置菜单-角色中间表关系
            Integer id = menu.getId();
            Menu addedMenu = menuDao.findByName(menu.getName());
            menuDao.setRoleIdAndMenuId(getRoleId(username),addedMenu.getId());
            return new Result(true,MessageConstant.ADD_MENU_SUCCESS);
        }
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
    public Result edit(Menu menu) {
        String parentPath = getParentPath(menu);
        //根据名称查询菜单表中是否已经有此菜单
        //Menu menuByName = menuDao.findByName(menu.getName());
        //根据名称和所属菜单id查询菜单表中是否已经有此菜单
        Menu menuByNameAndParentId = menuDao.findByNameAndParentId(menu.getName(),menu.getParentMenuId());
        if (menuByNameAndParentId != null) {
            return new Result(false, MessageConstant.HAS_MENU);
        }else {
            //菜单中插入路径
            menu.setPath(parentPath);
            menuDao.edit(menu);
            return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
        }
    }

    /**
     * 根据id删除菜单项
     * @param id
     * @return
     */
    @Override
    public void deleteById(Integer id){
        //查询当前删除菜单是否有子菜单
        Integer count = menuDao.findCountOfChildrentMenu(id);
        if (count > 0){
            throw new RuntimeException(MessageConstant.DELETE_MENU_FAIL2);
        }
        //删除菜单-角色中间表关系
        menuDao.deleteRoleIdAndMenuId(id);
        menuDao.deleteById(id);
        //todo
    }

    /**
     * 获取当前登录用户菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenuList(String username) {
        //获取菜单id集合
        List<Integer> menuIdList = getMenuIdList(username);
        List<Menu> parentList = new ArrayList<>();
        for (Integer menuId : menuIdList) {
            Menu level1Menu = menuDao.findMenuByLevel1(menuId);
            if (level1Menu != null) {
                parentList.add(level1Menu);
            }
        }
        //遍历menuList
        for (Menu parent : parentList) {
            parent.setTitle(parent.getName());
            List<Menu> childrenList = new ArrayList<>();
            for (Integer menuId : menuIdList) {
                Menu level2Menu = menuDao.findMenuByLevel2AndParentId(parent.getId(),menuId);
                if (level2Menu != null){
                    childrenList.add(level2Menu);
                }
            }
            for (Menu children : childrenList) {
                children.setTitle(children.getName());
            }
            parent.setChildren(childrenList);
        }
        return parentList;
    }




    /**
     * 菜单项分页查询
     * @param queryString
     * @return
     */
    @Override
    public List<Menu> findPage(String queryString,String username) {
        //获取菜单id集合
        List<Integer> menuIdList = getMenuIdList(username);
        //获取一级菜单集合
        List<Menu> parentList = new ArrayList<>();
        for (Integer menuId : menuIdList) {
            Menu level1Menu = menuDao.findMenuByLevel1AndCondition(queryString,menuId);
            if (level1Menu != null) {
                parentList.add(level1Menu);
            }
        }
        //遍历parentList
        if (parentList.size()>0) {
            for (Menu parent : parentList) {
                parent.setTitle(parent.getName());
                List<Menu> childrenList = new ArrayList<>();
                for (Integer menuId : menuIdList) {
                    Menu level2Menu = menuDao.findMenuByLevel2AndParentId(parent.getId(), menuId);
                    if (level2Menu != null){
                        childrenList.add(level2Menu);
                    }
                }
                for (Menu children : childrenList) {
                    children.setTitle(children.getName());
                }
                parent.setChildren(childrenList);
            }
        }else {
            for (Integer menuId : menuIdList) {
                Menu level2Menu = menuDao.findMenuByLevel2AndCondition(queryString,menuId);
                if (level2Menu != null) {
                    parentList.add(level2Menu);
                }
            }
        }
        return parentList;
    }

    /**
     *  1 新增角色项的回显(查询所有的菜单项)
     */
    @Override
    public List<Menu> findAll1() {
        return menuDao.findAll1();
    }
}
