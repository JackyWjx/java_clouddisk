package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.entity.uploader.FileInfo;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbDocumentMsgFileInfoService;
import com.jzb.resource.service.TbDocumentMsgLookfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wang jixiang
 * @Date 20191218
 * 体系建设-文件目录的读取，修改，删除
 */
@RequestMapping("/resource/fileMenu")
@RestController
public class FileMenuController {

    //起始路径
    //private String href = "F://files";

    @Autowired
    private TbDocumentMsgFileInfoService tbDocumentMsgFileInfoService;

    @Autowired
    private TbDocumentMsgLookfileService tbDocumentMsgLookfileService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(FileMenuController.class);

    /**
     * 获取文件目录
     *
     * @param param
     * @return
     */
    //传输级别
    @RequestMapping(value = "/getFileMenu", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getFileMenu(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/fileMenu/getFileMenu";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //判断路径是否为起始路径

            List<FileInfo> fileInfo = tbDocumentMsgFileInfoService.getFileInfo(param);
            for (int k = 0, c = fileInfo.size(); k < c; k++) {
                String directory = fileInfo.get(k).getLocation();
                param.put("href", directory);
                List<FileInfo> resFileInfo = tbDocumentMsgFileInfoService.getFileInfo(param);
                for (int i = 0, j = resFileInfo.size(); i < j; i++) {
                    String dir = resFileInfo.get(i).getLocation();
                    param.put("href", dir);
                    List<FileInfo> resInfo = tbDocumentMsgFileInfoService.getFileInfo(param);
                    resFileInfo.get(i).setChildren(resInfo);
                }
                fileInfo.get(k).setChildren(resFileInfo);
            }
            response = Response.getResponseSuccess();
            response.setResponseEntity(fileInfo);


        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
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
     * 创建新的目录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addFileMenu", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addFileMenu(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/fileMenu/addFileMenu";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            String fid = JzbRandom.getRandomChar(19);
            param.put("path", param.get("href") + "/" + param.get("name"));
            param.put("uid", userInfo.get("uid"));
            param.put("addtime", System.currentTimeMillis());
            param.put("uniquefileid", fid);
            response = tbDocumentMsgFileInfoService.addFileMenu(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            List<Map<String,Object>> list = new ArrayList<>();
            Map<String,Object> fidMap = new HashMap<>();
            fidMap.put("uniquefileid",fid);
            list.add(fidMap);
            tbDocumentMsgLookfileService.addLookHistory(list,JzbDataType.getString(userInfo.get("uid")),3);
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
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
     * 修改文件
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/putFileMenu", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response putFileMenu(@RequestBody Map<String, Object> param) {
        Response response = null;
        Map<String, Object> userInfo = null;
        String api = "/resource/fileMenu/putFileMenu";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.allNotEmpty(param, new String[]{"type", "name", "uniquefileid"})) {
                //前端传输全部参数
                param.put("uptime", System.currentTimeMillis());
                if (JzbDataType.getInteger(param.get("type")) == 1) {
                    //这是文件夹

                    //查询href是否发生改变
                    Map<String, Object> map = tbDocumentMsgFileInfoService.getFileInfoPath(param);
                    if (!map.get("path").equals(param.get("href") + "/" + param.get("name"))) {
                        //进行改名或移动操作
                        //修改本身
                        param.put("newPath", param.get("href") + "/" + param.get("name"));
                        param.put("newHref", param.get("href"));
                        tbDocumentMsgFileInfoService.updateItselfFileInfo(param);
                        //查询所有信息
                        List<Map<String, Object>> list = tbDocumentMsgFileInfoService.queryAllInfo(map);
                        List<Map<String, Object>> result = new ArrayList<>();
                        for (int i = 0, j = list.size(); i < j; i++) {
                            if (JzbDataType.getInteger(list.get(i).get("type")) == 1) {
                                //操作的是文件夹
                                String path = (String) list.get(i).get("path");
                                path = path.replace(JzbDataType.getString(map.get("path")), JzbDataType.getString(param.get("href") + "/" + param.get("name")));
                                list.get(i).put("path", path);
                                String href = (String) list.get(i).get("href");
                                href = href.replace(JzbDataType.getString(map.get("path")), JzbDataType.getString(param.get("href") + "/" + param.get("name")));
                                list.get(i).put("href", href);
                                //list.get(i).put("type",list.get(i).get("type"));
                            } else {
                                //操作的是文件
                                String href = (String) list.get(i).get("href");
                                href = href.replace(JzbDataType.getString(map.get("path")), JzbDataType.getString(param.get("href") + "/" + param.get("name")));
                                list.get(i).put("href", href);
                                //list.get(i).put("type",list.get(i).get("type"));
                            }
                            result.add(list.get(i));
                        }
                        if(result.size()>0){
                            response = tbDocumentMsgFileInfoService.batchHierarchyUpdate(result) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                            tbDocumentMsgLookfileService.addLookHistory(result,JzbDataType.getString(userInfo.get("uid")),2);
                        }else {
                            response=Response.getResponseSuccess(userInfo);
                        }
                    }
                    //进行修改备注操作
                    tbDocumentMsgFileInfoService.updateItselfFileInfo(param);

                } else if (JzbDataType.getInteger(param.get("type")) == 0) {
                    //这是文件
                    response = tbDocumentMsgFileInfoService.putFileMenu(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                } else {
                    response = Response.getResponseError();
                }
                List<Map<String,Object>> list = new ArrayList<>();
                Map<String,Object> fidMap = new HashMap<>();
                fidMap.put("uniquefileid",param.get("uniquefileid"));
                list.add(fidMap);
                tbDocumentMsgLookfileService.addLookHistory(list,JzbDataType.getString(userInfo.get("uid")),2);
            } else {
                response = Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
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
     * 修改文件删除状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/putFileMenuStatus", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response putFileMenuStatus(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/fileMenu/putFileMenuStatus";
        boolean flag = true;
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid",userInfo.get("uid"));
            if (JzbCheckParam.allNotEmpty(param, new String[]{"type", "uniquefileid"})) {
                //前端传输全部参数
                if (JzbDataType.getInteger(param.get("type")) == 1) {
                    //这是文件夹

                    //查询href是否发生改变
                    Map<String, Object> map = tbDocumentMsgFileInfoService.getFileInfoPath(param);

                    tbDocumentMsgFileInfoService.delFileInfo(param);
                    //查询所有信息
                    List<Map<String, Object>> list = tbDocumentMsgFileInfoService.queryAllInfo(map);
                    if(list.size()>0){
                        response = tbDocumentMsgFileInfoService.batchHierarchyDelete(list) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                        tbDocumentMsgLookfileService.addLookHistory(list,JzbDataType.getString(userInfo.get("uid")),4);
                    }else {
                        response=Response.getResponseSuccess();
                    }

                } else if (JzbDataType.getInteger(param.get("type")) == 0) {

                    response = tbDocumentMsgFileInfoService.delFileInfo(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                } else {
                    response = Response.getResponseError();
                }
                List<Map<String,Object>> list = new ArrayList<>();
                Map<String,Object> fidMap = new HashMap<>();
                fidMap.put("uniquefileid",param.get("uniquefileid"));
                list.add(fidMap);
                tbDocumentMsgLookfileService.addLookHistory(list,JzbDataType.getString(userInfo.get("uid")),4);

            } else {
                response = Response.getResponseError();
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }
}
