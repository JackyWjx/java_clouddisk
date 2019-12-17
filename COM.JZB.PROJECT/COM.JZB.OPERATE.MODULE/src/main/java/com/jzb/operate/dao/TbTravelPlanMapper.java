package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ：Champ-Ping
 * @date ：Created in 2019-12-03 13:49
 * @description：出差计划
 * @modified By：
 * @version: 1.0$
 */
@Mapper
@Repository
public interface TbTravelPlanMapper {

//    int addTravelRecord(List<Map<String, Object>> list);
    int addTravelRecord(Map<String, Object> list);

    int addTravelDetails(List<Map<String, Object>> list);

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    int setDeleteStatus(Map<String, Object> param);

    /**
     * 设置撤回状态
     * @param param
     * @return
     */
    int setRecallStatus(Map<String, Object> param);


    /**
     * 更新出差记录
     * @param param
     * @return
     */
    int updateTravelRecord(Map<String, Object> param);

    /**
     * 更新出差详情
     * @param param
     * @return
     */
    int updateTravelDetials(Map<String, Object> param);

    /**
     * 获取出差详情
     * @param param
     * @return
     */
//    List<Map<String, Object>> getTravelDetials(Map<String, Object> param);

    /**
     * 根据出差id查询出差记录
     * @param param
     * @return
     */
    Map<String, Object> queryTravelRecordByTravelid(Map<String, Object> param);

    /**
     * 根据出差id查询出差详情
     * @param param
     * @return
     */
    List<Map<String, Object>> queryTravelDetailsByTravelid(Map<String, Object> param);

    /**
     *  @author: gongWei
     *  @Date:  2019/12/13 14:04
     *  @Description: 获取出差记录列表
     *  @Param:
     *  @Return:
     *  @Exception:
     */

    List<Map<String, Object>> queryTravelRecordByUid(Map<String, Object> param);

    /**
     *  @author: gongWei
     *  @Date:  2019/12/13 14:09
     *  @Description: 获取出差记录列表总数
     *  @Param:
     *  @Return:
     *  @Exception:
     */
    int queryTravelRecordCountByUid(Map<String, Object> param);
    /**
     *  @author: gongWei
     *  @Date:  2019/12/16 11:27
     *  @Description: 获取出差审批记录列表总数
     *  @Param:
     *  @Return:
     *  @Exception:
     */
    int queryTravelApprovalRecordCountByUid(Map<String, Object> param);
    /**
     *  @author: gongWei
     *  @Date:  2019/12/16 11:41
     *  @Description: 获取出差审批记录列表
     *  @Param:
     *  @Return:
     *  @Exception:
     */

    List<Map<String, Object>> queryTravelApprovalRecordByUid(Map<String, Object> param);

    int setDetailsStatusByTravelid(Map<String, Object> param);
}
