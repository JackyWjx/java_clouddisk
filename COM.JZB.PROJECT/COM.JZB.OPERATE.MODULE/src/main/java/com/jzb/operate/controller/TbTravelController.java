package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.operate.service.TbTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/2 11:21
 */
@RestController
@RequestMapping(value = "/query")
public class TbTravelController {

    @Autowired
    private TbTravelService tbTravelService;

    /**
     * 查询出差记录
     */
    @PostMapping("/list")
    public Response queryTravelList(@RequestBody Map<String, Object> map){
        Response result;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            // 获取出差记录
            List<Map<String , Object>> list = tbTravelService.queryTravelList(map);
            // 根据出差记录获取出差详情  出差记录的id 获取
            for(int i = 0 ; i<list.size();i++){
               Map<String , Object> deMap = new HashMap<>();
               deMap.put("travelid",list.get(i).get("travelid").toString());
                // 获取出差详情 deList<Map>
                List<Map<String , Object>>  deList =  tbTravelService.queryTravelListDeta(deMap);
                for (int j = 0 ;j<deList.size();j++){
                    Map<String,Object> damap=new HashMap<>();
                    damap.put("deid",deList.get(j).get("deid").toString());
                    // 通过出差详情id  获取出差资料信息
                    List<Map<String , Object>> daList = tbTravelService.queryTravelData(damap);
                    //通过出差详情id  获取出差情报信息
                    List<Map<String , Object>> infoList = tbTravelService.queryTravelInfo(damap);
                    // 添加至出差详情里
                    deList.get(j).put("data",daList);
                    deList.get(j).put("info",infoList);
                }
                //list.get(i).put("deta",deList)
                list.get(i).put("deta",deList);
            }
            result =  Response.getResponseSuccess();
            pageInfo.setList(list);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }


    /**
     *  修改出差费用
     */
    @PostMapping("/updateTravelFare")
    public Response updateTravelFare(@RequestBody Map<String, Object> map){
        Response result;
        try {
            result =  tbTravelService.updateTravelFare(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

}
