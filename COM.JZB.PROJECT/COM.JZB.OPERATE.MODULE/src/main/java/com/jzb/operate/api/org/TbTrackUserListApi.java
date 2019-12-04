package com.jzb.operate.api.org;
/**
 * @Author sapientia
 * @Date 2019/12/3 16:21
 */

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/trackUserList")
public interface TbTrackUserListApi {

    /**
     * 根据获取跟进记录
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTrackUserList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTrackUserList(@RequestBody Map<String, Object> param);

    /**
     * 根据获取跟进记录
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryTrackUserByName", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTrackUserByName(@RequestBody Map<String, Object> param);
}
