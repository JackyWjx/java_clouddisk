package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbMethodTargetMapper {

    /*
    * 1.查询方法论目标
     */
   List<Map<String, Object>> queryMethodTarget(Map<String, Object> param);

   /*
    * 1.查询方法论目标
     */
   List<Map<String, Object>> queryMethodTargetByTypeid(Map<String, Object> param);

   /*
   * 2.添加方法论目标
   * */
   int addMethodTarget(List<Map<String, Object>> list);

    /*
     * 3.修改方法论目标状态（删除）
     * */
   int updateMethodTargetStatus(Map<String, Object> param);

    /*
     * 4.修改方法论目标
     * */
   int updateMethodTarget(Map<String, Object> param);


   /*
     * 5.获取排序
     * */
   int getMethodTargetIdx();

}
