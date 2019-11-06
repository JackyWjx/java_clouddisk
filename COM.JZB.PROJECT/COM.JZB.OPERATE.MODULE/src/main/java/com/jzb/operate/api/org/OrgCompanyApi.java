package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description:
 * @Author Han Bin
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org")
public interface OrgCompanyApi {


    /**
     * 根据项目id获取项信息
     *
     * @author hanbin
     * param projectid
     */
    @RequestMapping(value = "/getCompanyProjct", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyProjct(@RequestBody Map<String, Object> param);

    /**
     * 主页获取单位信息
     *
     * @author kuangbin
     * para cid
     */
    @RequestMapping(value = "/getEnterpriseData", method = RequestMethod.POST)
    @CrossOrigin
    public Response getEnterpriseData(@RequestBody Map<String, Object> param);

    /**
     * 根据单位id获取单位信息
     *
     * @author hanbin
     */
    @RequestMapping(value = "/getCompany", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompany(@RequestBody Map<String, Object> param);

}
