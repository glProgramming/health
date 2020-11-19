package com.itheima.service;

import java.util.Map;

/**
 * 运营数据统计报表
 */
public interface ReportService {
    /**
     * 运营数据统计报表
     */
    Map<String,Object> getBusinessReportData() throws Exception;
}
