package com.jzb.api.service;

import com.jzb.api.api.org.FriendComApi;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/29 17:34
 */
@Service
public class FriendComService {
    @Autowired
    private FriendComApi friendComApi;

    public Response searchCompanyFriend(Map<String, Object> param) {
        Response result = friendComApi.searchCompanyFriend(param);
        PageInfo pageInfo =result.getPageInfo();
        List<Map<String, Object>> friList = pageInfo.getList();
        if (friList != null&&friList.size()>0) {


        }


        return result;
    }

    private String getCity(Map<String, Object> map){
        String result="";

        return result;
    }
}
