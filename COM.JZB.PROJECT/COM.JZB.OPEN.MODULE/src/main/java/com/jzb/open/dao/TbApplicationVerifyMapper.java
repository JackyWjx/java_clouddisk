package com.jzb.open.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbApplicationVerifyMapper {

    /**
     * 提交应用列表到审批列表
     * @param param
     * @return
     */
    int saveApplicationVerify(Map<String, Object> param);

    /**
     * 修改成未审批状态
     * @param param
     * @return
     */
    int updateApplicationVerify(Map<String, Object> param);

    /**
     * 根据appid查询这个应用存不存在
     * @param param
     * @return
     */
    int getApplicationVerify(Map<String, Object> param);

    /**
     * 提交应用菜单列表到审批表
     * @param param
     * @return
     */
    int saveApplicationMenuVerify(Map<String, Object> param);

    /**
     * 提交应用页面到审批表
     * @param param
     * @return
     */
    int saveApplicationPageVerify(Map<String, Object> param);

    /**
     * 单点登录审批通过后显示出来的应用
     * @param param
     * @return
     */
    List<Map<String, Object>> getApplicationPageVerify(Map<String, Object> param);
}
