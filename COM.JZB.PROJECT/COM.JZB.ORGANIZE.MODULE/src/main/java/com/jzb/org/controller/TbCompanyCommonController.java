package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.TbCompanyCommonService;
import com.jzb.org.util.SetPageSize;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
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


    /**
     * 销售业主-所有业主-业主列表查询
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyCommon",method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                //设置好分页参数
                SetPageSize setPageSize = new SetPageSize();
                  param = setPageSize.setPagenoSize(param);
                //判断前端传过来的分页总数
                int count = JzbDataType.getInteger(param.get("count"));
                // 获取业主列表的总数
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    count = tbCompanyCommonService.getCount(param);
                }
                //查询 业主列表的所有数据
                  List<Map<String,Object>> list = tbCompanyCommonService.getCompanyCommon(param);
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                //设置分页总数
                pageInfo.setTotal(count > 0 ? count : list.size());
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-业主列表-新建
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //新建数据
            int count = tbCompanyCommonService.saveCompanyCommon(param);
            //获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
    /**
     * 所有业主-业主列表-修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
            //修改数据
            int count = tbCompanyCommonService.updateCompanyCommon(param);
            //获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-业主列表-删除
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteCompanyCommon",method = RequestMethod.POST)
    @CrossOrigin
    public Response deleteCompanyCommon(@RequestBody Map<String,Object> param) {
        Response result;
        try {

                List<Map<String, Object>> paramList = (List) param.get("list");
                int count = tbCompanyCommonService.deleteCompanyCommon(paramList);
                //如果返回值大于0，则返回成功信息
                if (count > 0) {
                    //获取用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //否则返回失败信息
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
     * 所有业主-业主列表-分配业务员
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCompanys", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCompanys(@RequestBody Map<String, Object> param) {

        Response result;
        try {
            //如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                //根据id进行修改，添加业务员
                int count = tbCompanyCommonService.updateCompanys(param);
                //如果返回值大于零则响应成功信息
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
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
}
