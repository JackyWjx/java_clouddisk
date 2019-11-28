package com.jzb.open.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/7 15:13
 */
@Mapper
@Repository
public interface PlatformComMapper {

    /**
     * 获取所有开放平台的企业id
     *
     * @param param
     * @return java.lang.String
     * @Author: DingSC
     */
    Map<String, Object> queryPlatformIds(Map<String, Object> param);

    /**
     * 开发者列表查询
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> searchAppDeveloper(Map<String, Object> param);

    /**
     * 开发者列表查询count
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int searchAppDeveloperCount(Map<String, Object> param);

    /**
     * 根据cid和集获取管理员信息
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> getComAndMan(Map<String, Object> param);

    /**
     * 产品列表审批查询
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> searchApplicationVerify(Map<String, Object> param);

    /**
     * 产品列表审批查询count
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int searchApplicationVerifyCount(Map<String, Object> param);

    /**
     * 审批产品列表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int updateVerify(Map<String, Object> param);

    /**
     * 查询应用审批表数据
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> queryVerify(Map<String, Object> param);

    /**
     * 新增平台开发文档表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int insertPlatformHelper(Map<String, Object> param);

    /**
     * 修改平台开发文档表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int updatePlatformHelper(Map<String, Object> param);

    /**
     * 查询平台开发文档表
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> searchPlatformHelper(Map<String, Object> param);

    /**
     * 查询平台开发文档表总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int searchPlatformHelperCount(Map<String, Object> param);

    /**
     * 新增开放文档类型
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int insertOpenApiType(Map<String, Object> param);

    /**
     * 修改开放文档类型
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int updateOpenApiType(Map<String, Object> param);

    /**
     * 获取开放文档类型
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> getOpenApiType(Map<String, Object> param);

    /**
     * 新增文档类型接口表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int insertOpenApiList(Map<String, Object> param);

    /**
     * 修改文档类型接口表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int updateOpenApiList(Map<String, Object> param);

    /**
     * 模糊查询文档类型接口
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> searchOpenApiList(Map<String, Object> param);

    /**
     * 查询文档类型接口总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int searchOpenApiListCou(Map<String, Object> param);

    /**
     * 新增开发者应用
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int insertOrgApplication(Map<String, Object> param);

    /**
     * 修改开发者应用
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int updateOrgApplication(Map<String, Object> param);

    /**
     * 根据AppId查询应用审批表存在的个数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int queryVerifyCount(Map<String, Object> param);

    /**
     * 根据appid查询页面
     * @param param
     * @return
     */
    List<Map<String, Object>> getApplicationPage(Map<String, Object> param);
}
