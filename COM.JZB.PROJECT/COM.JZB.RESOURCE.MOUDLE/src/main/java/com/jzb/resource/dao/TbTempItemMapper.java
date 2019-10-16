package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbTempItemMapper {

    /*
     * 1.根据模板类型查询条目
     * */
    List<Map<String, Object>> queryTempItem(Map<String, Object> param);

    /*
     * 2.添加条目
     * */
    int saveTempItem(Map<String, Object> param);

    /*
     * 3.根据同级id查询父级id
     * */
    String queryParentByBrotherId(Map<String, Object> param);

    /*
     * 4.修改条目
     * */
    int updateTempItem(Map<String, Object> param);

    /*
     * 5.修改状态  1正常  2不正常  4 删除
     * */
    int updateStatus(Map<String, Object> param);

    /*
     * 6.获取排序
     * */
    int getTempItemIdx();
}
