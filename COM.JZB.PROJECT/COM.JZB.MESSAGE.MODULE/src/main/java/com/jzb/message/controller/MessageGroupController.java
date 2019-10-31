package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.MessageGroupService;
import com.jzb.message.service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业消息
 * @Author Han Bin
 */
@Controller
@RequestMapping("/message/group")
public class MessageGroupController {

    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger(MessageGroupController.class);

    /**
     * 企业消息业务操作
     */
    @Autowired
    MessageGroupService groupService;

    /**
     * 服务层处理
     */
    @Autowired
    private ShortMessageService smsService;

    /**
     * 查询企业消息组
     *
     *
     * map 用户参数
     */
    @RequestMapping("/queryMessageGroup")
    @ResponseBody
    public Response queryMessageGroup(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>queryMessageGroup");
            List<Map<String , Object>> list ;
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            response =  Response.getResponseSuccess();
            if(map.containsKey("groupname") && !JzbTools.isEmpty(map.get("greoupname"))){
                list = groupService.searchMessageGroup(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    pageInfo.setTotal(groupService.searchMessageGroupCount(map));
                }
            }else{
                list  = groupService.listMessageGroup(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    pageInfo.setTotal(groupService.queryMessageGroupCount(map));
                }
            }
            pageInfo.setList(list);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询企业消息组用户
     *
     * map 用户参数
     */
    @RequestMapping("/queryMessageUserGroup")
    @ResponseBody
    public Response queryMessageUserGroup(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>queryMessageUserGroup");
            List<Map<String , Object>> list ;
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            if(map.containsKey("nickname") && !JzbTools.isEmpty(map.get("nickname"))){
                list  = groupService.searchMessageUserGroup(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    pageInfo.setTotal(groupService.searchMessageUserGroupCount(map));
                }
            }else{
                list = groupService.listMessageUserGroup(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    pageInfo.setTotal(groupService.queryMessageUserGroupCount(map));
                }
            }
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询企业消息组配置
     *
     * map 用户参数
     */
    @RequestMapping("/queryMessageGroupConfigure")
    @ResponseBody
    public Response queryMessageGroupConfigure(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>queryMessageGroupConfigure");
            List<Map<String , Object>> list ;
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            if(map.containsKey("context") && !JzbTools.isEmpty(map.get("context"))){
                list  = groupService.searchMsgGroupConfigure(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    pageInfo.setTotal(groupService.searchMsgGroupConfigureCount(map));
                }
            }else{
                list = groupService.listMsgGroupConfigure(map);
                if(JzbDataType.getInteger(map.get("count")) == 0){
                    pageInfo.setTotal(groupService.queryMsgGroupConfigureCount(map));
                }
            }
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加企业消息组
     *
     * checkcode = appId + secret +  msgtype  + cid + groupname + checkCode
     * map 用户参数
     */
    @RequestMapping("/saveMessageGroup")
    @ResponseBody
    public Response saveMessageGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>saveMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupname = map.get("groupname").toString();
            String msgtype = map.get("msgtype").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret +  msgtype  + cid + groupname + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.saveMessageGroup(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加企业消息组用户
     *
     * checkcode = appId + secret  + uid   + cid + groupname + checkCode
     * map 用户参数
     */
    @RequestMapping("/saveMessageUserGroup")
    @ResponseBody
    public Response saveMessageUserGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>saveMessageUserGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupname = map.get("groupid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid   + cid + groupname + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.saveMessageUserGroup(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加企业消息组配置
     *
     * checkcode = appId + secret  + isp   + cid + groupname + checkCode
     * map 用户参数
     */
    @RequestMapping("/saveMessageGroupConfigure")
    @ResponseBody
    public  Response saveMessageGroupConfigure(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>saveMessageGroupConfigure");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupname = map.get("groupid").toString();
            String isp = map.get("isp").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + isp   + cid + groupname + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.saveMsgGroupConfigure(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改企业消息组
     *
     * checkcode = appId + secret  + cid + groupid + checkCode
     * map 用户参数
     */
    @RequestMapping("/upMessageGroup")
    @ResponseBody
    public Response upMessageGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>upMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupid = map.get("groupid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + cid + groupid + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.upMessageGroup(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改企业消息组用户
     *
     * checkcode = appId + secret  + uid   + cid + groupname + checkCode
     * map 用户参数
     */
    @RequestMapping("/upMessageUserGroup")
    @ResponseBody
    public  Response upMessageUserGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>upMessageUserGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupname = map.get("groupid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid   + cid + groupname + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.upMessageUserGroup(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改企业消息组配置
     *
     * checkcode = appId + secret  + uid   + cid + groupname + checkCode
     * map 用户参数
     */
    @RequestMapping("/upMessageGroupConfigure")
    @ResponseBody
    public  Response upMessageGroupConfigure(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>upMessageGroupConfigure");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupname = map.get("groupid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid   + cid + groupname + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.upMsgGroupConfigure(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用企业消息组
     *
     * checkcode = appId + secret  + cid + groupid + checkCode
     * map 用户参数
     */
    @RequestMapping("/removerMessageGroup")
    @ResponseBody
    public  Response removerMessageGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>removerMessageGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupid = map.get("groupid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + cid + groupid + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.removeMessageGroup(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用企业消息组用户
     *
     * checkcode = appId + secret  + uid   + cid + groupname + checkCode
     * map 用户参数
     */
    @RequestMapping("/removerMessageUserGroup")
    @ResponseBody
    public  Response removerMessageUserGroup (@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>removerMessageUserGroup");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupname = map.get("groupid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid   + cid + groupname + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.removeMessageUserGroup(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用企业消息组配置
     *
     * checkcode = appId + secret  + uid   + cid + groupname + checkCode
     * map 用户参数
     */
    @RequestMapping("/removerMessageGroupConfigure")
    @ResponseBody
    public Response removerMessageGroupConfigure(@RequestBody Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>removerMsgGroupConfigure");
            String appId = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String cid = map.get("cid").toString();
            String groupname = map.get("groupid").toString();
            String uid = map.get("uid").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret  + uid   + cid + groupname + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if(md5.equals(map.get("checkcode"))){
                response = groupService.removeMsgGroupConfigure(map) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

}
