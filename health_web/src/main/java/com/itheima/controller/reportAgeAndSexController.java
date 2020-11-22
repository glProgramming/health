package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.ReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/reportAgeAndSex")
public class reportAgeAndSexController {

    @Reference
    private ReportService reportService;

    @RequestMapping("/reportAgeAndSex")
    public Result reportAgeAndSex() {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<Map<String, Object>> AgeAndSex = reportService.reportAgeAndSex();
            resultMap.put("AgeAndSex", AgeAndSex);

            List<String> Names = new ArrayList<String>();
            Names.add("男");
            Names.add("女");
            resultMap.put("Names", Names);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/AgeBand")
    public Result AgeBand() {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            List<Map<String, Object>> ageBand = reportService.AgeBand();
            resultMap.put("AgeBand", ageBand);

            List<String> Names = new ArrayList<String>();
            Names.add("0-18");
            Names.add("18-30");
            Names.add("30-45");
            Names.add("45以上");
            resultMap.put("Names", Names);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }
}