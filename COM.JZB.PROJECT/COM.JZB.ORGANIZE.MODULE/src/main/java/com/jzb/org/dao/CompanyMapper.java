package com.jzb.org.dao;

import com.jzb.base.entity.auth.CompanyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据库处理对象
 */
@Mapper
@Repository
public interface CompanyMapper {
    /**
     * 开放平台调用接口修改企业地址,地区信息
     *
     * @param param
     * @return
     */
    int updateOrgInfo(Map<String, Object> param);

    /**
     * 主页获取单位信息
     *
     * @param param
     * @return
     */
    Map<String, Object> queryEnterpriseData(Map<String, Object> param);

    /**
     * 获取当前用户下的所以企业
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryCompanyList(Map<String, Object> param);

    /**
     * 获取企业下部门数
     *
     * @param param
     * @return
     */
    int queryDepartmentCount(Map<String, Object> param);

    /**
     * 获取企业总人数
     *
     * @param param
     * @return
     */
    int queryHeadcountCount(Map<String, Object> param);

    /**
     * 认证企业信息list表修改
     */
    int updateCompanyList(Map<String, Object> param);

    /**
     * 认证企业信息info表修改
     */
    int insertCompanyInfo(Map<String, Object> param);

    /**
     * 政府认证企业信息info表修改(业务需要特有)
     */
    int insertCompanyInfo1(Map<String, Object> param);

    /**
     * 获取单位名称
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> searchEnterpriseNames(Map<String, Object> map);

    /**
     * 创建单位
     *
     * @param map
     */
    void insertCompany(Map<String, Object> map);


    /**
     * 添加邀请
     *
     * @param map
     */
    void insertInvite(Map<String, Object> map);

    /**
     * 根据企业ID获取当前企业的超级管理员
     *
     * @author kuangbin
     */
    Map<String, Object> queryAdministratorId(Map<String, Object> param);

    /**
     * 转让企业管理员
     *
     * @author kuangbin
     */
    int updateAdministrator(Map<String, Object> param);

    /**
     * 查询申请人总数
     *
     * @return
     */
    int queryApplyCount(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 显示所有申请成员
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryApplyList(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 单个通过申请
     *
     * @author kuangbin
     */
    int updatePassProposer(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 拒绝申请通过
     *
     * @author kuangbin
     */
    int updateDenyProposer(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 批量通过成员申请
     *
     * @author kuangbin
     */
    int updateBatchPass(List<Map<String, Object>> param);

    /**
     * 成员邀请,申请
     * 单个通过申请后加入部门资源池,即默认部门
     *
     * @author kuangbin
     */
    int insertProposer(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 企业中中有该员工就修改状态为1
     *
     * @author kuangbin
     */
    int updateProposer1(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 查询企业中部门是否存在员工
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryBatchProposer(List<Map<String, Object>> param);

    /**
     * 成员邀请,申请
     * 批量修改已经存在的用户状态为1
     *
     * @author kuangbin
     */
    int updateBatchProposer(List<Map<String, Object>> param);

    /**
     * 成员邀请,申请
     * 批量通过申请后加入部门资源池,即默认部门
     *
     * @author kuangbin
     */
    int insertBatchProposer(List<Map<String, Object>> param);

    /**
     * 成员邀请,申请
     * 邀请用户加入企业
     *
     * @author kuangbin
     */
    int insertInvitee(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 邀请用户加入企业,查询企业中有没有该员工
     *
     * @author kuangbin
     */
    int queryProposerCount(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 取消用户加入企业
     *
     * @author kuangbin
     */
    int updateInvitee(Map<String, Object> param);

    /**
     * 成员邀请,申请
     * 取消成功后取消加入企业部门资源池
     *
     * @author kuangbin
     */
    int updateProposer(Map<String, Object> param);

    /**
     * 主页获取单位部门信息
     * 获取未激活总人数
     *
     * @author kuangbin
     */
    int queryInactiveCount(Map<String, Object> param);

    /**
     * 我的单位基本资料
     * 主页中单位设置基本资料进行企业list表修改
     *
     * @author kuangbin
     */
    int updateCompanyBasicList(Map<String, Object> param);

    /**
     * 我的单位基本资料
     * 主页中单位设置基本资料进行企业info表修改
     *
     * @author kuangbin
     */
    int updateCompanyBasicInfo(Map<String, Object> param);

    /**
     * 根据模糊姓名获取企业下所有同字员工姓名
     *
     * @author kuangbin
     */
    List<Map<String, Object>> searchUserName(Map<String, Object> param);

    /**
     * 根据模糊姓名获取企业下所有同字员工姓名的总数
     *
     * @param param
     * @return
     * @author kuangbin
     */
    int queryUserNameCount(Map<String, Object> param);

    /**
     * 根据用户id获取该用户加入的单位和创建的单位名称
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/16 9:39
     */
    List<Map<String, Object>> queryCompanyByUid(Map<String, Object> param);

    /**
     * 确认是否已申请形象大使
     *
     * @param param
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/24 16:47
     */
    int queryCompanyFriend(Map<String, Object> param);

    /**
     * 保存伙伴单位信息
     *
     * @param param
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/20 17:52
     */
    int insertCompanyFriend(Map<String, Object> param);

    /**
     * 修改伙伴单位状态
     *
     * @param param
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/23 16:49
     */
    int updateCompanyFriend(Map<String, Object> param);
} // End interface CompanyMapper
