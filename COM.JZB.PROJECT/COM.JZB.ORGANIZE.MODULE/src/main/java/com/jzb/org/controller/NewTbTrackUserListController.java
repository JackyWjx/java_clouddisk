package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.NewTbTrackUserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/3 18:59
 */
@RestController
@RequestMapping("/org/taback")
public class NewTbTrackUserListController {

    @Autowired
    private NewTbTrackUserListService newTbTrackUserListService;

    /**
     * 根据获取跟进记录
     * @param map
     * @return
     */
    @RequestMapping(value = "/queryTrackUserByName", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTrackUserByName(@RequestBody Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            // 获取进度情况
            List<Map<String , Object>> list = newTbTrackUserListService.queryTrackUserListByKey((Map<String, Object>) map.get("param"));
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            response = Response.getResponseError();
            JzbTools.logError(e);
        }
        return response;
    }
}
