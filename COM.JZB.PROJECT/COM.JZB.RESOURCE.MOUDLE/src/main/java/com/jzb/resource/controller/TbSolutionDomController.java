package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.resource.service.TbSolutionDomService;
import com.jzb.resource.util.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 获取方案文档
 */
@RestController
@RequestMapping(value = "/solutionDom")
public class TbSolutionDomController {

    @Autowired
    private TbSolutionDomService tbSolutionDomService;

    /**
     * 根据方案类别查询方案文档 param传参  有typeid查改typeid下方案文档，无则查所有
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSolutionDom", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionDom(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 如果参数typeid为"" 则不进mapper.xml
            if (JzbCheckParam.haveEmpty(param, new String[]{"rows", "page"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);

                List<Map<String, Object>> list = tbSolutionDomService.getSolutionDom(param);

                PageInfo pi = new PageInfo();
                pi.setList(list);

                int count = tbSolutionDomService.queryCount(param);
                pi.setTotal(count);

                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 模糊查询方案文档列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSolutionDomCname", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionDomCname(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 判断如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"rows", "page"})) {
                result = Response.getResponseError();
            } else {
                // 查询结果
                List<Map<String, Object>> list = tbSolutionDomService.getSolutionDomCname(param);

                // 查询总数
                int count = tbSolutionDomService.queryCount(param);

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setTotal(count);
                pi.setList(list);

                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询单篇文档详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSolutionDomByDomid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionDomByDomid(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"domid"})) {
                result = Response.getResponseError();
            } else {
                // 查询结果
                List<Map<String, Object>> list = tbSolutionDomService.queryDomByDomid(param);

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }


}
