package com.jzb.config.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 字典/字典类型
 * @Author Han Bin
 */
@Mapper
@Repository
public interface TbDictMapper {

    /**
     *  查询字典
     */
    List<Map<String , Object>> queryDictItem(Map<String, Object> paraMap);

    /**
     *  查询字典类型
     */
    List<Map<String , Object>> queryDictType(Map<String, Object> paraMap);

    /**
     *  查询字典总数
     */
    int queryDictItemCount(Map<String, Object> paraMap);

    /**
     *  查询字典类型总数
     */
    int queryDictTypeCount(Map<String, Object> paraMap);

    /**
     *  模糊查询字典
     */
    List<Map<String , Object>> seachDictItem(Map<String, Object> paraMap);

    /**
     *  模糊查询字典类型
     */
    List<Map<String , Object>> seachDictType(Map<String, Object> paraMap);

    /**
     *  模糊查询字典总数
     */
    int  seachDictItemCount(Map<String, Object> paraMap);

    /**
     *  模糊查询字典类型总数
     */
    int  seachDictTypeCount(Map<String, Object> paraMap);

    /**
     *  添加字典
     */
    int saveDictItem(Map<String, Object> paraMap);

    /**
     *  添加字典类型
     */
    int saveDictType(Map<String, Object> paraMap);

    /**
     *  修改字典
     */
    int updateDictItem(Map<String, Object> paraMap);

    /**
     *  修改字典类型
     */
    int updateDictType(Map<String, Object> paraMap);

    /**
     *  禁用字典
     */
    int deleteDictItem(Map<String, Object> paraMap);

    /**
     *  禁用字典类型
     */
    int deleteDictType(Map<String, Object> paraMap);

}
