package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description: 获取企业部门信息
 * @Author Han Bin
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/dept")
public interface OrgDeptApi {

    /**
     * 根据企业id获取部门信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getDeptList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptList(@RequestBody Map<String, Object> param);

    /**
     * 获取部门下所有子级的用户包括自身
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/4 14:36
     */
    @RequestMapping(value = "/getDeptUserChildList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptUserChildList(@RequestBody Map<String, Object> param);

}
