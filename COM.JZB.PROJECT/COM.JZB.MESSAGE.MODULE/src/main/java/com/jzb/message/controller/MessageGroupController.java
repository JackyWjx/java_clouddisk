package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.MessageGroupService;
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
     * 企业消息业务操作
     */
    @Autowired
    MessageGroupService groupService;

    /**
     * 查询企业消息组
     */
    @RequestMapping("/queryMessageGroup")
    @ResponseBody
    public Response queryMessageGroup(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = groupService.listMessageGroup(map);
            int count  =  groupService.queryMessageGroupCount(map);
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
     * 查询企业消息组用户
     */
    @RequestMapping("/queryMessageUserGroup")
    @ResponseBody
    public Response queryMessageUserGroup(@RequestBody  Map<String , Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = groupService.listMessageUserGroup(map);
            int count  =  groupService.queryMessageUserGroupCount(map);
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
     * 模糊查询企业消息组
     */
    @RequestMapping("/searchMessageGroup")
    @ResponseBody
    public Response searchMessageGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = groupService.searchMessageGroup(map);
            int count  =  groupService.searchMessageGroupCount(map);
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
     * 模糊查询企业消息组用户
     */
    @RequestMapping("/searchMessageUserGroup")
    @ResponseBody
    public Response searchMessageUserGroup(@RequestBody Map<String , Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = groupService.searchMessageUserGroup(map);
            int count  =  groupService.searchMessageUserGroupCount(map);
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
            response = groupService.saveMessageGroup(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
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
            response = groupService.upMessageGroup(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
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
            response = groupService.removeMessageGroup(map) ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
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
