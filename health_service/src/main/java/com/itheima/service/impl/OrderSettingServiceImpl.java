package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置业务逻辑处理层
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void batchAdd(List<OrderSetting> orderSettingList) {
        //多次执行批量导入同一个模板会怎么样？
        if(orderSettingList != null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                //1.根据可预约日期 查询记录是否存在
                editByOrderDate(orderSetting);
            }
        }
    }
    /**
     * 根据年月 查询预约设置数据List<Map></Map>
     */
    @Override
    public List<Map> getOrderSettingByYearMonth(String yearMonth) {
        //yearMonth == '2020-07'
        String startDate = yearMonth+"-1";
        String endDate = yearMonth +"-31";
        //根据年月查询t_ordersetting表，获取可预约人数、已经预约人数、年月
        //select * from t_ordersetting where orderDate between ‘2020-07-01’ and ‘2020-07-31’
        Map<String,String> map = new HashMap<>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByYearMonth(map);
        //将List<OrderSetting>转List<Map>
        List<Map> rsMap = new ArrayList<>();
        if(orderSettingList != null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                Map osMap = new HashMap();
                osMap.put("date",orderSetting.getOrderDate().getDate());//几号
                osMap.put("number",orderSetting.getNumber());//可预约人数
                osMap.put("reservations",orderSetting.getReservations());//已经预约人数

                rsMap.add(osMap);
            }
        }
        return rsMap;
    }
    /**
     * 单个预约设置（根据预约日期修改可预约人数）
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        editByOrderDate(orderSetting);
    }

    /**
     * 公共方法
     * @param orderSetting
     */
    private void editByOrderDate(OrderSetting orderSetting) {
        //1.根据可预约日期 查询记录是否存在
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        //2.如果存在，则更新数据
        if(count>0){
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }
        else
        {
            //3.如果不存在，则插入数据
            orderSettingDao.add(orderSetting);
        }
    }
}
