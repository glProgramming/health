package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 统计分析报表控制层
 */
@RestController
@RequestMapping("/report")
public class ReportController {


    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员数量折线图
     */
    @RequestMapping(value = "/getMemberReport", method = RequestMethod.GET)
    public Result getMemberReport() {
        try {
            Map<String,Object> map = memberService.getMemberReport();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


    /**
     * 套餐预约占比饼形图
     */
    @RequestMapping(value = "/getSetmealReport", method = RequestMethod.GET)
    public Result getSetmealReport() {
        try {
            Map<String,Object> map = setmealService.getSetmealReport();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 运营数据统计报表
     */
    @RequestMapping(value = "/getBusinessReportData", method = RequestMethod.GET)
    public Result getBusinessReportData() {
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    /**
     * 导出运营数据
     */
    /*@RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //1.获取模板需要的数据
            Map<String,Object> result = reportService.getBusinessReportData();
            String reportDate = (String)result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //2.获取模板
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //3.通过poi技术往单元格填充数据
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //遍历热门套餐数据
            int rownum = 12;
            for (Map hs : hotSetmeal) {
                row = sheet.getRow(rownum);
                row.getCell(4).setCellValue((String)hs.get("name"));
                row.getCell(5).setCellValue((Long)hs.get("setmeal_count"));
                row.getCell(6).setCellValue(((BigDecimal)hs.get("proportion")).doubleValue());
                row.getCell(7).setCellValue((String)hs.get("remark"));
                rownum++;
            }
            //4.通过输出流返回浏览器下载本地
            ServletOutputStream outputStream = response.getOutputStream();
            //设置文件类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置文件名称 name一定不能写错 固定的区分大小写
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            workbook.write(outputStream);

            outputStream.flush();//刷新
            outputStream.close();//释放资源
            workbook.close();//关闭workbook

            //5.资源释放关闭
            return null; //成功null
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
    }*/


    @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //1.获取模板需要的数据
            Map<String,Object> result = reportService.getBusinessReportData();
            //2.获取模板
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //3.通过poi技术往单元格填充数据
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(workbook, result);
            //4.通过输出流返回浏览器下载本地
            ServletOutputStream outputStream = response.getOutputStream();
            //设置文件类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置文件名称 name一定不能写错 固定的区分大小写
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            workbook.write(outputStream);
            outputStream.flush();//刷新
            outputStream.close();//释放资源
            workbook.close();//关闭workbook
            //5.资源释放关闭
            return null; //成功null
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
    }
}
