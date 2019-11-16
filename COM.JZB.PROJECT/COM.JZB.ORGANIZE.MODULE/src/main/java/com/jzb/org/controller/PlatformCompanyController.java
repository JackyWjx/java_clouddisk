package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.CompanyControllerApi;
import com.jzb.org.api.open.PlatformComApi;
import com.jzb.org.service.PlatformCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("org/platform")
public class PlatformCompanyController {

    @Autowired
    private PlatformCompanyService platService;

    @Autowired
    private PlatformComApi platformComApi;
    @Autowired
    private CompanyControllerApi companyControllerApi;

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
            param.put("status","1");
            int add = platService.insertProductByOpen(param);
            result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
