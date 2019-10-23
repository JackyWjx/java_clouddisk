package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TbCompanyPropertyMapper {

    /**
     * 添加单位动态属性
     * @param param
     * @return
     */
    int addCompanyProperty(Map<String, Object> param);


    /**
     * 查询企业id 以及 类型id 是否存在，给设置业主等级提供
     * @param param
     * @return
     */
    int queryexists(Map<String, Object> param);



    /**
     * 修改属性默认值
     * @param param
     * @return
     */
    int updatePropertyByCidAndTypeid(Map<String, Object> param);




}
