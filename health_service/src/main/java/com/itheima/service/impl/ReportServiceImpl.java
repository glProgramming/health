package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 运营数据统计报表
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 运营数据统计报表
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String, Object> rsMap = new HashMap<>();

        //获取日期
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        // 获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 获取本周最后一天的日期
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 获得本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 获取本月最后一天的日期
        String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        // 今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);

        //本周预约数  不需要指定状态（包含所有状态-未到诊 已到诊）
        Map map = new HashMap();
        map.put("begin",thisWeekMonday);
        map.put("end",thisWeekSunday);
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(map);

        // 本月预约数
        Map<String,Object> monthMap = new HashMap<String,Object>();
        monthMap.put("begin",firstDay4ThisMonth);
        monthMap.put("end",lastDay4ThisMonth);
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(monthMap);


        // 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);

        //本周到诊数  状态==‘已到诊’ findVisitsCountAfterDate
        Map map2 = new HashMap();
        map2.put("begin",thisWeekMonday);
        map2.put("end",thisWeekSunday);
        Integer thisWeekVisitsNumber = orderDao.findOrderCountBetweenDate(map2);

        // 本月到诊数
        Map<String,Object> monthMap2 = new HashMap<String,Object>();
        monthMap2.put("begin",firstDay4ThisMonth);
        monthMap2.put("end",lastDay4ThisMonth);
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountBetweenDate(monthMap2);

        //查询热门套餐数据
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        rsMap.put("reportDate",reportDate);
        rsMap.put("todayNewMember",todayNewMember);
        rsMap.put("totalMember",totalMember);
        rsMap.put("thisWeekNewMember",thisWeekNewMember);
        rsMap.put("thisMonthNewMember",thisMonthNewMember);
        rsMap.put("todayOrderNumber",todayOrderNumber);
        rsMap.put("todayVisitsNumber",todayVisitsNumber);
        rsMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        rsMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        rsMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        rsMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        rsMap.put("hotSetmeal",hotSetmeal);
        return rsMap;
    }

    @Override
    public List<Map<String, Object>> reportAgeAndSex() {
        List<Map<String, Object>> objects = new ArrayList<>();

        Map<String, Object> stringMan = new HashMap<>();
        Integer max = memberDao.reportAgeAndSexman();
        stringMan.put("value",max);
        stringMan.put("name","男");
        objects.add(stringMan);

        Map<String, Object> stringWoman = new HashMap<>();
        Integer woman = memberDao.reportAgeAndSexwoman();
        stringWoman.put("value",woman);
        stringWoman.put("name","女");
        objects.add(stringWoman);

        return objects;
    }

    @Override
    public List<Map<String, Object>> AgeBand() throws Exception {
        List<Map<String, Object>> objects = new ArrayList<>();

        Map<String, Object> one = new HashMap<>();
        one.put("value",AgeBandFunction(0,18));
        one.put("name","0-18");
        objects.add(one);

        Map<String, Object> tow = new HashMap<>();
        tow.put("value",AgeBandFunction(18,30));
        tow.put("name","18-30");
        objects.add(tow);

        Map<String, Object> three = new HashMap<>();
        three.put("value",AgeBandFunction(30,45));
        three.put("name","30-45");
        objects.add(three);

        Map<String, Object> may = new HashMap<>();
        may.put("value",AgeBandFunction(45,200));
        may.put("name","45以上");
        objects.add(may);

        return objects;
    }
    public Integer AgeBandFunction(Integer Intstart,Integer Intstop) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        String substring = format.substring(4);//-02-21
        String newYear = format.substring(0,4);//2020
        Integer count =0;
        Date start = null;
        Date stop = null;

        start = simpleDateFormat.parse(Integer.toString(Integer.parseInt(newYear)-Intstart)+substring);
        if (Intstop != null){
            stop = simpleDateFormat.parse(Integer.toString(Integer.parseInt(newYear)-Intstop)+substring);
            count = memberDao.AgeBandFunction(start,stop);
        }else {
            count = memberDao.AgeBandFunctionNull(start);
        }
        return count;
    }
}
