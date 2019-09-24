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
@RequestMapping(value = "/org/company")
public interface CompanyUserApi {
    /**
     * 创建单位 & 加入单位
     * 参数type为1创建单位，2加入单位
     *
     * @param param
     * @return
     */
    @PostMapping("/sendRemind")
    @CrossOrigin
    public Response sendRemind(@RequestBody Map<String, Object> param);

}
