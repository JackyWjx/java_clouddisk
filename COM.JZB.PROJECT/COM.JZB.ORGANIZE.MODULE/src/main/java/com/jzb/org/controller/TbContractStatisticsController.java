package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.service.TbCompanyContractService;
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
 * @desc 合同统计信息
 */
@RestController
@RequestMapping(value = "/org/contractStatistics")
public class TbContractStatisticsController {

    @Autowired
    private TbContractStatisticsService tbContractStatisticsService;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private TbCompanyContractService tbCompanyContractService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbContractStatisticsController.class);

    /**
     * 提交到合同库
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addToContractStatistics", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addToContractStatistics(@RequestBody Map<String, Object> param) {
        /**  定义返回值 */
        Response response;

        /**  定义对象接收接口调用人信息 */
        Map<String, Object> userInfo = null;

        /**  定义接口记录Api 路径为当前 Controller-->method */
        String api = "/org/contractStatistics/addToContractStatistics";

        /**  为接口结束记录日志时判断  在catch{} 里改变值 */
        boolean flag = true;

        try {
            /** 如果获取参数userinfo不为空 则记录正常信息 */
            if (param.get("userinfo") != null) {
                /**  接收用户信息 */
                userInfo = (Map<String, Object>) param.get("userinfo");

                /**  接口开始的第一次记录 打印接口信息，用户信息*/
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {

                /**  接口开始的第一次记录 打印接口信息，没有用户信息则为""*/
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            /** 验证指定参数为空则返回error */
            if (JzbCheckParam.haveEmpty(param, new String[]{"conid", "conname", "sales", "ownid", "proname"})) {
                response = Response.getResponseError();
            } else {

                /**  为sql填充参数 */
                param.put("adduid", userInfo.get("uid").toString());
                param.put("addtime", System.currentTimeMillis());
                param.put("staid", JzbRandom.getRandomCharLow(7));

                /**  执行添加方法 */
                int count = tbContractStatisticsService.addToContractStatistics(param);

                /**  合同提交到合同库后需改状态为已入库 */
                if (count > 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("contid", param.get("conid").toString());
                    map.put("upduid", userInfo.get("uid").toString());
                    tbCompanyContractService.updateCompanyContractStatus(map);
                }
                /** 根据添加结果返回接口结果 */
                response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();

            }
        } catch (Exception ex) {

            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addToContractStatistics Method", ex.toString()));

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
                // 获取结果集
                List<Map<String, Object>> list = tbContractStatisticsService.findContractStatisticsList(param);
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> regionMap = new HashMap<>();
                    regionMap.put("key", list.get(i).get("region"));
                    Response cityList = tbCityRedisApi.getCityList(regionMap);
                    // 获取地区map
                    Map<String, Object> resultParam = null;
                    if (cityList.getResponseEntity() != null) {
                        resultParam = (Map<String, Object>) JSON.parse(cityList.getResponseEntity().toString());
                        if (resultParam != null) {
                            resultParam.put("region", resultParam.get("creaid"));
                        } else {
                            resultParam = new HashMap<>();
                            resultParam.put("region", null);
                        }
                    }
                    // 转map
                    if (resultParam != null) {
                        Response response1 = regionBaseApi.getRegionInfo(resultParam);
                        list.get(i).put("region", response1.getResponseEntity());
                    }
                    param.put("uid", list.get(i).get("sales"));
                    // 缓存查询出用户信息
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    // 放入每一条记录
                    list.get(i).put("userInfo", region.getResponseEntity());
                    // 签订时间
                    list.get(i).put("signdate", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("signdate")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                }
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
                param.put("upduid", userInfo.get("uid").toString());
                param.put("updtime", System.currentTimeMillis());
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
     *
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
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
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
            for (int i = 0, l = contractList.size(); i < l; i++) {
                param.put("uid", contractList.get(i).get("sales"));
                // 获取用户信息
                Response region = userRedisServiceApi.getCacheUserInfo(param);
                contractList.get(i).put("userInfo", region.getResponseEntity());
                // 修改一下添加时间
                contractList.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(contractList.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                contractList.get(i).put("signdate", JzbDateUtil.toDateString(JzbDataType.getLong(contractList.get(i).get("signdate")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
            }

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
                Map<String, Object> regionMap = new HashMap<>();
                regionMap.put("key", contractList.get(i).get("region"));
                Response cityList = tbCityRedisApi.getCityList(regionMap);
                // 获取地区map
                Map<String, Object> resultParam = null;
                if (cityList.getResponseEntity() != null) {
                    resultParam = (Map<String, Object>) JSON.parse(cityList.getResponseEntity().toString());
                    if (resultParam != null) {
                        resultParam.put("region", resultParam.get("creaid"));
                    } else {
                        resultParam = new HashMap<>();
                        resultParam.put("region", null);
                    }
                }
                // 转map
                if (resultParam != null) {
                    Response response1 = regionBaseApi.getRegionInfo(resultParam);
                    contractList.get(i).put("region", response1.getResponseEntity());
                }
                Map<String, Object> map = (Map<String, Object>) contractList.get(i).get("region");

                for (int j = 0; j < 20; j++) {
                    String value = "";
                    switch (j) {

                        case 0:
                            value = i + 1 + "";
                            break;
                        case 1:
                            value = contractList.get(i).get("conid") == null ? "" : contractList.get(i).get("conid").toString();
                            break;
                        case 2:
                            String province = map == null || map.get("province") == null ? null : map.get("province").toString();
                            String city = map == null || map.get("city") == null ? "" : "/" + map.get("city").toString();
                            String county = map == null || map.get("county") == null ? "" : "/" + map.get("county").toString();
                            value = province + city + county;
                            break;
                        case 3:
                            value = userMap.get("cname") == null ? "" : userMap.get("cname").toString();
                            break;
                        case 4:
                            value = contractList.get(i).get("conname") == null ? "" : contractList.get(i).get("conname").toString();
                            break;
                        case 5:
                            value = contractList.get(i).get("proname") == null ? "" : contractList.get(i).get("proname").toString();
                            break;
                        case 6:
                            value = contractList.get(i).get("ownname") == null ? "" : contractList.get(i).get("ownname").toString();
                            break;
                        case 7:
                            value = contractList.get(i).get("projectname") == null ? "" : contractList.get(i).get("projectname").toString();
                            break;
                        case 8:
                            value = contractList.get(i).get("concname") == null ? "" : contractList.get(i).get("concname").toString();
                            break;
                        case 9:
                            value = contractList.get(i).get("conperiod") == null ? "" : contractList.get(i).get("conperiod").toString();
                            break;
                        case 10:
                            value = contractList.get(i).get("amount") == null ? "" : contractList.get(i).get("amount").toString();
                            break;
                        case 11:
                            value = contractList.get(i).get("invoicemoney") == null ? "" : contractList.get(i).get("invoicemoney").toString();
                            break;
                        case 12:
                            value = contractList.get(i).get("returnmoney") == null ? "" : contractList.get(i).get("returnmoney").toString();
                            break;
                        case 13:
                            value = contractList.get(i).get("collectmoney") == null ? "" : contractList.get(i).get("collectmoney").toString();
                            break;
                        case 14:
                            value = contractList.get(i).get("signdate") == null ? "" : contractList.get(i).get("signdate").toString();
                            break;
                        case 15:
                            value = contractList.get(i).get("subcname") == null ? "" : contractList.get(i).get("subcname").toString();
                            break;
                        case 16:
                            value = contractList.get(i).get("conimportant") == null ? "" : contractList.get(i).get("conimportant").toString();
                            break;
                        case 17:
                            value = contractList.get(i).get("contype") == null ? "" : contractList.get(i).get("contype").toString();
                            break;
                        case 18:
                            value = contractList.get(i).get("yntable") == null ? "" : contractList.get(i).get("yntable").toString();
                            break;
                        case 19:
                            value = contractList.get(i).get("condesc") == null ? "" : contractList.get(i).get("condesc").toString();
                            break;
                    }

                    // 设置值
                    cell = sheet.getRow(i + 1).createCell(j);
                    cell.setCellValue(value);
                    cell.setCellStyle(contextStyle);
                }
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 设置编码为中文
            response.setCharacterEncoding("UTF-8");
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode("合同统计.xlsx", "UTF-8"));
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
    public static XSSFCellStyle genContextStyle(XSSFWorkbook workbook) {
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
