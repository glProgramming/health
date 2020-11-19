package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderSettingService;
import com.itheima.service.SetmealService;
import com.itheima.utils.POIUtils;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.*;

/**
 * 预约设置控制层
 *  批量预约设置
 *  单个预约设置
 *  展示预约数据
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量预约设置（解析excel中的数据 往t_ordersetting数据库插入数据）
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(MultipartFile excelFile) {
        try {
            //1.POIUtils读取Excel对象
            List<String[]> listStr = POIUtils.readExcel(excelFile);
            //2.将List<String[]> 转为List<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<>();
            if(listStr != null && listStr.size()>0){
                for (String[] row : listStr) {
                    //row 每一行数据  ['2020/7/12',500]
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(new Date(row[0]));//可预约日期
                    orderSetting.setNumber(Integer.parseInt(row[1]));//可预约人数
                    orderSettingList.add(orderSetting);
                }
            }
            //3.往数据库插入数据
            orderSettingService.batchAdd(orderSettingList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.UPLOAD_FAIL);
        }
        return new Result(true,MessageConstant.UPLOAD_SUCCESS);
    }

    /**
     * 根据年月 查询预约设置数据List<Map></Map>
     */
    @RequestMapping(value = "/getOrderSettingByYearMonth", method = RequestMethod.GET)
    public Result getOrderSettingByYearMonth(String yearMonth) {
        try {
            List<Map> mapList = orderSettingService.getOrderSettingByYearMonth(yearMonth);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    /**
     * 单个预约设置（根据预约日期修改可预约人数）
     */
    @RequestMapping(value = "/editNumberByDate", method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
