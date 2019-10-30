package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.message.dao.MsgListMapper;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
import com.jzb.message.util.MessageUtile;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短消息服务类
 *
 * @author Chad
 * @date 2019年08月07日
 */
@Service
public class ShortMessageService {

    private final static Logger logger = LoggerFactory.getLogger(ShortMessageService.class);

    /**
     * 消息组DB
     */
    @Autowired
    MsgListMapper msgListMapper;

    /**
     * 添加消息模板
     */
    public boolean saveMsgUserTeamplate(Map<String , Object> map){
        map.put("tempid",JzbRandom.getRandomChar(9));
        map.put("status",1);
        return msgListMapper.insertMsgUserTemplate(map) > 0 ? true : false ;
    }

    /**
     * 插入一条发送记录`
     */
    public int saveSendsUserMessage(Map<String , Object> map){
        return msgListMapper.insertSendUserMessage(map);
    }

    /**
     * 根据appid 获取checkcode
     */
    public Map<String , Object> queryMsgOrganizeCheckcode(String appid){
        return msgListMapper.queryMsgOrganizeCheckcode(appid);
    }

    /**
     * 修改成已发送
     */
    public int  updateMessageListSendStatusByMsgid(Map<String , Object> msgid){
        return msgListMapper.updateMessageListSendStatusByMegid(msgid);
    }

} // End class ShortMessageService
