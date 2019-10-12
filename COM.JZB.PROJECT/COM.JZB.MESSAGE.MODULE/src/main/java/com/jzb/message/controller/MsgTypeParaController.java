package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.MsgTypeParaService;
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

    @Autowired
    MsgTypeParaService service;

    /**
     * 查询消息参数
     */
    @RequestMapping(value = "/queryMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgTypePara(@RequestBody  Map<String , Object> map){
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list =  service.queryMsgTypePara(map);
            int count = service.queryMsgTypeParaCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 查询用户参数
     */
    @RequestMapping(value = "/queryUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response queryUserPara(@RequestBody  Map<String , Object> map){
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list =  service.queryUserPara(map);
            int count = service.queryUserParaCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }


    /**
     * 查询服务商
     */
    @RequestMapping(value = "/queryServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response queryServiceProviders(@RequestBody  Map<String , Object> map){
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list =  service.queryServiceProviders(map);
            int count = service.queryServiceProvidersCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }



    /**
     * 模糊查询消息参数
     */
    @RequestMapping(value = "/searchMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response  searchMsgTypePara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list =  service.searchMsgTypePara(map);
            int count = service.searchMsgTypeParaCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 模糊查询用户参数
     */
    @RequestMapping(value = "/searchUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response  searchUserPara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list =  service.searchUserPara(map);
            int count = service.searchUserParaCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 模糊查询服务商
     */
    @RequestMapping(value = "/searchServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response  searchServiceProviders(@RequestBody Map<String , Object> map){
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list =  service.searchServiceProviders(map);
            int count = service.searchServiceProvidersCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 添加消息参数
     */
    @RequestMapping(value = "/saveMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgTypePara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.saveMsgTypePara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 添加用户参数
     */
    @RequestMapping(value = "/saveUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response saveUserPara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.saveUserPara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 添加服务商
     */
    @RequestMapping(value = "/saveServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response saveServiceProviders(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.saveServiceProviders(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 修改消息参数
     */
    @RequestMapping(value = "/upMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgTypePara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.upMsgTypePara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 修改用户参数
     */
    @RequestMapping(value = "/updateUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response updateUserPara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.updateUserPara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 修改服务商
     */
    @RequestMapping(value = "/updateServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response updateServiceProviders(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.updateServiceProviders(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 禁用消息参数
     */
    @RequestMapping(value = "/removeMsgTypePara",method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgTypePara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.removeMsgTypePara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 禁用用户参数
     */
    @RequestMapping(value = "/removeUserPara",method = RequestMethod.POST)
    @ResponseBody
    public Response removeUserPara(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.removeUserPara(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     * 禁用服务商
     */
    @RequestMapping(value = "/removeServiceProviders",method = RequestMethod.POST)
    @ResponseBody
    public Response removeServiceProviders(@RequestBody Map<String , Object> map){
        Response response;
        try {
            response = service.removeServiceProviders(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

}
