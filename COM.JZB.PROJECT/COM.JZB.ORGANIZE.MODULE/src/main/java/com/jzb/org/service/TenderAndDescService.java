package com.jzb.org.service;

import com.jzb.org.dao.TenderAndDescMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class TenderAndDescService {
    @Resource
    private TenderAndDescMapper tenderAndDescMapper;

    @Transactional
    public Integer addTenderMessage(Map<String, Object> param) {
        tenderAndDescMapper.addTenderMessage(param);
        tenderAndDescMapper.addTenderInfoMessage(param);
        return 1;
    }

    @Transactional
    public void putTenderMessage(Map<String, Object> param) {
        tenderAndDescMapper.putTenderMessage(param);
        tenderAndDescMapper.putTenderInfoMessage(param);
    }

    @Transactional
    public void delTenderMessage(Map<String, Object> param) {
        tenderAndDescMapper.delTenderMessage(param);
        tenderAndDescMapper.delTenderInfoMessage(param);
    }
}
