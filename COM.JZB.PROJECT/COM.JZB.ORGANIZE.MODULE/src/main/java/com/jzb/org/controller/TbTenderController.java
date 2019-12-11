package com.jzb.org.controller;

import com.alibaba.fastjson.JSONObject;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.tender.Tender;
import com.jzb.org.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author  chenzhengduan
 */
@RestController
@RequestMapping(value = "/org/tender")
public class TbTenderController {

    @Autowired
    private Tender tenderApi;

    @Autowired
    private TenderService tenderService;

    /**
     * 获取招标信息（暂时禁用）
     *
     * @return
     */
//    @RequestMapping("/getTender")
    @ResponseBody
    @CrossOrigin
    public String getTender() {
        StringBuilder strTmpB = new StringBuilder();
        String strTmp = "";
        try {
            String filePath = "E:/tender/tender.txt";
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader buffReader = new BufferedReader(reader);
            while ((strTmp = buffReader.readLine()) != null) {
                strTmpB.append(strTmp).append("\r ");
            }
            buffReader.close();
            JSONObject object = JSONObject.parseObject(strTmpB.toString());
            List list = object.getJSONArray("data");
            List<Map<String, Object>> list1 = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JzbDataType.getString(strTmpB.toString());
    }

    /**
     * 获取招投标的省市
     *
     * @return
     */
    @RequestMapping(value = "/getProvice", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getProvice() {
        Response result;
        try {
            String provice = "{" +
                    "\"list\": [{" +
                    "\"id\": \"10000000\"," +
                    "\"name\": \"湖南\"" +
                    "}, {" +
                    "\"id\": \"20000000\"," +
                    "\"name\": \"湖北\"" +
                    "}]," +
                    "\"10000000\": [{" +
                    "\"id\": \"10010000\"," +
                    "\"name\": \"长沙\"" +
                    "}, {" +
                    "\"id\": \"10020000\"," +
                    "\"name\": \"娄底\"" +
                    "}]," +
                    "\"20000000\": [{" +
                    "\"id\": \"20010000\"," +
                    "\"name\": \"武汉\"" +
                    "}, {" +
                    "\"id\": \"20020000\"," +
                    "\"name\": \"黄石\"" +
                    "}]," +
                    "\"20010000\": [{" +
                    "\"id\": \"20010001\"," +
                    "\"name\": \"武汉区1\"" +
                    "}, {" +
                    "\"id\": \"20010002\"," +
                    "\"name\": \"武汉区2\"" +
                    "}]," +
                    "\"20020000\": [{" +
                    "\"id\": \"20020001\"," +
                    "\"name\": \"黄石区1\"" +
                    "}, {" +
                    "\"id\": \"20020002\"," +
                    "\"name\": \"黄石区2\"" +
                    "}]" +
                    "}";
            JSONObject object = JSONObject.parseObject(provice);
            result = Response.getResponseSuccess();
            result.setResponseEntity(object);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取招投标信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTenderList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderList(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            // 判断指定参数为空返回error
            if (JzbCheckParam.haveEmpty(param,new String[]{"pageno","pagesize"})) {

                result = Response.getResponseError();

            } else {
                //设置好分页参数
                JzbPageConvert.setPageRows(param);

                // 结果集
                List<Map<String, Object>> list = tenderService.getTenderList(param);

                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("opendate",JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("opendate")), JzbDateStr.yyyy_MM_dd));
                    list.get(i).put("addtime",JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }

                // 定义返回结果
                result = Response.getResponseSuccess();

                // 定义pageinfo
                PageInfo pi = new PageInfo();

                pi.setList(list);

                pi.setTotal(tenderService.getTenderCount(param));
                result.setPageInfo(pi);
            }
        } catch (Exception e) {

            JzbTools.logError(e);
            result = Response.getResponseError();

        }

        return result;

    }



}