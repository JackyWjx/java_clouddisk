package com.jzb.auth.api.organize;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/9 10:42
 */
@FeignClient(name = "jzb-org")
@RequestMapping("org/company")
public interface CompanyUserApi {
    /**
     * 根据用户ID查询企业中是否存在用户
     *
     * @param param
     * @return
     */
    @PostMapping("/getDeptCount")
    @CrossOrigin
    Response getDeptCount(@RequestBody Map<String, Object> param);

    /**
     * 将用户加入单位资源池中
     *
     * @param param
     * @return
     */
    @PostMapping("/addCompanyDept")
    @CrossOrigin
    Response addCompanyDept(@RequestBody Map<String, Object> param);
}
