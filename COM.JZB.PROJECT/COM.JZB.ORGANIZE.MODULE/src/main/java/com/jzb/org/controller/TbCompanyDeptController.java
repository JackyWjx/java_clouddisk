package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.service.TbCompanyDeptService;
import com.jzb.org.util.SetPageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/CompanyDept")
public class TbCompanyDeptController {

    @Autowired
    private TbCompanyDeptService tbCompanyDeptService;

    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    /**
     * 根据用户id 查找部门负责人
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getDeptUser")
    @CrossOrigin
    public Response getDeptUser(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                SetPageSize setPageSize = new SetPageSize();
                setPageSize.setPagenoSize(param);
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                //获取用户信息  设置用户信息
                param.put("uid", userInfo.get("uid"));
                //获取前端传过来的分页参数
                int count = JzbDataType.getInteger(param.get("count"));

                // 获取招标类型的总数
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    // 查询所有符合条件的总数
                    count = tbCompanyDeptService.getDeptUserCount(param);
                }
                List<Map<String, Object>> list = tbCompanyDeptService.getDeptUser(param);
                // 遍历获取地区调用redis返回
                for (int i = 0, l = list.size(); i < l; i++) {
                    Map<String, Object> map = new HashMap<>();
                    //根据地区id从缓存中获取地区的信息
                    Response regionInfo = regionBaseApi.getRegionInfo(list.get(i));
                    // 转map
                    if (regionInfo != null) {
                        list.get(i).put("region", regionInfo.getResponseEntity());
                    }
                    map.put("uid", list.get(i).get("manager"));
                    Response cacheUserInfo12 =  userRedisServiceApi.getCacheUserInfo(map);

                    Map<String,Object> cacheUserInfo1 = (Map<String, Object>) cacheUserInfo12.getResponseEntity();

                    if (cacheUserInfo1 != null) {
                        list.get(i).put("username", cacheUserInfo1.get("cname"));
                    }
                }
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(count);
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
