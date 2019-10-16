package com.jzb.api.controller.org;

import com.jzb.api.service.FriendComService;
import com.jzb.api.util.ApiToken;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/29 17:09
 */
@RestController
@RequestMapping(value = "/api/org/fri")
public class FriendComController {

    @Autowired
    private ApiToken apiToken;
    @Autowired
    private FriendComService friendComService;

    /**
     * 伙伴单位列表（拼接接口）
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/29 17:13
     */
    @RequestMapping(value = "/searchCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCompanyFriend(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"pagesize", "pageno"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                    if (userInfo.size() > 0) {
                        param.put("userinfo", userInfo);
                        result = friendComService.searchCompanyFriend(param);
                    } else {
                        result = Response.getResponseError();
                    }

                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
