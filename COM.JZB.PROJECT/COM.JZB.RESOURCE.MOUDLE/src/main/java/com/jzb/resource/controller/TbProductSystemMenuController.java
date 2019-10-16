package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbProductSystemMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/resource/systemMenu")
public class TbProductSystemMenuController {

    @Autowired
    private TbProductSystemMenuService tbProductSystemMenuService;

    /**
     * 获取menu
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductSystemMenuList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getProductSystemMenuList(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = tbProductSystemMenuService.queryMenuList(param);
            result = Response.getResponseSuccess();
            PageInfo pi = new PageInfo();
            pi.setList(list);
            pi.setTotal(list.size());
            result.setPageInfo(pi);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取menu
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addProductSystemMenuList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addProductSystemMenuList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"menuno", "menuname","uid"})) {
                result = Response.getResponseError();
            }else {
                result = tbProductSystemMenuService.addMenuList(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取menu
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/editProductSystemMenuList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response editProductSystemMenuList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = tbProductSystemMenuService.updateMenuList(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
