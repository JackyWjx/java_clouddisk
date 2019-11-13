package com.jzb.open.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据库处理对象
 */
@Mapper
@Repository
public interface OpenAPIMapper {
    /**
     * 创建文档类型
     */
    public int insertApiType(Map<String, Object> map);

    /**
     * 获取文档类型
     * @return
     */
    public List<Map<String, Object>> queryApiType(Map<String, Object> param);

    /**
     * 创建API
     */
    public int insertApi(Map<String, Object> map);

    /**
     * 获取文档类型
     * @return
     */
    public List<Map<String, Object>> queryApiList(Map<String, Object> param);

    /**
     * 获取API内容
     * @return
     */
    public List<Map<String, Object>> queryApiContent(Map<String, Object> param);

    /**
     * 获取使用帮助文档标题
     * @return
     */
    public List<Map<String, Object>> queryHelperTitle(Map<String, Object> param);

    /**
     * 获取使用帮助文档内容
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryHelperContent(Map<String, Object> param);

    /**
     * 入驻开放平台
     * @param param
     * @return
     */
    int insertOpenPlatform(Map<String, Object> param);

    /**
     * 获取api类型总数
     * @return
     */
    int queryApiTypeCount();

    /**
     * 获取文档类型的API列表的总数
     * @return
     */
    int queryApiListCount();

    /**
     * 获取使用帮助文档总数
     * @return
     */
    int queryHelperCount();

    /**
     * 创建应用
     * @param param
     * @return
     */
    int insertApp(Map<String, Object> param);

    /***
     * 获取应用列表
     */
    List<Map<String,Object>>  queryRogApplication(Map<String,Object> param);

    /***
     * 获取入驻企业列表
     */
    List<Map<String,Object>>  queryPlatformOrg(Map<String,Object> param);

    /***
     * 模糊查询APi
     */
    List<Map<String,Object>>  searchApiByName(Map<String,Object> param);

    /***
     * 获取开发者列表
     */
    List<Map<String,Object>>  queryAppDeveloper(Map<String,Object> param);

    /**
     * 添加开发者
     * @param param
     * @return
     */
    int insertDeveloper(Map<String, Object> param);

    /**
     * 移除开发者
     * @param param
     * @return
     */
    int updateDeveloper(Map<String, Object> param);

    /**
     * 验证是否已加入开发者列表
     * @param param
     * @return
     */
    int queryPhoneCount(Map<String, Object> param);

    /**
     * 显示企业是否入驻
     * @param param
     * @return
     */
    int queryWhetherEnter(Map<String, Object> param);

    /**
     * 修改应用信息
     * @param param
     * @return
     */
    int updateApp(Map<String, Object> param);

    /**
     * 添加以有的开发者,修改状态为1
     * @param param
     */
    int updateStatus(Map<String, Object> param);

    /**
     * 验证ID是否已存在开发者列表
     * @param param
     * @return
     */
    int queryIdCount(Map<String, Object> param);

    /**
     * 修改当前应用的管理员为普通开发者
     * @param param
     * @return
     */
    int updateDevauth(Map<String, Object> param);

    /***
     * 获取应用列表总数
     */
    int queryApplicationCount(Map<String, Object> param);

    /***
     * 获取入驻企业总数
     */
    int queryPlatformCount(Map<String, Object> param);

    /***
     * 获取模糊查询API总数
     */
    int queryApiCount(Map<String, Object> param);
} // End interface OpenAPIMapper
