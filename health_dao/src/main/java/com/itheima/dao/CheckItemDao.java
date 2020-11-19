package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;
/**
 * 检查项服务dao接口
 */
public interface CheckItemDao {
    /**
     * 查询所有检查项数据
     */
    List<CheckItem> findAll();
    /**
     * 新增检查项
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页查询 @Param("aaaa")  //默认属性名values
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据检查项id查询  检查项检查组中间表
     * @param id
     * @return
     */
    int findCountByCheckItemId(Integer id);

    /**
     * 如果中间表没有记录  根据检查项id删除检查项记录
     * @param id
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

    /*关联查询检查项*/
    List<CheckItem> findCheckItemListById(Integer id);
}
