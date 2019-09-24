package com.jzb.auth.api.organize;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/9 10:42
 */
@FeignClient(name = "jzb-org")
@RequestMapping("org")
public interface CompanyApi {
    /**
     * 根据单位名称获取相似单位名称
     *
     * @param param
     * @return
     */
    @PostMapping("/getEnterpriseNames")
    @CrossOrigin
    Response getEnterpriseNames(@RequestBody Map<String, Object> param);

}
