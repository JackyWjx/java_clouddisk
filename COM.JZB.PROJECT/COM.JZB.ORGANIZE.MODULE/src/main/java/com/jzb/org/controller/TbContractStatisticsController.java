package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbContractStatisticsService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/org/contractStatistics")
public class TbContractStatisticsController {

    @Autowired
    private TbContractStatisticsService tbContractStatisticsService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbContractStatisticsController.class);

    /**
     * 查询合同统计信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractStatisticsList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractStatisticsList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/contractStatistics/getContractStatisticsList";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定参数
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                response = Response.getResponseError();
            } else {
                // 设置分页参数
                JzbPageConvert.setPageRows(param);

                // 要分页
                param.put("page", 1);

                // 判断时间参数不为空则转换参数值
                if (param.get("beginTime") != null && !param.get("beginTime").toString().equals("")) {
                    param.put("beginTime", JzbDateUtil.getDate(param.get("beginTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                }
                if (param.get("endTime") != null && !param.get("endTime").toString().equals("")) {
                    param.put("endTime", JzbDateUtil.getDate(param.get("endTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                }

                // 获取结果集
                List<Map<String, Object>> list = tbContractStatisticsService.findContractStatisticsList(param);

                // 设置返回对象
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbContractStatisticsService.findContractCount(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractStatisticsList Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/setDeleteStatus", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response setDeleteStatus(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/contractStatistics/setDeleteStatus";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定参数为空返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"staid"})) {
                response = Response.getResponseError();
            } else {
                int count = tbContractStatisticsService.setDeleteStatus(param);
                response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "setDeleteStatus Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractStatistics", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractStatistics(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/contractStatistics/updateContractStatistics";
        boolean flag = true;
        try {
            param = param == null ? new HashMap<>() : param;
            param.put("updtime", System.currentTimeMillis());
            param.put("upduid", userInfo.get("uid").toString());
            int count = tbContractStatisticsService.updateContractStatisticsList(param);
            response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateContractStatistics Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }



    /**
     * 导出Excel
     *
     * @param response
     * @param param
     */
    @RequestMapping(value = "/getContractStatisticsExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void getExcel(HttpServletResponse response, @RequestBody(required = false) Map<String, Object> param) {
        try {

            // 判断时间参数不为空则转换参数值
            if (param.get("beginTime") != null && !param.get("beginTime").toString().equals("")) {
                param.put("beginTime", JzbDateUtil.getDate(param.get("beginTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
            }
            if (param.get("endTime") != null && !param.get("endTime").toString().equals("")) {
                param.put("endTime", JzbDateUtil.getDate(param.get("endTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
            }

            // 获取结果集
            List<Map<String, Object>> contractList = tbContractStatisticsService.findContractStatisticsList(param);


            // 模板路径
            String srcFilePath = "static/excel/contractStatistics.xlsx";
            // 资源路径
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            // 创建输入流
            InputStream in = resource.getInputStream();
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            // 设置样式
            XSSFCellStyle contextStyle = genContextStyle(wb);//创建文本样式
            XSSFCell cell;
            for (int i = 0, l = contractList.size(); i < l; i++) {
                Map<String, Object> userMap = contractList.get(i).get("userInfo") == null ? new HashMap<>() : (Map<String, Object>) contractList.get(i).get("userInfo");
                for (int j = 0; j < 8; j++) {
                    String value = "";
                    switch (j) {

                        case 0:
                            value = i + 1 + "";
                            break;
                        case 1:
                            value = userMap.get("cname") == null ? "" : userMap.get("cname").toString();
                            break;
                        case 2:
                            value = contractList.get(i).get("trackcname") == null ? "" : contractList.get(i).get("trackcname").toString();
                            break;
                        case 3:
                            value = JzbDateUtil.toDateString(JzbDataType.getLong(contractList.get(i).get("tracktime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss);
                            break;
                        case 4:
                            value = contractList.get(i).get("trackcontent") == null ? "" : contractList.get(i).get("trackcontent").toString();
                            break;
                        case 5:
                            value = contractList.get(i).get("trackoutput") == null ? "" : contractList.get(i).get("trackoutput").toString();
                            break;
                        case 6:
                            value = contractList.get(i).get("abdialogue") == null ? "" : contractList.get(i).get("abdialogue").toString();
                            break;
                        case 7:
                            value = contractList.get(i).get("nextadvance") == null ? "" : contractList.get(i).get("nextadvance").toString();
                            break;
                    }

                    // 设置值
                    cell=sheet.getRow(i + 1).createCell(j);
                    cell.setCellValue(value);
                    cell.setCellStyle(contextStyle);
                }
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 设置编码为中文
            response.setCharacterEncoding("UTF-8");
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode("总结统计.xlsx", "UTF-8"));
            // 将excel写入到输出流中
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            // 释放资源
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            JzbTools.logError(ex);
        }
    }


    // 创建文本样式
    public static XSSFCellStyle genContextStyle(XSSFWorkbook workbook){
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);//文本水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//文本竖直居中显示
        style.setWrapText(true);//文本自动换行
        // 生成Excel表单，需要给文本添加边框样式和颜色
        /*
             CellStyle.BORDER_DOUBLE      双边线
             CellStyle.BORDER_THIN        细边线
             CellStyle.BORDER_MEDIUM      中等边线
             CellStyle.BORDER_DASHED      虚线边线
             CellStyle.BORDER_HAIR        小圆点虚线边线
             CellStyle.BORDER_THICK       粗边线
         */
        style.setBorderBottom(BorderStyle.THIN);//设置文本边框
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        // 设置文本边框颜色
        style.setTopBorderColor(new XSSFColor(Color.BLACK));
        style.setBottomBorderColor(new XSSFColor(Color.BLACK));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK));
        style.setRightBorderColor(new XSSFColor(Color.BLACK));

        return style;
    }

}
