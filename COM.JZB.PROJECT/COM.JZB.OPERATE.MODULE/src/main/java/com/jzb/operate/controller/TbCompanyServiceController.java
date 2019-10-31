package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;

import com.jzb.operate.service.TbCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("operate/CompanyService")
public class TbCompanyServiceController {

    @Autowired
    private TbCompanyService tbCompanyService;
    /**
     * 项目跟进
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果参数为为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projected"})) {
                result = Response.getResponseError();
            } else {
                int count = tbCompanyService.saveProject(param);
                if (count > 0) {
                    //存入用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    PageInfo pageInfo = new PageInfo();
                    //返回成功的响应消息
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //否则就输出错误信息
                    result = Response.getResponseError();
                }
            }

        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 项目跟进的查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"projected"})) {
                result = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = tbCompanyService.getProject(param);
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);

            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getCompanyServiceList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyServiceList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 获取前台的总数
            int count = JzbDataType.getInteger(param.get("count"));
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询所有我服务的业主总数
                count = tbCompanyService.getCompanyServiceCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyService.getCompanyServiceList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-2
     * 根据模糊搜索条件获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/searchCompanyServiceList", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCompanyServiceList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyService.searchCompanyServiceList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(0);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
