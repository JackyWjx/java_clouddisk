package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbStandardDocumentService;
import com.jzb.resource.util.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 运营管理中的内容列表
 */
@RestController
@RequestMapping(value = "/resource/standardDocument")
public class TbStandardDocumentController {

    @Autowired
    private TbStandardDocumentService tbStandardDocumentService;

    /**
     * 根据菜单类别进行查询  如果没有菜单类别则进行所有的查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbStandardDocument", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbStandardDocument(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断分页参数是否为空
            if (JzbCheckParam.haveEmpty(param, new String[]{"rows", "page"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);

                List<Map<String, Object>> list = tbStandardDocumentService.getTbStandardDocument(param);

                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);

                //总数
                int count = list.size();
                pageInfo.setTotal(count);
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 运营管理中标准中内容列表的新建
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbStandardDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbStandardDom(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断如果指定参数有为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "price", "department", "effective", "stdrange", "pagekey", "pagedesc"})) {
                result = Response.getResponseError();
            } else {
                //添加一条模板记录
                int count = tbStandardDocumentService.saveTbStandardDom(param);

                //如果返回值大于0表示添加成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.添加失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 运营管理中标准中内容列表的修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTbStandardDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateTbStandardDom(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断如果指定参数有为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "price", "department", "effective", "stdrange", "pagekey", "pagedesc"})) {
                result = Response.getResponseError();
            } else {
                //修改一条模板记录
                int count = tbStandardDocumentService.updateTbStandardDom(param);

                //如果返回值大于0表示修改成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            JzbTools.logError(e);
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
    public Response updateDelete(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"domid"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录
                int count = tbStandardDocumentService.updateDelete(param);
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
     * 根据名称进行模糊查询
     *
     * @param param
     * @return
     */

    @RequestMapping(value = "/getCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCname(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断分页参数是否为空
            if (JzbCheckParam.haveEmpty(param, new String[]{"rows", "page"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);
                List<Map<String, Object>> list = tbStandardDocumentService.getCname(param);
                // 遍历转格式
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("effective", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("effective")), JzbDateStr.yyyy_MM_dd));
                }

                // 定义返回pageinfo
                PageInfo pageInfo = new PageInfo<>();
                pageInfo.setList(list);

                //总数
                int count = list.size();
                pageInfo.setTotal(count);

                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);

                // 设置返回pageinfo
                result.setPageInfo(pageInfo);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}

