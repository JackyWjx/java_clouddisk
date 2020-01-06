package com.jzb.resource.service;

import com.jzb.resource.dao.TbMethodDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbMethodDataService {

    @Autowired
    private TbMethodDataMapper tbMethodDataMapper;


    /*
     * 1.根据方法论类型查询方法论资料
     * */
    public List<Map<String, Object>> quertMethodData(Map<String, Object> param){
       return tbMethodDataMapper.quertMethodData(param);
    }

    /*
     * 2.修改方法论资料
     * */
    public int updateMethodData(Map<String, Object> param){
        Long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbMethodDataMapper.updateMethodData(param);
    }

    /*
     * 3.添加方法论资料
     * */
    public int saveMethodData(Map<String, Object> param){
        Long time=System.currentTimeMillis();
        param.put("addtime",time);
        param.put("updtime",time);
        param.put("idx", getMethodDataIdx());
        return tbMethodDataMapper.saveMethodData(param);
    }
    /*
     * 3.添加方法论资料同级子级
     * */
    public int saveMethodDatanew(Map<String, Object> param){
        Long time=System.currentTimeMillis();
        if(param.get("parentid")==null || param.get("parentid").toString().equals("")){
            param.put("parentid", "0000000");
        }
        param.put("addtime",time);
        param.put("updtime",time);
        //param.put("idx", getMethodDataIdx());
        return tbMethodDataMapper.saveMethodData(param);
    }

    /*
     * 4.获取排序
     * */
    public int getMethodDataIdx(){
        return tbMethodDataMapper.getMethodDataIdx();
    }



    public List<Map<String, Object>> getMethodDataItme(Map<String, Object> param) {
        return tbMethodDataMapper.getMethodDataItme(param);
    }

    public int delMethodData(Map<String, Object> param) {return tbMethodDataMapper.delMethodData(param);
    }

    public int getMethodDatazi(Map<String, Object> param) {

        return tbMethodDataMapper.getMethodDatazi(param);
    }

    public List<Map<String, Object>> getMethodDataTypeIdsAll(Map<String, Object> param) {
        return tbMethodDataMapper.getMethodDataTypeIdsAll(param);
    }

    public int delMethodDataids(Map<String, Object> param) {
        return tbMethodDataMapper.delMethodDataids(param);
    }

    public List<Map<String, Object>> getMethodDataByTypeid(Map<String, Object> param) {
        return tbMethodDataMapper.getMethodDataByTypeid(param);
    }
}
