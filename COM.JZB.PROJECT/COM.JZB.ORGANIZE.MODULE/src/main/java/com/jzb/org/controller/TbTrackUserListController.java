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
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.service.TbTrackUserListService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@RequestMapping(value = "/org/trackUserList")
@RestController
public class TbTrackUserListController {

    @Autowired
    private TbTrackUserListService tbTrackUserListService;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTrackUserListController.class);

    /**
     * 查询所有跟进记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTrackUserList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTrackUserList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/trackUserList/getTrackUserList";
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
            // 如果验证指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                response = Response.getResponseError();
            } else {
                // 配置分页参数
                JzbPageConvert.setPageRows(param);
                // 告诉sql  要分页
                param.put("page", 1);
                // 获取结果集
                List<Map<String, Object>> trackList = tbTrackUserListService.findTrackList(param);
                for (int i = 0; i < trackList.size(); i++) {
                    param.put("uid", trackList.get(i).get("trackuid"));
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    trackList.get(i).put("userInfo", region.getResponseEntity());
                    trackList.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(trackList.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                    trackList.get(i).put("tracktime", JzbDateUtil.toDateString(JzbDataType.getLong(trackList.get(i).get("tracktime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                }
                if (userInfo == null) {
                    // 定义返回结果
                    response = Response.getResponseSuccess();
                } else {
                    response = Response.getResponseSuccess(userInfo);
                }
                // 定义分页对象
                PageInfo pageInfo = new PageInfo();
                // 设置list
                pageInfo.setList(trackList);
                // 设置总数
                pageInfo.setTotal(tbTrackUserListService.findTrackListCount());
                // 返回分页对象
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTrackUserList Method", ex.toString()));
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
     * 查询所有跟进记录 (带条件)
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTrackUserListByKeywords", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTrackUserListByKeywords(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/trackUserList/getTrackUserListByKeywords";
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

                // 定义list放uid和cid
                List<Map<String, Object>> list = new ArrayList<>();
                // 定义map便于list添加对象
                Map<String, Object> map = new HashMap<>();
                // 配置参数
                if (!JzbCheckParam.haveEmpty(param, new String[]{"beginTime"})) {
                    param.put("beginTime", JzbDateUtil.getDate(param.get("beginTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                }
                // 配置参数
                if (!JzbCheckParam.haveEmpty(param, new String[]{"endTime"})) {
                    param.put("endTime", JzbDateUtil.getDate(param.get("endTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                }
                // 根据关键字查询出来的单位id
                List<Map<String, Object>> cnameLike = tbTrackUserListService.findCnameLike(param);
                for (int i = 0, l = cnameLike.size(); i < l; i++) {
                    map = new HashMap<>();
                    map.put("value", cnameLike.get(i).get("cid"));
                    list.add(map);
                }
                // 根据关键字查询出来的用户id
                List<Map<String, Object>> unameLike = tbTrackUserListService.findUnameLike(param);
                for (int i = 0, l = unameLike.size(); i < l; i++) {
                    map = new HashMap<>();
                    map.put("value", unameLike.get(i).get("uid"));
                    list.add(map);
                }
                // 把list放到参数中用于查询数据
                param.put("list", list);
                // 设置分页参数
                JzbPageConvert.setPageRows(param);
                // 告诉sql  要分页
                param.put("page", 1);
                List<Map<String, Object>> trackListByKeywords = tbTrackUserListService.findTrackListByKeywords(param);
                // 处理返回数据
                for (int i = 0; i < trackListByKeywords.size(); i++) {
                    param.put("uid", trackListByKeywords.get(i).get("trackuid"));
                    // 缓存查询出用户信息
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    // 放入每一条记录
                    trackListByKeywords.get(i).put("userInfo", region.getResponseEntity());
                    // 转换时间
                    trackListByKeywords.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(trackListByKeywords.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                    // 跟进时间
                    trackListByKeywords.get(i).put("tracktime", JzbDateUtil.toDateString(JzbDataType.getLong(trackListByKeywords.get(i).get("tracktime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                }
                if (userInfo == null) {
                    // 定义返回结果
                    response = Response.getResponseSuccess();
                } else {
                    response = Response.getResponseSuccess(userInfo);
                }
                // 定义分页对象
                PageInfo pageInfo = new PageInfo();
                // 设置list
                pageInfo.setList(trackListByKeywords);
                // 设置总数
                pageInfo.setTotal(tbTrackUserListService.findTrackListCountByKeywords(param));
                // 返回分页对象
                response.setPageInfo(pageInfo);
        } catch (Exception ex) {
            // 异常信息
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTrackUserListByKeywords Method", ex.toString()));
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
    @RequestMapping(value = "/getTrackExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void getExcel(HttpServletResponse response, @RequestBody(required = false) Map<String, Object> param) {
        try {
            // 定义跟进记录数据源
            List<Map<String, Object>> trackList = null;
            // 根据参数是否为空来处理数据源
            if (param == null || JzbCheckParam.haveEmpty(param, new String[]{"keyword"})) {
                param = param == null ? new HashMap<>() : param;
                trackList = tbTrackUserListService.findTrackList(param);
            } else {
                // 定义list放uid和cid
                List<Map<String, Object>> list = new ArrayList<>();
                // 定义map便于list添加对象
                Map<String, Object> map = new HashMap<>();
                // 配置参数
                if (!JzbCheckParam.haveEmpty(param, new String[]{"beginTime"})) {
                    param.put("beginTime", JzbDateUtil.getDate(param.get("beginTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                }
                // 配置参数
                if (!JzbCheckParam.haveEmpty(param, new String[]{"endTime"})) {
                    param.put("endTime", JzbDateUtil.getDate(param.get("endTime").toString(), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                }
                // 根据关键字查询出来的单位id
                List<Map<String, Object>> cnameLike = tbTrackUserListService.findCnameLike(param);
                for (int i = 0, l = cnameLike.size(); i < l; i++) {
                    map = new HashMap<>();
                    map.put("value", cnameLike.get(i).get("cid"));
                    list.add(map);
                }
                // 根据关键字查询出来的用户id
                List<Map<String, Object>> unameLike = tbTrackUserListService.findUnameLike(param);
                for (int i = 0, l = unameLike.size(); i < l; i++) {
                    map = new HashMap<>();
                    map.put("value", unameLike.get(i).get("uid"));
                    list.add(map);
                }
                // 把list放到参数中用于查询数据
                param.put("list", list);
                trackList = tbTrackUserListService.findTrackListByKeywords(param);
            }

            // 处理好数据准备导入EXCEL
            for (int i = 0; i < trackList.size(); i++) {
                param.put("uid", trackList.get(i).get("trackuid"));
                // 获取用户信息
                Response region = userRedisServiceApi.getCacheUserInfo(param);
                trackList.get(i).put("userInfo", region.getResponseEntity());
                // 修改一下添加时间
                trackList.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(trackList.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
            }
            // 模板路径
            String srcFilePath = "static/excel/trackMessage.xlsx";
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
            for (int i = 0, l = trackList.size(); i < l; i++) {
                Map<String, Object> userMap = trackList.get(i).get("userInfo") == null ? new HashMap<>() : (Map<String, Object>) trackList.get(i).get("userInfo");
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
                            value = trackList.get(i).get("trackcname") == null ? "" : trackList.get(i).get("trackcname").toString();
                            break;
                        case 3:
                            value = JzbDateUtil.toDateString(JzbDataType.getLong(trackList.get(i).get("tracktime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss);
                            break;
                        case 4:
                            value = trackList.get(i).get("trackcontent") == null ? "" : trackList.get(i).get("trackcontent").toString();
                            break;
                        case 5:
                            value = trackList.get(i).get("trackoutput") == null ? "" : trackList.get(i).get("trackoutput").toString();
                            break;
                        case 6:
                            value = trackList.get(i).get("abdialogue") == null ? "" : trackList.get(i).get("abdialogue").toString();
                            break;
                        case 7:
                            value = trackList.get(i).get("nextadvance") == null ? "" : trackList.get(i).get("nextadvance").toString();
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
