package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/***
 *  访问单位模块查询数据
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/CompanyProject")
@Repository
public interface TbCompanyProjectApi {
    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceProjectList", method = RequestMethod.POST)
    public Response getServiceProjectList(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-2
     * 根据服务的项目ID获取模糊搜索项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/searchCompanyServiceList", method = RequestMethod.POST)
    public Response searchCompanyServiceList(Map<String, Object> param);
}
