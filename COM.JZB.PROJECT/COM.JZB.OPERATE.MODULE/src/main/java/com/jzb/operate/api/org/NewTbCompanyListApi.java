package com.jzb.operate.api.org;
import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name = "jzb-org")
@RequestMapping(value = "/company/project")
public interface NewTbCompanyListApi {

    /**
     * @Author sapientia
     * @Date  10:05
     * @Description 根据拜访公司id查询
     **/
    @PostMapping("/queryCompanyByid")
    @CrossOrigin
    public Response queryCompanyByid(@RequestBody Map<String, Object> map);

    /**
     * @Author sapientia
     * @Date  10:07
     * @Description 根据项目id修改项目
     **/
    @PostMapping("/updateCompanyProject")
    @CrossOrigin
    public Response updateCompanyProject(@RequestBody Map<String, Object> map);

    /**
     * @Author sapientia
     * @Date  10:07
     * @Description 根据项目id修改项目详情
     **/
    @PostMapping("/updateCompanyProjectInfo")
    @CrossOrigin
    public Response updateCompanyProjectInfo(@RequestBody Map<String, Object> map);

    /**
     * @Author sapientia
     * @Date  10:10
     * @Description 根据公司id修改公司信息
     **/
    @PostMapping("/updateCommonCompanyList")
    @CrossOrigin
    public Response updateCommonCompanyList(@RequestBody Map<String, Object> map);
}
