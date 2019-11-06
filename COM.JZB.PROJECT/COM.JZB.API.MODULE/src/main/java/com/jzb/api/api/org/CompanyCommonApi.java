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
@RequestMapping(value = "/org/companyCommon")
public interface CompanyCommonApi {
    /**
     * CRM-所有业主-业主列表-修改
     * 点击修改判断是否是系统中的用户如果不是就新建用户
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @PostMapping("/modifyCompanyCommon")
    @CrossOrigin
    public Response modifyCompanyCommon(@RequestBody Map<String, Object> param);

}
