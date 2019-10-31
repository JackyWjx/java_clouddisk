package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.MsgTypeParaService;
import com.jzb.message.service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息参数控制层
 * @Author Han Bin
 */
@Controller
@RequestMapping("/message/para")
public class MsgTypeParaController {

    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(MsgTypeParaController.class);

    @Autowired
    MsgTypeParaService service;

    @Autowired
    private ShortMessageService smsService;

    /**
     * 查询消息参数
     *
     *  map 用户参数
     */
    @RequestMapping(value = "/queryMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgTypePara(@RequestBody  Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>queryMsgTypePara");
            List<Map<String , Object>> list ;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            if(map.containsKey("paraname") && !JzbTools.isEmpty(map.get("paraname"))){
                list = service.searchMsgTypePara(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    info.setTotal(service.searchMsgTypeParaCount(map));
                }
            }else{
                list  = service.queryMsgTypePara(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    info.setTotal(service.queryMsgTypeParaCount(map));
                }
            }
            response = Response.getResponseSuccess();
            info.setList(list);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 查询用户参数
     *
     *  map 用户参数
     */
    @RequestMapping(value = "/queryUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response queryUserPara(@RequestBody  Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>queryUserPara");
            List<Map<String , Object>> list ;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            if(map.containsKey("param") && !JzbTools.isEmpty(map.get("param"))){
                list =  service.searchUserPara(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    info.setTotal(service.searchUserParaCount(map));
                }
            }else{
                list =  service.queryUserPara(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    info.setTotal(service.queryUserParaCount(map));
                }
            }
            response = Response.getResponseSuccess();
            info.setList(list);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }


    /**
     * 查询服务商
     *
     *  map 用户参数
     */
    @RequestMapping(value = "/queryServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response queryServiceProviders(@RequestBody  Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>queryUserPara");
            List<Map<String , Object>> list ;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            if(map.containsKey("ispname") && !JzbTools.isEmpty(map.get("ispname"))){
                list =  service.searchServiceProviders(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    info.setTotal(service.searchServiceProvidersCount(map));
                }
            }else{
                list =  service.queryServiceProviders(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    info.setTotal(service.queryServiceProvidersCount(map));
                }
            }
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 添加消息参数
     *
     *  checkcode = appId + secret  + paraname + paracode + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/saveMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgTypePara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>saveMsgGroupConfigure");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String paracode = map.get("paracode").toString();
            String paraname = map.get("paraname").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + paraname + paracode + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.saveMsgTypePara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 添加用户参数
     *
     *  checkcode = appId + secret  + uid + cid + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/saveUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response saveUserPara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>saveMsgGroupConfigure");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid + cid + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.saveUserPara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 添加服务商
     *
     *  checkcode = appId + secret  + msgtype + isp + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/saveServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response saveServiceProviders(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>saveMsgGroupConfigure");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String msgtype = map.get("msgtype").toString();
            String isp = map.get("isp").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + msgtype + isp + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.saveServiceProviders(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 修改消息参数
     *
     *  checkcode = appId + secret  + paraname + paracode + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/upMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgTypePara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>upMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String paracode = map.get("paracode").toString();
            String paraname = map.get("paraname").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + paraname + paracode + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.upMsgTypePara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 修改用户参数
     *
     *  checkcode = appId + secret  + uid + cid + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/upUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserPara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>upMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid + cid + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.updateUserPara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 修改服务商
     *
     *  checkcode = appId + secret  + msgtype + isp + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/upServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response updateServiceProviders(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>upMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String msgtype = map.get("msgtype").toString();
            String isp = map.get("isp").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + msgtype + isp + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.updateServiceProviders(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 禁用消息参数
     *
     *  checkcode =  appId + secret  + paraname + paracode + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/removeMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgTypePara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>removerMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String paracode = map.get("paracode").toString();
            String paraname = map.get("paraname").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + paraname + paracode + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.removeMsgTypePara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 禁用用户参数
     *
     *  checkcode =  appId + secret  + uid + cid + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/removeUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response removeUserPara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>removerMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid + cid + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.removeUserPara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 禁用服务商
     *
     *  checkcode =  appId + secret  + msgtype + isp + checkCode
     *  map 用户参数
     */
    @RequestMapping(value = "/removeServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response removeServiceProviders(@RequestBody Map<String , Object> map){
        Response response;
        try {
            logger.info("===================>>removerMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String msgtype = map.get("msgtype").toString();
            String isp = map.get("isp").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + msgtype + isp + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = service.removeServiceProviders(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

}
