package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: gl
 * @Date: 2020/11/21 15:45
 */
@Service( interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    /**
     * 第一个 功能进行权限页面的分页查询
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第二步：查询数据库（代码一定要紧跟设置分页代码  没有手动分页 select * from table where name = 'xx'  ）
        Page<Permission> permissionPage = permissionDao.selectByCondition(queryString);
        return new PageResult(permissionPage.getTotal(), permissionPage.getResult());
    }

    /**
     *  二、新增权限项
     *  第一： permission 不为空
     *  第二： permission.name 或者 keyword 经查询后 不能 重复（int  count）
     * */
    @Override
    public Permission add(Permission permission) {
        Permission permissionCheckout =null;
        if (permission !=null) {
            permissionCheckout =  permissionDao.findPermissionCountBy(permission);
            if (permissionCheckout == null) {
                //说明没有重复。可以正常添加
                permissionDao.add(permission);
            }
        }
        return permissionCheckout;
    }

    /**
     * 三、根据id删除权限
     */
    @Override
    public void deleteById(Integer permissionId) {
        //  （业务复杂的时候写伪代码  或 可以业务流程图 ）
        //1.第一步：根据检查项id查询  检查项检查组中间表
        int count = permissionDao.findCountByPermissionId(permissionId);
        //2.第二步：如果中间表有记录 抛出异常 controller捕获异常 返回告知用户
        if(count>0){
            throw  new RuntimeException(MessageConstant.DELETE_PERMISSION_FAIL2);
        }
        //3.第三步：如果中间表没有记录  根据检查项id删除检查项记录
        permissionDao.deleteById(permissionId);

    }


    /**
     * 4.1回显, 根据编辑行的id, 把查询到的权限信息 返回前端
     * @param permissionId
     * @return
     */
    @Override
    public Permission findById(Integer permissionId) {
        return permissionDao.findById(permissionId);
    }

    /**
     *  四、 4.2 根据权限id更新权限详情
     */
    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    /**
     *  5.1 新增角色项的回显(查询所有的权限项)
     */
    @Override
    public List<Permission> findAll() {
       return permissionDao.findAll();
    }

}
