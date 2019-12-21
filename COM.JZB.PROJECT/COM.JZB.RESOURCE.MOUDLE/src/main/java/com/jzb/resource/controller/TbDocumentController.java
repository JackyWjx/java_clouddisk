package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.resource.api.redis.auth.TbAuthApi;

import com.jzb.resource.service.TbDocumentMsgPowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 体系建设文档管理
 * @author lifei
 */
@RestController
@RequestMapping(value = "/resource/docuMent")
public class TbDocumentController {

    @Autowired
    TbDocumentMsgPowerService tbDocumentMsgPowerService;
    @Autowired
   private TbAuthApi tbAuthApi;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbDocumentController.class);
        //1.通过 api/auth/getGroupUser


    /**
     * 批量插入 修改 文档记录权限
     *
     * @lifei
     *
     * */
    @RequestMapping(value = "/addUpPowerList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addPowerList(@RequestBody(required = false) Map<String,Object> param) {
        Response result= null;
        Map<String, Object> userInfo = null;
        String api = "/resource/docuMent/addUpPowerList";
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("uid", userInfo.get("uid"));

            Response docMsgPower = tbAuthApi.getDocMsgPower(param);
            if (JzbDataType.getInteger(docMsgPower.getResponseEntity()) != 0) {
                result.setResponseEntity("没有权限");
            } else {
               List<Map<String, Object>> list=tbDocumentMsgPowerService.queryPowerList();
               List<Map<String, Object>> qlist = (List<Map<String, Object>>) param.get("list");
               List<Map<String,Object>> uplist=new ArrayList<Map<String, Object>>();

            for (int i=0,c=list.size();i<c;i++) {
                Map<String, Object> map = list.get(i);
                // 迭代器
                Iterator<Map<String, Object>> it = qlist.iterator();
                while (it.hasNext()) {
                    Map<String, Object> sublevelMap = it.next();
                    if (map.get("uid").equals(sublevelMap.get("uid"))) {
                        uplist.add(sublevelMap);
                        // 迭代器删除list对象
                        it.remove();
                        break;
                    }
                }
            }
                //批量
                int addcount= tbDocumentMsgPowerService.addPowerList(qlist);
                int upcont= tbDocumentMsgPowerService.upPowerList(uplist);
                if (upcont>0 ||addcount>0){
                    result = Response.getResponseSuccess(userInfo);
                }
            }

        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addPowerList Method", e.toString()));
        }
        return result;
    }










}
