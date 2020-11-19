package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务接口实现类
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional  //2.6.2高些版本事务没有问题 当前2.6.0存在问题
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询所有检查项数据
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
    /**
     * 新增检查项
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页查询
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第二步：查询数据库（代码一定要紧跟设置分页代码  没有手动分页 select * from table where name = 'xx'  ）
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }
    /**
     * 根据id删除检查项
     */
    @Override
    public void deleteById(Integer id) {
        //  （业务复杂的时候写伪代码  或 可以业务流程图 ）
        //1.第一步：根据检查项id查询  检查项检查组中间表
        int count = checkItemDao.findCountByCheckItemId(id);
        //2.第二步：如果中间表有记录 抛出异常 controller捕获异常 返回告知用户
        if(count>0){
            throw  new RuntimeException(MessageConstant.DELETE_CHECKITEM_FAIL2);
        }
        //3.第三步：如果中间表没有记录  根据检查项id删除检查项记录
        checkItemDao.deleteById(id);
    }
    /**
     * 根据id检查项数据
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }
    /**
     * 根据id更新检查项数据
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
}
