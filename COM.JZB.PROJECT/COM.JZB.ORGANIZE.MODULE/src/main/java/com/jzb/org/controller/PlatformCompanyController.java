package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.CompanyControllerApi;
import com.jzb.org.api.open.PlatformComApi;
import com.jzb.org.service.PlatformCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/7 16:51
 */
@RestController
@RequestMapping("/org/platform")
public class PlatformCompanyController {

    @Autowired
    private PlatformCompanyService platService;

    @Autowired
    private PlatformComApi platformComApi;
    @Autowired
    private CompanyControllerApi companyControllerApi;

    @Autowired
    private ProductLineController productLineController;

    /**
     * 开放平台企业列表查询
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchPlatformCom", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchPlatformCom(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("start", rows * (page - 1));
                param.put("pagesize", rows);
                int type = JzbDataType.getInteger(param.get("type"));
                Response plRes = platformComApi.getComAndMan(param);
                Map<String, Object> plMap = platService.getCIdUId(plRes, type);
                param.put("cids", plMap.get("cids"));
                param.put("uids", plMap.get("uids"));
                plMap.remove("cids");
                plMap.remove("uids");
                if (type == 1) {

                } else {
                    //加限制条件
                    //从tb_user_list获取uids
                    Response userRe = companyControllerApi.searchUidByUidCname(param);
                    Map<String, Object> temp = new HashMap<>(2);
                    temp.put("userinfo", param.get("userinfo"));
                    temp.put("uids", userRe.getResponseEntity());
                    //根据开发管理员uid获取cids
                    Response managerRes = platformComApi.getPlatformId(temp);
                    param.put("cidbyuid", platService.getCIds(managerRes).get("cids"));
                }
                List<Map<String, Object>> comList = platService.searchPlatformComList(param, plMap);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(comList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = platService.searchPlatformComCount(param);
                    Info.setTotal(size > 0 ? size : comList.size());
                }
                result.setPageInfo(Info);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 根据企业名称或企业cid集合获取cid合集
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchCidByCidCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCidByCidCname(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("start", 0);
            param.put("pagesize", 100);
            String deList = platService.searchCidByCidCname(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(deList);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 开放平台添加产品
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addProductByOpen", method = RequestMethod.POST)
    @CrossOrigin
    public Response addProductByOpen(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", userInfo.get("uid"));
            param.put("time", System.currentTimeMillis());
            param.put("status", "1");
            param.put("ptype", "1");
            int add = platService.insertProductByOpen(param);

           /*int count =  platService.getProductByOpens(param);
            if (count > 0) {
                count = platService.upsateProductByOpens(param);
            } else {
                 count = platService.insertProductByOpens(param);

            }*/

            //往页面和菜单表中间添加数据
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("pid", param.get("appid"));
            int count1 = platService.getMune(map1);
            if (count1 > 0) {
                int add1 = platService.updateMune(map1);
            }
            count1 = platService.saveMune(list);
            List<Map<String, Object>> list1 = (List<Map<String, Object>>) param.get("list1");
            Map<String, Object> map = new HashMap<>();
            map.put("pid", param.get("appid"));
            int counts = platService.getPage(map);
            if (counts > 0) {
                int adds = platService.updatePage(map);
            }
            counts = platService.savepage(list1);
            result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 私海里面的电脑端授权之后根据cid查询出这个企业下的产品pid
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyProduct", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyProduct(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空，则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                //根据cid查询出来这个企业的产品id
                List<Map<String, Object>> list = platService.getCompanyProduct(param);
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                List<Object> list1 = new ArrayList<>();
                //ProductLineController productLineController = new ProductLineController();
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    Response response = null;
                    if (list.get(i).get("pid") != null || list.get(i).get("pid") != "") {
                        map.put("pid", list.get(i).get("pid"));
                         response = productLineController.getProductMenuList(map);
                    }
                    if (response.getPageInfo().getList() != null && response.getPageInfo().getList().size() > 0) {
                        List list2 = response.getPageInfo().getList();
                        for (int j = 0; j < list2.size(); j++) {
                            list1.add(list2.get(j));
                        }
                    }
                }
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list1);
                result.setPageInfo(pageInfo);
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
