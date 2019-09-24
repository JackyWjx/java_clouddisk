package com.jzb.api.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/5 15:32
 */
@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth/role")
public interface RoleAuthApi {

    /**
     * 根据用户部门id和用户id获取用户角色组信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUserDeptGroupList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserDeptGroupList(@RequestBody Map<String, Object> param);

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
    /**
     * 保存角色组关联信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addRoleRelation", method = RequestMethod.POST)
    @CrossOrigin
    public Response addRoleRelation(@RequestBody Map<String, Object> param);
}
