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

    /**
    * 开放平台添加产品
    * @Author: DingSC
    * @param param
    * @return int
    */
    int insertProductByOpen(Map<String, Object> param);

    int insertProductByOpens(Map<String, Object> param);

    /**
     * 添加菜单数据
     * @param list
     * @return
     */
    int saveMune(List<Map<String, Object>> list);

    /**
     * 添加页面
     * @param list1
     * @return
     */
    int savepage(List<Map<String, Object>> list1);

    int getMune(Map<String, Object> map);

    int updateMune(Map<String, Object> map);

    int getPage(Map<String, Object> map);

    int updatePage(Map<String, Object> map);
}
