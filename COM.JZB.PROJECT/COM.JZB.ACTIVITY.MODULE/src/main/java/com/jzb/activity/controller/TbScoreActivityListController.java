package com.jzb.activity.controller;

import com.jzb.activity.service.TbScoreActivityListService;
import com.jzb.activity.util.PageConvert;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
        *@描述 查询积分页面活动列表
`       *@创建人 chenhui
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@RestController
@RequestMapping("/score/activity")
public class TbScoreActivityListController {
    @Autowired
    TbScoreActivityListService scoreService;


    @RequestMapping("/getScoreActivityList")
    @ResponseBody
    public Response getScoreActivityList(@RequestBody Map<String, Object> paramap){
        Response response;
        try{
            //获取单位总数
            int count = JzbDataType.getInteger(paramap.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                //查询活动总数
                count = scoreService.getCount(paramap);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(paramap);
            Map<String,Object> userinfo = (Map<String, Object>) paramap.get("userinfo");
            paramap.put("uid",userinfo.get("uid"));
            List<Map<String, Object>> list  = scoreService.getActivity(paramap);
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
