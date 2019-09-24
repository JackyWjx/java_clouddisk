package com.jzb.auth.controller;

import com.jzb.auth.service.CompanyListService;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/***
 *
 *@Author
 *@Data 2019/7/26   11:06
 *@Describe 企业控制层
 *
 */
@RestController
@RequestMapping("/company/list")
public class CompanyController {
    @Autowired
    CompanyListService service;

    /**
     * @param
     * @return
     * @date 2019/7/26 13:22
     * @describe 创建单位
     */
    @PostMapping("/saveCompany")
    @ResponseBody
    @Transactional
    public Response saveCompanyList(@RequestBody Map<String, Object> map, @RequestHeader("token") String token) {
        Response result;
        try {
            if ("jzbtoken".equals(token)) {
                //创建一条企业信息
                int countlist = service.saveCompanyList(map);
                //创建一条企业详细信息
                int countinfo = service.saveCompanyInfo(map);
                if(countlist > 0 ){
                    result = countinfo > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                }else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param
     * @return
     * @date 2019/7/26 13:22
     * @describe 加入单位
     */
    @PostMapping("/addCompany")
    @ResponseBody
    public Response addCompany(@RequestBody Map<String,Object> map,@RequestHeader("token") String token){
        Response result ;
        try {
            if ("jzbtoken".equals(token)) {
                //添加一条申请记录
                int count = service.saveInviteUser(map);
                result = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            } else {
                result =  Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }




}
