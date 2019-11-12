package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.TbCompanySupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 已分配的业主下供应商模块 与公海类似
 */
@RestController
@RequestMapping(value = "/org/companySupplier")
public class TbCompanySupplierController {

    @Autowired
    private TbCompanySupplierService tbCompanySupplierService;

    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    /**
     * 获取供应商
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanySupplier", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response getCompanySupplier(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "count"})) {
                result = Response.getResponseError();
            } else {
                // 设置参数
                JzbPageConvert.setPageRows(param);
                // 获取结果
                List<Map<String, Object>> list = tbCompanySupplierService.queryCompanySupplier(param);

                // 循环list 赋值
                for (int i = 0, l = list.size(); i < l; i++) {
                    Map<String, Object> map = new HashMap<>();
                    // 从返回结果中获取地区id
                    map.put("key", list.get(i).get("region"));
                    // 从redis 获取地区信息
                    Response cityList = tbCityRedisApi.getCityList(map);
                    // 获取地区map
                    Map<String, Object> resultParam = (Map<String, Object>) cityList.getResponseEntity();
                    list.get(i).put("city", resultParam.get("city"));
                    list.get(i).put("province", resultParam.get("province"));
                    list.get(i).put("county", resultParam.get("county"));
                    list.get(i).put("creaid", resultParam.get("creaid"));
                }
                // 分页对象
                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                // 如果前端传的count 大于0 则返回list大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? list.size() : 0);
                // 获取用户信息返回
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pageInfo);

            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 添加供应商
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanySupplier", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response addCompanySupplier(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{""})) {
                result = Response.getResponseError();
            } else {
                tbCompanySupplierService.addCompanySupplier(param);
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 修改供应商
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCompanySupplier", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response updateCompanySupplier(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{""})) {
                result = Response.getResponseError();
            } else {
                tbCompanySupplierService.updateCompanySupplier(param);
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-供应商6
     * 删除供应商
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/removeCompanySupplier", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeCompanySupplier(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("upduid", JzbDataType.getString(userInfo.get("uid")));
            // 返回删除数
            int count = tbCompanySupplierService.removeCompanySupplier(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
