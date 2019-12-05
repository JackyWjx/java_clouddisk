package com.jzb.org.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.org.service.TbDeptUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/5 16:08
 */

@RestController
@RequestMapping("/dept/user")
public class TbDeptUserController {

    @Autowired
    private TbDeptUserService tbDeptUserService;

    /**
     * @Author sapientia
     * @Date 16:21 2019/12/5
     * @Description
     **/
    @RequestMapping(value = "/queryUsernameBydept", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryUsernameBydept(@RequestBody Map<String, Object> param){
        Response response;
        try {
            PageInfo pageInfo = new PageInfo();
            List<Map<String , Object>> list = tbDeptUserService.queryUsernameBydept(param);
            for (Map<String,Object> usermap : list ){
               if (usermap.get("uid") == param.get("uid")) {
                   list.remove(usermap);
                   break;
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
}
