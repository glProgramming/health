package com.itheima.service;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组服务接口
 */
public interface CheckGroupService {
    /**
     * 新增检查组
     */
    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    /**
     * 分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 根据检查组id查询检查组
     */
    CheckGroup findById(Integer groupId);
    /**
     * 根据检查组id 查询检查项ids
     */
    List<Integer> findCheckItemIdsByGroupId(Integer groupId);
    /**
     * 编辑检查组
     */
    void edit(CheckGroup checkGroup, Integer[] checkItemIds);
    /**
     * 根据检查组id删除检查组
     */
    void deleteById(Integer id);

    /**
     * 查询所有检查组
     */
    List<CheckGroup> findAll();
}
