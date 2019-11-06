package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbSolutionTypeMapper {

    /**
     * 1.查询方案类型（父子级）
     */
    List<Map<String, Object>> querySolutionType(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文件列表-分类2
     * 点击新增按钮新建文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int insertSolutionType(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文件列表-分类3
     * 点击修改按钮进行文章分类修改
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int updateSolutionType(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文件列表-分类1
     * 点击新建显示文章分类的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int querySolutionTypeDataCount(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文件列表-分类1
     * 点击新建显示文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> getSolutionTypeData(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文件列表-分类4
     * 查询分类下是否存在数据
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int getSolutionTypeDocument(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文件列表-分类4
     * 点击新建显示文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int deleteSolutionType(Map<String, Object> param);
}


