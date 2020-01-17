package com.jzb.auth.api.organize;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Description:
 * @Author czd
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/9 10:42
 */
@FeignClient(name = "jzb-org")
@RequestMapping("/org/CompanyList")
@Repository
public interface CompanyListApi {

    /**
     * 根据用户ID查询企业中是否存在用户
     *
     * @param param
     * @return
     */
    @PostMapping("/getManagerByCid")
    @CrossOrigin
    Response getManagerByCid(@RequestBody Map<String, Object> param);

}
