package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {



        /*
         * 返回到前端数据里
         *1、需要过往一年中每个月
         *  data: res.data.data.months:["2019-11","2019-12","2020-01","2020-02"]
         *
         *2、需要每个月里增加的会员数的数量
         *  data: res.data.data.memberCount: [5, 20, 36, 10, 10, 20]
         * */

        //获取日历对象
        Calendar calendar = Calendar.getInstance();
        //第一个参数表示：月份
        //第二个参数表示：要修改的月份，-12 表示向前推12个月
        calendar.add(Calendar.MONTH, -12);

        //用于存放12个月的月份
        List< String > months = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            //一个月一个月的向前推进
            calendar.add(Calendar.MONTH, 1);
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));

        }
        Map< String, Object > map = new HashMap<>();
        map.put("months", months);

        List< Integer > memberCount = reportService.findMemberCountBeforeDate(months);
        map.put("memberCount", memberCount);

        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, map);
    }


    //需要获取的数据为res.data.data.setmealNames['xxxxx','xxxxx',......]
    //和res.data.data.setmealCount,
    //[
    // {value: xx , name:'xxxxxx'},
    // {value: xx , name:'xxxxxx'},
    //......
    // ]


    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {


        //已经是符合res.data.data.setmealCount中所需的数据，
        // 但是还缺setmealNames，而setmealCount中含有setmealNames的数据
        List< Map< String, Object > > Lists = setmealService.findSetmealCount();

        //获取setmealNames数据
        List< String > setmealNames = new ArrayList<>();
        for (Map< String, Object > list : Lists) {

            String name = (String) list.get("name");
            setmealNames.add(name);
        }

        //获取完毕，返回结果
        Map map = new HashMap<>();
        map.put("setmealCount", Lists);
        map.put("setmealNames", setmealNames);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {

        Map< String, Object > map = reportService.getBusinessReportData();

        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map< String, Object > result = reportService.getBusinessReportData();

            String reportDate = (String) result.get("reportDate");

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

            List< Map > hotSetmeal = (List< Map >) result.get("hotSetmeal");

            //文件目录
            String temlateRealPath =
                    request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //定义工作簿
            XSSFWorkbook workbook =
                    new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            // 默认取第一个子表
            XSSFSheet sheet = workbook.getSheetAt(0);

            //日期
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            //会员数据统计
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);


            //预约出游数据统计
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            //热门套餐
            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            //下载数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");

            //设置下载形式（通过附件的形式下载）
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            //关闭流
            out.flush();
            out.close();
            workbook.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }

    }

}
