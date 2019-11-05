package com.jzb.org.dao;

import com.jzb.base.message.Response;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
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


    /**
     *  所有业主-业主列表-分配售后人员
     * @param param
     * @return
     */
    int saveCompanyProperty(Map<String, Object> param);

    /**
     *  所有业主-业主列表-分配售后人员
     * @param param
     * @return
     */
    int saveCompanyPropertys(Map<String, Object> param);

    /**
     * 查各个等级次数
     * @param param
     * @return
     */
    List<Map<String, Object>> queryLevelCount(Map<String, Object> param);


    /**
     * 分配服务人员
     * @param param
     * @return
     */
    int addCompanyPropert(Map<String, Object> param);


    /**
     * 查询单位的设置等级
     * @param param
     * @return
     */
    List<Map<String, Object>> getsaveCompanyPropertys(Map<String, Object> param);


    /**
     * 修改等级
     * @param param
     * @return
     */
    int updateompanyPropertys(Map<String, Object> param);


    /**
     * 根据A类 B类  C类进行分页
     * @param param
     * @return
     */
    List<Map<String, Object>> GroupCompanyPropert(Map<String, Object> param);
}
