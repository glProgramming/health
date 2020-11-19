package com.itheima.test;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 读取excel
 */
public class POITest {

    /**
     * 方式一：读取excel文件
     */
   // @Test
    public void readExcel() throws IOException, InvalidFormatException {
        //1：创建工作簿对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File("C:\\Users\\Administrator\\Desktop\\read.xlsx"));
        //2：获得工作表对象
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //3：遍历工作表对象 获得行对象
        for (Row row : sheetAt) {
            //4：遍历行对象 获得单元格（列）对象
            for (Cell cell : row) {
                //5：获得数据
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("************************************");
        }
        //6：关闭
        xssfWorkbook.close();

    }

    /**
     * 方式二：读取excel(指定从哪行 哪列读取)
     */
   // @Test
    public void readExecl2() throws IOException, InvalidFormatException {
        //1：创建工作簿对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File("C:\\Users\\Administrator\\Desktop\\read.xlsx"));
        //2：获得工作表对象
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //3.获取最后一行行号
        int lastRowNum = sheetAt.getLastRowNum();
        System.out.println("lastRowNum*******"+lastRowNum);
        //4.遍历每行
        for(int i =0;i<=lastRowNum;i++){
            XSSFRow row = sheetAt.getRow(i);
            //5.获取最后一列列号
            short lastCellNum = row.getLastCellNum();
            System.out.println("lastCellNum*************"+lastCellNum);
            for(int j=0;j<lastCellNum;j++){
                //row.getCell(j)单元格对象 getStringCellValue():获取单元格的值
                System.out.println(row.getCell(j).getStringCellValue());
            }
            System.out.println("***************************************************");
        }
        //6：关闭
        xssfWorkbook.close();
    }

    //@Test
    public void createExcel() throws IOException {
        //1.创建工作簿对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //2.创建工作表对象
        XSSFSheet sheet = xssfWorkbook.createSheet("第一次创建");
        //3.创建行对象
        //4.创建列(单元格)对象, 设置内容
        XSSFRow row = sheet.createRow(0);///第一行
        row.createCell(0).setCellValue("编号");//第一行第一列
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row2 = sheet.createRow(1);///第二行
        row2.createCell(0).setCellValue("001");//第二行第一列
        row2.createCell(1).setCellValue("老王");
        row2.createCell(2).setCellValue("18");
        //5.通过输出流将workbook对象下载到磁盘
        OutputStream outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\create.xlsx");
        xssfWorkbook.write(outputStream);
        xssfWorkbook.close();
    }
}
