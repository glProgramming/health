package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐服务接口
 */
public interface SetmealService {
    /**
     * 新增套餐
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 套餐分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 回显套餐
     */
    Setmeal findById(Integer setmealId);
    /**
     * 根据套餐id 查询套餐ids
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);
    /**
     * 编辑套餐
     */
    void edit(Setmeal setmeal, Integer[] checkgroupIds);
    /**
     * 删除套餐
     */
    void deleteById(Integer id);
    /**
     * 套餐列表
     */
    List<Setmeal> getSetmeal();

    /**
     * 套餐预约占比饼形图
     */
    Map<String,Object> getSetmealReport();
}
