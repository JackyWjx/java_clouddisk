package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/7 16:57
 */
@Mapper
@Repository
public interface PlatformCompanyMapper {


    /**
     * 开放平台企业列表查询总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int searchPlatformComCount(Map<String, Object> param);

    /**
     * 开放平台企业列表查询
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> searchPlatformComList(Map<String, Object> param);


    /**
     * 根据企业名称或企业cid集合获取cid合集
     *
     * @param param
     * @return java.lang.String
     * @Author: DingSC
     */
    String searchCidByCidCname(Map<String, Object> param);
}
