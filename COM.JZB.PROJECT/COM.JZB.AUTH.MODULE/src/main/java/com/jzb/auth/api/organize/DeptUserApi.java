package com.jzb.auth.api.organize;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-org")
@RequestMapping("/org/dept")
@Repository
public interface DeptUserApi {

    /**
     * update company
     * @param param
     * @return
     */
    @RequestMapping(value = "/modifyDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyDeptUser(@RequestBody Map<String, Object> param);
}
