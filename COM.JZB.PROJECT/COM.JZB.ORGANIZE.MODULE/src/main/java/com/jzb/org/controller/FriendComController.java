package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.DeptService;
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

    @Autowired
    private DeptService deptService;

    /**
     * 通过负责人或者单位名称查伙伴单位列表数据
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/24 16:07
     */
    @RequestMapping(value = "/searchCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCompanyFriend(@RequestBody Map<String, Object> param) {
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


    /**
     * 获取邀请人信息部门信息加入部门
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/modifyUserDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyUserDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", userInfo.get("uid"));
            param.put("cname", userInfo.get("cname"));
            param.put("relphone", userInfo.get("relphone"));
            //第一步，获取企业和部门id
            List<Map<String, Object>> comDeptList = friendComService.getInviteCD(param);
            //第二步，加入部门,修改邀请表状态
            int size = comDeptList.size();
            for (int i = 0; i < size; i++) {
                Map<String, Object> comDeptMap = comDeptList.get(i);
                deptService.addDeptUser(comDeptMap);
                comDeptMap.put("status", 10);
                comDeptMap.put("time", System.currentTimeMillis());
                friendComService.updateInvite(comDeptMap);
            }
            result = Response.getResponseSuccess(userInfo);

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
