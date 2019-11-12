package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbSolutionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 方案类型
 */
@RestController
@RequestMapping(value = "/solutionType")
public class TbSolutionTypeController {

    @Autowired
    private TbSolutionTypeService tbSolutionTypeService;

    /**
     * 获取方案类型
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSolutionType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 查询结果
            List<Map<String, Object>> list = tbSolutionTypeService.getSolutionType(param);

            // 定义返回pageinfo
            PageInfo pi = new PageInfo();
            pi.setList(list);

            // 定义返回结果
            result = Response.getResponseSuccess();

            //设置返回pageinfo
            result.setPageInfo(pi);

        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-运营管理-解决方案-文件列表-分类2
     * 点击新增按钮新建文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/addSolutionType", method = RequestMethod.POST)
    @CrossOrigin
    public Response addSolutionType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 加入新建的活动分类
            int count = tbSolutionTypeService.addSolutionType(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-运营管理-解决方案-文件列表-分类3
     * 点击修改按钮进行文章分类修改
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/modifySolutionType", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifySolutionType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 加入新建的活动分类
            int count = tbSolutionTypeService.modifySolutionType(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-运营管理-解决方案-文件列表-分类1
     * 点击新建显示文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getSolutionTypeData", method = RequestMethod.POST)
    @CrossOrigin
    public Response getSolutionTypeData(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = tbSolutionTypeService.getSolutionTypeDataCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbSolutionTypeService.getSolutionTypeData(param);
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
     * CRM-运营管理-解决方案-文件列表-分类4
     * 点击新建显示文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/removeSolutionType", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeSolutionType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 删除分类
            int count = tbSolutionTypeService.removeSolutionType(param);
            if (count == 1) {
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
                if (count == 4){
                    result.setResponseEntity("此分类下存在数据,无法删除!");
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
