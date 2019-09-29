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
     * 查询
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
     * 模糊查询
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
     * 添加
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
     * 修改
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
     * 禁用
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

}
