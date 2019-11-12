package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbScoreManualService;
import com.jzb.operate.util.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
        *@描述
        *@创建人 chenhui
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@RestController
@RequestMapping("/operate/score")
public class TbScoreManualTypeController {
    @Autowired
    TbScoreManualService scoreManual;

    @RequestMapping("/getScoreManualList")
    public Response getScoreManual(@RequestBody Map<String ,Object> paramap){

        Response response;
        try{
            //获取单位总数
            int count = JzbDataType.getInteger(paramap.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                //查询活动总数
                count = scoreManual.getCount(paramap);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(paramap);
            Map<String,Object> userinfo = (Map<String, Object>) paramap.get("userinfo");

            List<Map<String, Object>> list  = scoreManual.getActivity(paramap);
            response = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count > 0 ? count : list.size());

            response.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;



    }




}
