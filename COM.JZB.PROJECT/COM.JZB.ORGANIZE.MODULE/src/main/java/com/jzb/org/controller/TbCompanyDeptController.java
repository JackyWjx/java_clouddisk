package com.jzb.org.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/CompanyDept")
public class TbCompanyDeptController {

    @Autowired
    private TbCompanyDeptService tbCompanyDeptService;
    /**
     * 根据用户id 查找部门负责人
     * @param param
     * @return
     */
    @RequestMapping(value = "/getDeptUser")
    @CrossOrigin
    public Response getDeptUser(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            //获取用户信息  设置用户信息
            param.put("uid", userInfo.get("uid"));
               List<Map<String,Object>> list = tbCompanyDeptService.getDeptUser(param);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
