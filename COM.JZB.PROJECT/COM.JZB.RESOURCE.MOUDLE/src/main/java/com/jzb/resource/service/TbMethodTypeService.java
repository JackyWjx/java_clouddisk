package com.jzb.resource.service;

import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbMethodTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbMethodTypeService {

    @Autowired
    private TbMethodTypeMapper tbMethodTypeMapper;

    /*
     * 1.查询方法论（父子级）
     * */
    public List<Map<String, Object>> getMethodType(Map<String, Object> param) {
        return tbMethodTypeMapper.queryMethodType(param);
    }

    /*
     * 1.查询方法论（父子级）(分页)
     * */
    public List<Map<String, Object>> queryMethodTypePage(Map<String, Object> param){
        return tbMethodTypeMapper.queryMethodTypePage(param);
    }


    /*
     * 1.查询方法论（父子级）(分页)（总数）
     * */
    public int queryMethodTypePageCount(){
        return tbMethodTypeMapper.queryMethodTypePageCount();
    }

    /*
     * 1.查询方法论（父子级）(分页)(子级)
     * */
    public List<Map<String, Object>> queryMethodTypePageSon(Map<String, Object> param){
        return tbMethodTypeMapper.queryMethodTypePageSon(param);
    }

    /*
     * 2.1新建方法论同级
     * */
    public int saveMethodTypeBrother(Map<String, Object> param) {
        if (JzbCheckParam.haveEmpty(param, new String[]{"brotherid"})) {
            param.put("parentid", "0000000");

        } else {
            param.put("parentid", getParentByBrotherId(param));
            param.put("score",100);
        }
        if(JzbCheckParam.haveEmpty(param, new String[]{"score"})){
            param.put("score",100);
        }
        Long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("typeid", JzbRandom.getRandomCharCap(7));
        param.put("idx", getMethodTypeIdx());
        return tbMethodTypeMapper.saveMethodType(param);
    }

    /*
     * 2.2新建方法论子级
     * */
    public int saveMethodTypeSon(Map<String, Object> param) {
        Long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("typeid", JzbRandom.getRandomCharCap(7));
        param.put("parantid", param.get("parantid"));
        param.put("idx", getMethodTypeIdx());
        return tbMethodTypeMapper.saveMethodType(param);
    }

    /*
     * 3.修改方法论
     * */
    public int updateMethodType(Map<String, Object> param) {
        Long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbMethodTypeMapper.updateMethodType(param);
    }

    /*
     * 4.获取排序
     * */
    public int getMethodTypeIdx() {
        return tbMethodTypeMapper.getMethodTypeIdx();
    }

    /*
     * 5.根据同级typeid查询 parentid
     * */
    public String getParentByBrotherId(Map<String, Object> param) {
        return tbMethodTypeMapper.getParentByBrotherId(param);
    }

    /*
     * 6.查询方法论资料tab方法论类别第一级  如果传了parentid就查子级，没传就查第一级
     * */
    public List<Map<String, Object>> getMethodLevel(Map<String, Object> param) {
        return tbMethodTypeMapper.queryMethodLevel(param);
    }

}
