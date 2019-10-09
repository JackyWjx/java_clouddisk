package com.jzb.resource.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbStandardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 运营管理中的标准菜单分类
 */
@RestController
@RequestMapping(value = "/resource/standardType")
public class TbStandardTypeController {

    @Autowired
    private TbStandardTypeService tbStandardTypeService;

    /**
     * 标准分类中的新建
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveStandardType", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveStandardType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "cname", "adduid"})) {
                result = Response.getResponseError();
            } else {
                //添加一条记录
                int count = tbStandardTypeService.saveStandardType(param);
                //如果返回值大于0，添加成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.添加失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 运营管理下菜单分类的修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateStandardType", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateStandardType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "cname", "upduid"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录
                int count = tbStandardTypeService.updateStandardType(param);
                //如果返回值大于0，修改成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateStatus(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录
                int count = tbStandardTypeService.updateStatus(param);
                //如果返回值大于0，修改成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询运营管理中的菜单类别
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getStandardType",method = RequestMethod.POST)
    @CrossOrigin
    public Response getStandardType(@RequestBody Map<String,Object> param) {
        Response result;
        try {
            //查询运营管理中的菜单类别
            List<Map<String, Object>> list = tbStandardTypeService.getStandardType(param);

            // 遍历转格式
            for (int i = 0, l = list.size(); i < l; i++) {
                list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
            }

            // 定义返回pageinfo
            PageInfo pageInfo = new PageInfo<>();
            pageInfo.setList(list);

            // 定义返回结果
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);

            // 设置返回pageinfo
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;

    }
}
