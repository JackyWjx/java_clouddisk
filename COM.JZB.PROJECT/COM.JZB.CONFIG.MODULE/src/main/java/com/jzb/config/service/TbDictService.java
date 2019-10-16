package com.jzb.config.service;

import com.jzb.config.dao.TbDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 字典/字典类型
 * @Author Han Bin
 */
@Service
public class TbDictService {

    /**
     *  DB
     */
    @Autowired
    private TbDictMapper mapper;


    /**
     *  查询字典
     */
    public List<Map<String , Object>> queryDictItem(Map<String , Object> paraMap){
        return mapper.queryDictItem(paraMap);
    }

    /**
     *  查询字典类型
     */
    public List<Map<String , Object>> queryDictType(Map<String , Object> paraMap){
        return mapper.queryDictType(paraMap);
    }

    /**
     *  查询字典总数
     */
    public int queryDictItemCount(Map<String , Object> paraMap){
        return mapper.queryDictItemCount(paraMap);
    }

    /**
     *  查询字典类型总数
     */
    public int queryDictTypeCount(Map<String , Object> paraMap){
        return mapper.queryDictTypeCount(paraMap);
    }

    /**
     *  模糊查询字典
     */
    public List<Map<String , Object>> seachDictItem(Map<String , Object> paraMap){
        return mapper.seachDictItem(paraMap);
    }

    /**
     *  模糊查询字典类型
     */
    public List<Map<String , Object>> seachDictType(Map<String , Object> paraMap){
        return mapper.seachDictType(paraMap);
    }

    /**
     *  模糊查询字典总数
     */
    public int  seachDictItemCount(Map<String , Object> paraMap){
        return mapper.seachDictItemCount(paraMap);
    }

    /**
     *  模糊查询字典类型总数
     */
    public int  seachDictTypeCount(Map<String , Object> paraMap){
        return mapper.seachDictTypeCount(paraMap);
    }

    /**
     *  添加字典
     */
    public int saveDictItem(Map<String , Object> paraMap){
        return mapper.saveDictItem(paraMap);
    }

    /**
     *  添加字典类型
     */
    public int saveDictType(Map<String , Object> paraMap){
        return mapper.saveDictType(paraMap);
    }

    /**
     *  修改字典
     */
    public int updateDictItem(Map<String , Object> paraMap){
        return mapper.updateDictItem(paraMap);
    }

    /**
     *  修改字典类型
     */
    public int updateDictType(Map<String , Object> paraMap){
        return mapper.updateDictType(paraMap);
    }

    /**
     *  禁用字典
     */
    public int removeDictItem(Map<String , Object> paraMap){
        return mapper.deleteDictItem(paraMap);
    }

    /**
     *  禁用字典类型
     */
    public int removeDictType(Map<String , Object> paraMap){
        return mapper.deleteDictType(paraMap);
    }

}
