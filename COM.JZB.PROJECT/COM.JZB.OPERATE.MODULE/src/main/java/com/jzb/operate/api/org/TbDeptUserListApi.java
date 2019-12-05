package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/5 16:56
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/dept/user")
public interface TbDeptUserListApi {

    /**
     * @Author sapientia
     * @Date 16:57 2019/12/5
     * @Description 根据部门id查询部门下的人员
     **/
    @RequestMapping(value = "/queryUsernameBydept", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryUsernameBydept(@RequestBody Map<String, Object> param);
}
