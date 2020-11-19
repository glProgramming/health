package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务接口实现类
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    /**
     * 新增检查组
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        //第一步：保存检查组表
        checkGroupDao.add(checkGroup);
        //第二步：获取检查组id
        Integer groupId = checkGroup.getId();
        //第三步：往检查组检查项中间表 遍历插入关系数据
        setCheckGroupAndCheckItem(groupId,checkItemIds);
    }

    /**
     * 分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第二步：查询数据库（代码一定要紧跟设置分页代码  没有手动分页 select * from table where name = 'xx'  ）
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkGroupPage.getTotal(),checkGroupPage.getResult());
    }
    /**
     * 根据检查组id查询检查组
     */
    @Override
    public CheckGroup findById(Integer groupId) {
        return checkGroupDao.findById(groupId);
    }
    /**
     * 根据检查组id 查询检查项ids
     */
    @Override
    public List<Integer> findCheckItemIdsByGroupId(Integer groupId) {
        return checkGroupDao.findCheckItemIdsByGroupId(groupId);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        //1.先根据检查组id从检查组检查项中间表 删除关系数据
        checkGroupDao.deleteRelByGroupById(checkGroup.getId());
        //2.根据页面传入的检查项ids 和 检查组重新建立关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);
        //3根据检查组id 更新检查组数据
        checkGroupDao.edit(checkGroup);
    }
    /**
     * 根据检查组id删除检查组
     */
    @Override
    public void deleteById(Integer id) {
        //1.根据检查组id查询检查组检查项中间表（count(*)）
        int count1 = checkGroupDao.findCountCheckItemByGroupId(id);
        if(count1>0){
            throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_FAIL2);
        }
        //2.如果第三步关系不存在，根据检查组id查询检查组套餐数据（count(*)）
        int count2 = checkGroupDao.findCountSetmealByGroupId(id);
        if(count2>0){
            throw new RuntimeException(MessageConstant.DELETE_SETMEAL_FAIL);
        }
        //3.根据检查组id删除检查组记录
        checkGroupDao.deleteById(id);
    }
    /**
     * 查询所有检查组
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 设置检查组和检查项中间表
     * @param groupId
     * @param checkItemIds
     */
    private void setCheckGroupAndCheckItem(Integer groupId, Integer[] checkItemIds) {
        if(checkItemIds != null && checkItemIds.length>0){
            for (Integer checkItemId : checkItemIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("checkItemId",checkItemId);
                map.put("groupId",groupId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
