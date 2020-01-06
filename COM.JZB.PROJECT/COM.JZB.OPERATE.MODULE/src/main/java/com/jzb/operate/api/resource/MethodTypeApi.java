package com.jzb.operate.api.resource;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "jzb-resource")
@RequestMapping(value = "/methodType")
@Repository
public interface MethodTypeApi {
    /**
     * @param param
     * @return
     * @deprecated 获取方法论类别排除已勾选的 类别 单位 项目
     */
    @RequestMapping(value = "/getMethodTypedels", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMethodTypedels(@RequestBody Map<String, Object> param);
}
