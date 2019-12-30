package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/cockpit")
public interface CockpitApi {


        /**
         * 根据部门id获取部门下所有用户id
         *
         * @param param
         * @return
         */
        @RequestMapping(value = "/getAllDeptUser", method = RequestMethod.POST)
        @CrossOrigin
        public Response getDeptUser(@RequestBody Map<String, Object> param);


}
