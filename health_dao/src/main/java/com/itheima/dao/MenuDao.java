package com.itheima.dao;

import com.itheima.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {

    /**
     * 查询所有菜单项数据
     * @return
     */
    List<Menu> findAll();

    /**
     * 查询一级菜单项
     * @return
     * @param menuId
     */
    Menu findMenuByLevel1(Integer menuId);

    /**
     * 查询一级菜单对应二级菜单项
     * @param parentId
     * @return
     */
    Menu findMenuByLevel2AndParentId(@Param("parentId") Integer parentId, @Param("menuId") Integer menuId);



    /**
     * 新增菜单项数据
     * @param menu
     * @return
     */
    void add(Menu menu);

    /**
     * 根据name查询菜单项数据
     * @param name
     * @return
     */
    Menu findByName(String name);

    /**
     * 根据名称和所属菜单id查询菜单表中是否已经有此菜单
     * @param name
     * @param parentId
     * @return
     */
    Menu findByNameAndParentId(String name, Integer parentId);

    /**
     * 查询二级菜单所属菜单路径
     * @return
     * @param parentMenuId
     */
    String findParentPathByParentMenuId(Integer parentMenuId);

    /**
     * 查询新增菜单上级菜单列表
     * @return
     */
    List<Menu> parentMenus();

    /**
     * 设置菜单-角色中间表关系
     * @param roleId
     * @param menuId
     */
    void setRoleIdAndMenuId(Integer roleId, Integer menuId);



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


    //删除
    /**
     * 根据id删除菜单项
     * @param id
     * @return
     */
    void deleteById(Integer id);

    /**
     * 查询当前删除菜单是否有子菜单
     * @param parentMenuId
     * @return
     */
    Integer findCountOfChildrentMenu(Integer parentMenuId);

    /**
     * 删除菜单-角色中间表关系
     * @param menuId
     */
    void deleteRoleIdAndMenuId(Integer menuId);



    /**
     * 根据条件查询一级菜单对应二级菜单项
     * @param queryString
     * @return
     */
    Menu findMenuByLevel1AndCondition(@Param("queryString") String queryString, @Param("menuId") Integer menuId);

    /**
     * 根据条件查询一级菜单对应二级菜单项
     * @param parentId
     * //@param queryString
     * @return
     */
    //List<Menu> findMenuByLevel2AndParentIdAndCondition(@Param("parentId") Integer parentId,@Param("value") String queryString);
//    List<Menu> findMenuByLevel2AndParentId(Integer parentId);

    /**
     * 根据条件查询二级菜单项
     * @param queryString
     * @return
     */
    Menu findMenuByLevel2AndCondition(@Param("queryString") String queryString, @Param("menuId") Integer menuId);

    /**
     * 根据用户名获取用户id
     * @param username
     * @return
     */
    Integer findUserIdByUsername(String username);

    /**
     * 根据用户id查用户角色表获取角色id
     * @param userId
     * @return
     */
    Integer findRoleIdByUserId(Integer userId);

    /**
     * 根据角色id查角色菜单表获取菜单id
     * @param roleId
     * @return
     */
    List<Integer> findMenuIdByRoleId(Integer roleId);



    /**
     * 获取当前登录用户菜单列表
     * @return
     */
/*    List<Menu> getMenuList();*/
}
