package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.TbCompanySupplierService;
import com.jzb.org.util.HttpConnectionURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    /**
     * CRM-销售业主-单位-供应商
     * 一键认证
     *
     * @author chenhui
     */
    @RequestMapping(value = "/authCompanySupplier", method = RequestMethod.POST)
    @CrossOrigin
    public Response authCompanySupplier(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = Response.getResponseError();
            //获取认证公司名字
            if (!JzbTools.isEmpty(param.get("cname"))){
                // 调用认证接口获取供应商公司信息
                String url = "http://192.168.0.20:8811/test_1.0";
                String name = JzbDataType.getString(param.get("cname"));
                String s = HttpConnectionURL.post(url, name);
                Map<String, Object> parse  =(Map<String, Object>) JSON.parse(s);
                 if(parse != null && parse.get("code").equals("200")) {
                     result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                     result.setResponseEntity(parse);
                 }
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 单位认证-营业执照//todo
     * 一键认证
     *
     * @author chenhui
     */
    @RequestMapping(value = "/authCompanyByFile", method = RequestMethod.POST)
    @CrossOrigin
    public Response authCompanyByFile(@RequestParam(value = "file") MultipartFile  image) {
        Response result;
        try {
            result = Response.getResponseError();

            if (!JzbTools.isEmpty(image)){
                //
                String url = "http://192.168.0.20:8811/businesspic";
                String path = ResourceUtils.getURL("classpath:").getPath();
                String fileFullName = path+"static/excel/"+image.getOriginalFilename();
                File resourceInfoFile = new File(fileFullName);
                if(resourceInfoFile.exists()){
                    resourceInfoFile.delete();
                }
                resourceInfoFile.getParentFile().mkdirs();
                image.transferTo(resourceInfoFile);

                Map<String,String> fileMap =new HashMap<>();
                fileMap.put("file",fileFullName);

                String s = HttpConnectionURL.formUpload(url, null, fileMap, "");
                resourceInfoFile.delete();
                Map<String, Object> parse  =(Map<String, Object>) JSON.parse(s);
                if(parse != null && parse.get("code").equals("200")) {
                    result = Response.getResponseSuccess();
                    result.setResponseEntity(parse);
                }
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 单位认证-身份证认证//
     * 一键认证
     *
     * @author chenhui
     */
    @RequestMapping(value = "/authIDCardByFile", method = RequestMethod.POST)
    @CrossOrigin
    public Response authIDCardByFile(@RequestParam(value = "front") MultipartFile  front,@RequestParam(value = "back") MultipartFile  back) {
        Response result;
        try {
            result = Response.getResponseError();
            //
            if (!JzbTools.isEmpty(front) && !JzbTools.isEmpty(back)){

                String url = "http://192.168.0.20:8811/idcard";
                String path = ResourceUtils.getURL("classpath:").getPath();
                // 查询身份证正面信息（人脸）
                String fileFullName = path+"static/excel/"+front.getOriginalFilename();
                File resourceInfoFile = new File(fileFullName);
                if(resourceInfoFile.exists()){
                    resourceInfoFile.delete();
                }
                resourceInfoFile.getParentFile().mkdirs();
                front.transferTo(resourceInfoFile);
                Map<String,String> fileMap =new HashMap<>();
                Map<String,String> textMap =new HashMap<>();
                textMap.put("side","front");
                fileMap.put("file",fileFullName);
                String sFront = HttpConnectionURL.formUpload(url, textMap, fileMap, "");
                resourceInfoFile.delete();
                Map<String, Object> frontMap  =(Map<String, Object>) JSON.parse(sFront);

                // 身份证反面
                String backFileFullName = path+"static/excel/"+back.getOriginalFilename();
                File backResourceInfoFile = new File(backFileFullName);
                if(backResourceInfoFile.exists()){
                    backResourceInfoFile.delete();
                }
                resourceInfoFile.getParentFile().mkdirs();
                back.transferTo(backResourceInfoFile);
                Map<String,String> backFileMap =new HashMap<>();
                textMap.put("side","back");
                backFileMap.put("file",backFileFullName);
                String bFront = HttpConnectionURL.formUpload(url, textMap, backFileMap, "");
                resourceInfoFile.delete();
                Map<String, Object> backMap  =(Map<String, Object>) JSON.parse(bFront);
                if(!JzbTools.isEmpty(frontMap) && frontMap.get("code").equals("200") && !JzbTools.isEmpty(backMap) && backMap.get("code").equals("200")) {
                    Map<String,Object> m = new HashMap<>();
                    m.put("front",frontMap);
                    m.put("back",backMap);
                    result = Response.getResponseSuccess();
                    result.setResponseEntity(m);
                }

            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}