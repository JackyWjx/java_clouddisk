package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.CompanyService;
import com.jzb.org.service.NewCompanyCommonService;
import com.jzb.org.service.TbCompanyCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuangbin
 */
@RestController
@RequestMapping(value = "/org/newCompanyCommon")
public class NewCompanyCommonController {
    @Autowired
    private NewCompanyCommonService newCompanyCommonService;

    @Autowired
    private TbCompanyCommonService tbCompanyCommonService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyController companyController;

    /**
     * CRM-销售业主-公海-单位1
     * 点击公海显示所有的单位信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = newCompanyCommonService.getCompanyCommonList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(companyList.size() == 0 ? 0 : JzbDataType.getInteger(companyList.get(0).get("count")));
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 管理员创建公海单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/9/20 18:00
     */
    @RequestMapping(value = "/addCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        try {

                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("authid",JzbDataType.getInteger(param.get("authid")));
                param.put("tcid", JzbRandom.getRandomCharCap(6));
                param.put("cid", JzbRandom.getRandomCharCap(7));
                param.put("status", "1");
                param.put("addtime", System.currentTimeMillis());
                param.put("updtime", System.currentTimeMillis());
                param.put("uid", JzbDataType.getString(userInfo.get("uid")));
                param.put("adduid", JzbDataType.getString(userInfo.get("uid")));
                result = newCompanyCommonService.addCompanyCommonList(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                Map<String,Object> map = new HashMap<>();
                map.put("cid",param.get("cid"));
                result.setResponseEntity(map);

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-单位3
     * 点击修改按钮,进行公海单位修改
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/modifyCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            param.remove("birthday");
            param.put("relphone",param.get("phone"));
            param.put("relperson",param.get("managername"));
            // 修改数据
            int count = tbCompanyCommonService.modifyCompanyCommon(param);
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("upduid", JzbDataType.getString(userInfo.get("uid")));
            if (count >= 1) {
                result = Response.getResponseSuccess(userInfo);
                Map<String, Object> map = companyService.getEnterpriseData(param);
                companyController.comHasCompanyKey(map);
                // 修改公海单位信息
                newCompanyCommonService.modifyCompanyCommonList(param);
            } else if (count == -1){
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity("单位已经认证");
            }else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-单位4
     * 点击删除按钮,进行公海单位删除(私海,联系表没出来  ,暂时没实现)
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/removeCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 返回结果值
            result = newCompanyCommonService.removeCompanyCommonList(param) == 1 ?
                    Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }



   /**
    * @Author Reed
    * @Description // 添加供应商
    * @Date 15:29 2020/1/13
    * @Param [param]
    * @return com.jzb.base.message.Response
   **/
    @RequestMapping(value = "/addCompanyCommonListSuppler", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommonListSuppler(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid",userInfo.get("uid"));
            result = newCompanyCommonService.addCompanyCommonListSuppler(param) > 0 ?
                    Response.getResponseSuccess(userInfo) : Response.getResponseError();

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


}
