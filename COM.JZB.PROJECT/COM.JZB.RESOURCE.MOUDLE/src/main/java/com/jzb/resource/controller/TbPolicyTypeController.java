package com.jzb.resource.controller;

import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbPolicyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 获取政策类型
 */
@RestController
@RequestMapping(value = "/policyType")
public class TbPolicyTypeController {


    @Autowired
    private TbPolicyTypeService tbPolicyTypeService;

    /**
     * 查询政策类型（父子级）
     * @param params
     * @return
     */
    @RequestMapping(value = "/getPolicyType",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyType(@RequestBody(required = false) Map<String ,Object> params){
        Response result;
        try {

            // 获取返回jsonArray
            JSONArray objects = tbPolicyTypeService.queryPolicyType(params);

            // 定义返回结果
            result = Response.getResponseSuccess();
            result.setResponseEntity(objects);

        }catch (Exception e){

            // 打印异常信息
            e.printStackTrace();
            result=Response.getResponseError();

        }
        return result;
    }


    /**
     * LBQ
     * 查询运营管理下政策中的菜单类别
     *
     * @param
     * @return
     */

    @RequestMapping(value = "/getPolicyTypes",method = RequestMethod.POST)
    @CrossOrigin
    public Response getPolicyTypes(@RequestBody Map<String,Object> param) {
        Response result;
        try {
            //查询运营管理中的菜单类别
            List<Map<String, Object>> list = tbPolicyTypeService.getPolicyTypes(param);

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


    /** LBQ
     * 运营管理下政策模块中的菜单类别新增
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/savePolicyType", method = RequestMethod.POST)
    @CrossOrigin
    public Response savePolicyType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname","adduid"})) {
                result = Response.getResponseError();
            } else {
                //添加一条记录
                int count = tbPolicyTypeService.savePolicyType(param);
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



    /**LBQ
     * 运营管理下政策菜单列表的修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updatePolicyType", method = RequestMethod.POST)
    @CrossOrigin
    public Response updatePolicyType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "cname"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录

                int count = tbPolicyTypeService.updatePolicyType(param);
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

    /**LBQ
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
                int count = tbPolicyTypeService.updateStatus(param);
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

}
