package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项服务接口
 */
public interface CheckItemService {
    /**
     * 查询所有检查项数据
     */
    List<CheckItem> findAll();
    /**
     * 新增检查项
     */
    void add(CheckItem checkItem);
    /**
     * 检查项分页查询
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 根据id删除检查项
     */
    void deleteById(Integer id);
    /**
     * 根据id检查项数据
     */
    CheckItem findById(Integer id);
    /**
     * 根据id更新检查项数据
     */
    void edit(CheckItem checkItem);
}
