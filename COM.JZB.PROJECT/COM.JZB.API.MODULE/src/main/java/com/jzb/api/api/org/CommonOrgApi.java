package com.jzb.api.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time 2020/1/3
 * @other
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/orgCommon")
public interface CommonOrgApi {
    /**
     * @Author Reed
     * @Description 创建公海单位时 给负责人创建公海用户
     * @Date 10:58 2020/1/3
     * @Param [param]
     * @return com.jzb.base.message.Response
    **/
    @RequestMapping(value = "/addCommonUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCommonUser(@RequestBody Map<String, Object> param);
}
