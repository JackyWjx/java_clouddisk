package com.jzb.api.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:
 * @Author kuangbin
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/18 14:12
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/newCompanyCommon")
public interface NewCompanyCommonApi {
    /**
     * 创建公海单位没有负责人id时创建负责人账号并创建公海单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/addCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommonList(@RequestBody Map<String, Object> param);

    /**
     * 创建公海单位没有负责人id时创建负责人账号并创建公海单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/modifyCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyCommonList(@RequestBody Map<String, Object> param);
}
