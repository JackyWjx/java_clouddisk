package com.jzb.operate.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;


/**
 *  @author: gongWei
 *  @Date: Created in 2019/12/11 9:09
 *  @Description:
 */
@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/userInfo")
public interface UserInfoApi {

    /**
     *  @author: gongWei
     *  @Date:  2019/12/11 9:09
     *  @Description:
     *  @Param
     *  @Return
     *  @Exception
     */
    @RequestMapping(value = "/getUsernameList", method = RequestMethod.POST)
    @CrossOrigin
    Response searchInvitee(@RequestBody Map<String, Object> param);
}
