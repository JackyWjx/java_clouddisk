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

    private final static Logger logger = LoggerFactory.getLogger(ShortSessageController.class);


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
     */
    @RequestMapping("/queryMessageGroup")
    @ResponseBody
    public Response queryMessageGroup(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>queryMessageGroup");
            List<Map<String , Object>> list ;
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
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
     */
    @RequestMapping("/queryMessageUserGroup")
    @ResponseBody
    public Response queryMessageUserGroup(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            logger.info("===================>>queryMessageUserGroup");
            List<Map<String , Object>> list ;
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
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
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
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
     */
    @RequestMapping("/queryMsgGroupConfigure")
    @ResponseBody
    public Response queryMsgGroupConfigure(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = groupService.listMsgGroupConfigure(map);
            int count  =  groupService.queryMsgGroupConfigureCount(map);
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 模糊查询企业消息组配置
     */
    @RequestMapping("/searchMsgGroupConfigure")
    @ResponseBody
    public Response searchMsgGroupConfigure(@RequestBody Map<String , Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = groupService.searchMsgGroupConfigure(map);
            int count  =  groupService.searchMsgGroupConfigureCount(map);
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加企业消息组
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
     */
    @RequestMapping("/saveMessageUserGroup")
    @ResponseBody
    public Response saveMessageUserGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            response = groupService.saveMessageUserGroup(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加企业消息组配置
     */
    @RequestMapping("/saveMsgGroupConfigure")
    @ResponseBody
    public  Response saveMsgGroupConfigure(@RequestBody Map<String , Object> map){
        Response response;
        try{
            response = groupService.saveMsgGroupConfigure(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改企业消息组
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
     */
    @RequestMapping("/upMessageUserGroup")
    @ResponseBody
    public  Response upMessageUserGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            response = groupService.upMessageUserGroup(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改企业消息组配置
     */
    @RequestMapping("/upMsgGroupConfigure")
    @ResponseBody
    public  Response upMsgGroupConfigure(@RequestBody Map<String , Object> map){
        Response response;
        try{
            response = groupService.upMsgGroupConfigure(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用企业消息组
     */
    @RequestMapping("/removerMessageGroup")
    @ResponseBody
    public  Response removerMessageGroup(@RequestBody Map<String , Object> map){
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
                response = groupService.removeMessageGroup(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
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
     */
    @RequestMapping("/removerMessageUserGroup")
    @ResponseBody
    public  Response removerMessageUserGroup (@RequestBody Map<String , Object> map){
        Response response;
        try{
            response = groupService.removeMessageUserGroup(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用企业消息组配置
     */
    @RequestMapping("/removerMsgGroupConfigure")
    @ResponseBody
    public Response removerMsgGroupConfigure(@RequestBody Map<String , Object> map){
        Response response;
        try{
            response = groupService.removeMsgGroupConfigure(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

}
