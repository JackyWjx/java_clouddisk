package com.jzb.org.service;

import com.jzb.org.dao.TbCompanyContractFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyContractFileService {
    @Autowired
    private TbCompanyContractFileMapper tbCompanyContractFileMapper;

    /**
     * 添加附件
     *
     * @param list
     * @return
     */
    public int addCompanyContractFile(List<Map<String, Object>> list) {
        return tbCompanyContractFileMapper.addCompanyContractFile(list);
    }

    /**
     * 化学删除附件
     *
     * @param list
     * @return
     */
    public int updateFileStatus(List<Map<String, Object>> list) {
        return tbCompanyContractFileMapper.updateFileStatus(list);
    }

    /**
     * 查询合同下的附件
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findFileByContId(Map<String, Object> param) {
        return tbCompanyContractFileMapper.findFileByContId(param);
    }
}
