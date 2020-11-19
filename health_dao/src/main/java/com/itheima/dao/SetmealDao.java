package com.itheima.dao;
import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map; /**
 * 套餐接口
 */
public interface SetmealDao {
    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 设置中间表（检查组 套餐）
     * @param map
     */
    void setCheckGroupAndCheckSetmeal(Map<String, Object> map);

    /**
     * 套餐分页
     * @param queryString
     * @return
     */
    Page<Setmeal> selectByCondition(String queryString);
    /**
     * 回显套餐
     */
    Setmeal findById(Integer setmealId);
    /**
     * 根据套餐id 查询套餐ids
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    /**
     * 先根据套餐id从检查组套餐中间表 删除关系数据
     * @param id
     */
    void deleteRelBySetmealId(Integer id);

    /**
     * 根据套餐id 更新套餐数据
     * @param setmeal
     */
    void edit(Setmeal setmeal);

    /**
     * 根据套餐id查询中间表是否存在关联关系
     * @param id
     * @return
     */
    int findCountGroupBySetmealId(Integer id);

    /**
     * 先根据id删除套餐数据
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 套餐列表
     */
    List<Setmeal> getSetmeal();

    /**
     * 获取List<Map> 套餐名称、套餐预约数量
     * @return
     */
    List<Map> findSetmealNameCount();


    /**
     *
     */
}
