package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.FriendComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 伙伴单位控制层
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/24 15:44
 */
@RestController
@RequestMapping("org/friend")
public class FriendComController {

    @Autowired
    private FriendComService friendComService;

    /**
     * 通过负责人或者单位名称查伙伴单位列表数据
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/24 16:07
     */
    @RequestMapping(value = "/modifyCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyFriend(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo pageInfo;
        try {
            String[] str = {"pagesize", "pageno"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    param.put("pagesize", rows);
                    param.put("start", rows * (page - 1));
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    List<Map<String, Object>> deptList = friendComService.searchFriendComByValue(param);
                    result = Response.getResponseSuccess(userInfo);
                    pageInfo = new PageInfo();
                    pageInfo.setList(deptList);
                    int count = JzbDataType.getInteger(param.get("count"));
                    if (count == 0) {
                        int size = friendComService.searchFriendComByValueCount(param);
                        pageInfo.setTotal(size > 0 ? size : deptList.size());
                    }
                    result.setPageInfo(pageInfo);
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
