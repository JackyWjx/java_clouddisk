package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description: 获取销售统计分析的数据
 * @Author Han Bin
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/CompanyList")
public interface TbCompanyListApi {
    /**
     * 销售统计分析的查询
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryCompanyList", method = RequestMethod.POST)
    @CrossOrigin
    Response queryCompanyList(@RequestBody Map<String, Object> param);

}