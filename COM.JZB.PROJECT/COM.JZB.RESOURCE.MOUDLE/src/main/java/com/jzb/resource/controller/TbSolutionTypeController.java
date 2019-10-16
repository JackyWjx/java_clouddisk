package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
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
}
