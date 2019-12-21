package com.jzb.org.service;

import com.jzb.org.dao.TenderAndDescMapper;
import com.jzb.org.dao.TenderResultAndDescMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TenderResultAndDescService {

    @Resource
    private TenderResultAndDescMapper tenderResultAndDescMapper;

    @Resource
    private TenderAndDescMapper tenderAndDescMapper;

    @Transactional
    public Integer addTenderMessage(Map<String, Object> param) {
        tenderResultAndDescMapper.addTenderMessage(param);
        tenderResultAndDescMapper.addTenderInfoMessage(param);
        return 1;
    }


    public List<Map<String, Object>> queryTenderMessage(Map<String, Object> param) {
        List<Map<String, Object>> result;
        if (!("").equals(param.get("type")) && param.get("type") != null) {
            if (param.get("type").equals("1")) {
                //招标
                result = tenderAndDescMapper.queryTenderMessage(param);
            } else {
                //中标
                result = tenderResultAndDescMapper.queryTenderResultMessage(param);
            }
        } else {
            result = tenderAndDescMapper.queryAllTenderAndDescMessage(param);
        }
        return result;
    }

    public Integer queryTenderMessageCount(Map<String, Object> param) {
        Integer num;
        if (!("").equals(param.get("type")) && param.get("type") != null) {

            if (param.get("type").equals("1")) {
                //招标
                num = tenderAndDescMapper.queryTenderMessageCount(param);
            } else {
                //中标
                num = tenderResultAndDescMapper.queryTenderResultMessageCount(param);
            }
        } else {
            num = tenderResultAndDescMapper.queryTenderResultMessageCount(param);
            num += tenderAndDescMapper.queryTenderMessageCount(param);
        }
        return num;
    }

    @Transactional
    public void putTenderMessage(Map<String, Object> param) {
        tenderResultAndDescMapper.updateTenderMessage(param);
        tenderResultAndDescMapper.updateTenderInfoMessage(param);
    }

    @Transactional
    public void delTenderMessage(Map<String, Object> param) {
        tenderResultAndDescMapper.delTenderMessage(param);
        tenderResultAndDescMapper.delTenderInfoMessage(param);
    }

    public Map<String,Object> getTenderMessageBeforeUpdate(Map<String, Object> param){
        if (param.get("type").equals("1")) {
            //招标
            return  tenderAndDescMapper.getTenderMessageBeforeUpdate(param);
        } else {
            //中标
            return tenderResultAndDescMapper.getTenderMessageBeforeUpdate(param);
        }
    }
}
