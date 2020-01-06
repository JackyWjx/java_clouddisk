package com.jzb.operate.service;

import com.jzb.operate.api.resource.MethodDataApi;
import com.jzb.operate.dao.TbCompanyMethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyMethodService {
    @Autowired
    private TbCompanyMethodMapper tbCompanyMethodMapper;

    @Autowired
    private MethodDataApi methodDataApi;


    /**
     * 添加单位方法论（修改同步）
     * @param list
     * @return
     */
    public int addCompanyMethod(List<Map<String, Object>> list){
        return tbCompanyMethodMapper.addCompanyMethod(list);
    }


    /**
     * 查询单位方法论
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyMethod(Map<String, Object> param){
        List<Map<String, Object>> list = tbCompanyMethodMapper.queryCompanyMethod(param);
        return list;
    }

    /**
     * 设置方法论状态
     * @param param
     * @return
     */
    public int updateCompanyMethodStatus(Map<String, Object> param){
        param.put("status",'0');
        return tbCompanyMethodMapper.updateCompanyMethodStatus(param);
    }

    public List<Map<String, Object>> getCompanyMethodByids(Map<String, Object> param) {

        return tbCompanyMethodMapper.getCompanyMethodByids(param);
    }

    public int addMethodData(List<Map<String, Object>> datalist) {

        return tbCompanyMethodMapper.addMethodData(datalist);
    }

    public List<Map<String, Object>> getCompanyMethoddataAll(Map<String, Object> param) {
        return tbCompanyMethodMapper.getCompanyMethoddataAll(param);
    }

    public int delCompanyMethod(String param) {
        return tbCompanyMethodMapper.delCompanyMethod(param);
    }

    public void delcidsandprojectid(Map<String, Object> map) {
         tbCompanyMethodMapper.delcidsandprojectid(map);
    }
}
