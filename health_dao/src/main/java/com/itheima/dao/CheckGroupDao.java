package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * 检查组服务dao接口
 */
public interface CheckGroupDao {
    /**
     * 保存检查组表
     *
     * @param checkGroup
     */
    int add(CheckGroup checkGroup);

    /**
     * 设置检查组和检查项中间表
     */
    void setCheckGroupAndCheckItem(Map<String, Object> map);

    /**
     * 分页
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);

    /**
     * 根据检查组id查询检查组
     */
    CheckGroup findById(Integer groupId);
    /**
     * 根据检查组id 查询检查项ids
     */
    List<Integer> findCheckItemIdsByGroupId(Integer groupId);

    /**
     * 先根据检查组id从检查组检查项中间表 删除关系数据
     * @param id
     */
    void deleteRelByGroupById(Integer id);

    /**
     * 根据检查组id 更新检查组数据
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 根据检查组id查询检查组检查项中间表
     * @param id
     * @return
     */
    int findCountCheckItemByGroupId(Integer id);

    /**
     * 如果第三步关系不存在，根据检查组id查询检查组套餐数据
     * @param id
     * @return
     */
    int findCountSetmealByGroupId(Integer id);

    /**
     * 根据检查组id删除检查组记录
     * @param id
     */
    void deleteById(Integer id);
    /**
     * 查询所有检查组
     */
    List<CheckGroup> findAll();


    /*关联查询检查组*/
    List<CheckGroup> findCheckGroupListById(Integer id);
}
