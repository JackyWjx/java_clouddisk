package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
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
     *模糊查询 单位 项目名称
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompany", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompany(@RequestBody Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用c
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceProjectList", method = RequestMethod.POST)
    public Response getServiceProjectList(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-2
     * 获取所有人的uid
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceProjectUid", method = RequestMethod.POST)
    public Response getServiceProjectUid(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用c
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceByProjectid", method = RequestMethod.POST)
    public Response getServiceByProjectid(Map<String, Object> param);

    /**
     *
     * 根据服务的项目ID获取项目信息
     *
     * @Author: chenhui
     * @DateTime: 2019/12/29
     */
    @RequestMapping(value = "/getServiceProjectInfoByProjectid", method = RequestMethod.POST)
    public Response getServiceProjectInfoByProjectid(Map<String, Object> param);


    /**
     *
     * 根据项目名称获取项目id
     *
     * @Author: chenhui
     * @DateTime: 2019/12/29 getProjectByCname
     */
    @RequestMapping(value = "/getProjectidByname", method = RequestMethod.POST)
    public Response getProjectidByname(Map<String, Object> param);

    /**
     *
     * 根据单位名称获取项目id
     *
     * @Author: chenhui
     * @DateTime: 2019/12/29
     */
    @RequestMapping(value = "/getProjectByCname", method = RequestMethod.POST)
    public Response getProjectByCname(Map<String, Object> param);


    /**
     *
     * 根据销售员名称获取项目id
     *
     * @Author: chenhui
     * @DateTime: 2019/12/29
     */
    @RequestMapping(value = "/getProjectByUname", method = RequestMethod.POST)
    public Response getProjectByUname(Map<String, Object> param);


    /**
     *
     * 根据部门id获取项目id
     *
     * @Author: chenhui
     * @DateTime: 2019/12/29
     */
    @RequestMapping(value = "/getProjectByCdid", method = RequestMethod.POST)
    public Response getProjectByCdid(Map<String, Object> param);


}
