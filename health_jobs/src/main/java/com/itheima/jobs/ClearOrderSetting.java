package com.itheima.jobs;


import com.itheima.dao.OrderSettingDao;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClearOrderSetting {

    @Autowired
    private OrderSettingDao orderSettingDao;


    public  void clear() throws Exception {
        System.out.println("备份历史数据 接着  清理历史数据");
        String date = DateUtils.parseDate2String(new Date());
        //备份历史数据
        orderSettingDao.backup(date);
        //清理历史数据
        orderSettingDao.clear(date);
    }
}
