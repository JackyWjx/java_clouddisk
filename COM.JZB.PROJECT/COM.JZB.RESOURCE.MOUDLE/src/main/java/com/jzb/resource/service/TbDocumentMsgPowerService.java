package com.jzb.resource.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jzb.resource.dao.TbDocumentMsgPowerMapper;
import java.util.List;
import java.util.Map;


@Service
public class TbDocumentMsgPowerService {

    @Autowired
    private TbDocumentMsgPowerMapper tbDocumentMsgPowerMapper;



    public List<Map<String, Object>> queryPowerList() {
        return tbDocumentMsgPowerMapper.queryPowerList();
    }

    public int addPowerList(List<Map<String, Object>> param) {
        return tbDocumentMsgPowerMapper.addPowerList(param);
    }

    public int upPowerList(List<Map<String, Object>> param) {
        return tbDocumentMsgPowerMapper.upPowerList(param);
    }

    public int delPowerList(Map<String, Object> param) {
        return tbDocumentMsgPowerMapper.delPowerList(param);
    }


}
