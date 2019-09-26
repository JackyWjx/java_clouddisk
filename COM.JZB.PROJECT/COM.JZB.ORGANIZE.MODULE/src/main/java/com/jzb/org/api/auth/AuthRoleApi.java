package com.jzb.org.api.auth;


import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth/role")
public interface AuthRoleApi {
    /**
     * 添加企业角色表信息（角色表）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanyRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyRole(@RequestBody Map<String, Object> param);

    /**
     * 批量添加用户角色
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addUserRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response addUserRole(@RequestBody Map<String, Object> param);

    /**
     * 保存角色组信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveRoleGroup", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveRoleGroup(@RequestBody Map<String, Object> param);
}
