package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbPolicyTypeMapper {



    /**
     *     查询政策父子级
     * @param param
     * @return
     */
     List<Map<String, Object>> queryPolicyType(Map<String, Object> param);




    /**
     * LBQ
     * 查询运营管理下政策中的菜单类别
     *
     * @param
     * @return
     */
    List<Map<String, Object>> getPolicyTypes(Map<String, Object> param);

    /** LBQ
     * 运营管理下政策模块中的菜单类别新增
     *
     * @param param
     * @return
     */
    int savePolicyType(Map<String, Object> param);

    /** LBQ
     * 运营管理下政策模块中的菜单类别修改
     *
     * @param param
     * @return
     */
    int updatePolicyType(Map<String, Object> param);

    /**LBQ
     * 设置删除状态
     *
     * @param param
     * @return
     */
    int updateStatus(Map<String, Object> param);
}


