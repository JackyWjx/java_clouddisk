package com.jzb.activity.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/21
 * @修改人和其它信息
 */
@FeignClient(name = "jzb-org")
@RequestMapping("/org/dept")
public interface OrgCompanyDpt {

    /**
     * 根据企业id获取部门信息
     *
     * @author chenhui
     * param projectid
     */
    @RequestMapping(value = "/getDeptList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptList(@RequestBody Map<String, Object> param);
}
