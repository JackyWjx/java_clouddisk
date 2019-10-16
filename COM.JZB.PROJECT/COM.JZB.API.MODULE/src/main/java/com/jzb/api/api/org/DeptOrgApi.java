package com.jzb.api.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/5 15:42
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/dept")
public interface DeptOrgApi {
    /**
     * 根据用户姓名或手机号获取用户部门信息和在职状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptUser(@RequestBody Map<String, Object> param);

    /**
     * 批量导入用户到部门
     *
     * @param file
     * @param params
     * @return
     */
/*
    @RequestMapping(value = "/importUserInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response importUserInfo(@RequestBody MultipartFile file,@RequestBody Map<String, Object> params);
*/

    /**
     * 根据用户姓名或用户id获取用户部门信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUserDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserDept(@RequestBody Map<String, Object> param);
}
