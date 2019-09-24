package com.jzb.api.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/18 18:12
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org")
public interface CompanyOrgApi {
    /**
     * 创建单位 & 加入单位
     * 参数type为1创建单位，2加入单位
     *
     * @param param
     * @return
     */
    @PostMapping("/addCompany")
    @CrossOrigin
    public Response addCompany(@RequestBody Map<String, Object> param);

    /**
     * 根据用户id获取该用户加入的单位和创建的单位名称
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/12 17:45
     */
    @RequestMapping(value = "/getCompanyByUid", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyByUid(@RequestBody Map<String, Object> param);

    /**
     * 根据企业ID获取当前企业的超级管理员
     *
     * @param param
     * @return Response
     * @author kuangbin
     */
    @RequestMapping(value = "/getAdministrator", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAdministrator(@RequestBody Map<String, Object> param);

    /**
     * 管理员创建伙伴单位(合伙单位表)
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 18:00
     */
    @RequestMapping(value = "/addCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyFriend(@RequestBody Map<String, Object> param);
}
