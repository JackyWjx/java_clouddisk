package com.jzb.api.service;

import com.jzb.api.api.auth.RoleAuthApi;
import com.jzb.api.api.org.CompanyOrgApi;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.message.ServerResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/18 18:30
 */
@Service
public class CompanyService {
    @Autowired
    private CompanyOrgApi companyOrgApi;

    @Autowired
    private RoleAuthApi roleAuthApi;

    /**
     * 创建单位 & 加入单位
     * 参数type为1创建单位，2加入单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/19 9:08
     */
    public Response addCompany(Map<String, Object> param) {
        Response result;
        int type = Integer.parseInt(param.get("type").toString());
        //type的种类
        int found = 1;
        result = companyOrgApi.addCompany(param);
        if (type == found) {
            //创建单位类型，创建默认角色
            Map<String, Object> cidMap = (Map<String, Object>) result.getResponseEntity();
            //创建默认角色
            String cid = JzbDataType.getString(cidMap.get("cid"));
            String crId = cid + "000000";
            param.put("cid", cid);
            param.put("crid", crId);
            param.put("cname", "默认角色");
            param.put("status", "1");
            param.put("addtime", System.currentTimeMillis());
            roleAuthApi.addCompanyRole(param);
            //将用户加入默认角色中
            List<Map<String, Object>> list = new ArrayList<>(1);
            Map<String, Object> uidMap = new HashMap<>(2);
            uidMap.put("uid", param.get("uid"));
            list.add(uidMap);
            param.put("list", list);
            roleAuthApi.addUserRole(param);
            //创建默认角色组，资源池角色组合管理员角色组
            saveInitRoleGroup(param);

        }
        return result;
    }

    private void saveInitRoleGroup(Map<String, Object> param){
        //创建默认角色组，资源池角色组合管理员角色组
        String cid = JzbDataType.getString(param.get("cid"));
        param.put("cname", "资源池角色组");
        param.put("crgid",cid+"0000");
        roleAuthApi.saveRoleGroup(param);
        param.put("cname", "管理员角色组");
        param.put("crgid",cid+"1111");
        roleAuthApi.saveRoleGroup(param);
    }
}
