package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.TbCompanyCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 已分配的业主
 */
@RestController
@RequestMapping(value = "/org/companyCommon")
public class TbCompanyCommonController {

    @Autowired
    private TbCompanyCommonService tbCompanyCommonService;

    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    /**
     * 获取已分配的业主单位（不带条件查询）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyCommonList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "count", "uid"})) {
                result = Response.getResponseError();
            } else {

                // 设置参数
                JzbPageConvert.setPageRows(param);

                // 获取list
                List<Map<String, Object>> list = tbCompanyCommonService.queryCompanyCommon(param);

                for (int i = 0, l = list.size(); i < l; i++) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("key", list.get(i).get("region"));
                    Response cityList = tbCityRedisApi.getCityList(param);

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
     * 获取已分配的业主单位 (带条件查询)
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyCommonListByKeyword", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyCommonListByKeyword(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "count"})) {
                result = Response.getResponseError();
            } else {

                // 设置参数
                JzbPageConvert.setPageRows(param);

                // 获取list
                List<Map<String, Object>> list = tbCompanyCommonService.queryCompanyCommonByKeyWord(param);

                // 遍历获取地区调用redis返回
                for (int i = 0, l = list.size(); i < l; i++) {

                    Map<String, Object> map = new HashMap<>();
                    // 获取地区id
                    map.put("key", list.get(i).get("region"));
                    Response cityList = tbCityRedisApi.getCityList(param);

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
     * 修改业主单位
     *
     * @return
     */
    @RequestMapping(value = "/updateCompanyByCid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateCompanyByCid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "cname", "region", "phone", "username"})) {
                result = Response.getResponseError();
            } else {
                // 如果返回结果大于0 就返回success
                result = tbCompanyCommonService.updateCompany(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            // 打印错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

}
