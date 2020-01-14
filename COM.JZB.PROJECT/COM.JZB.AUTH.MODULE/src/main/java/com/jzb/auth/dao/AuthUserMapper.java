package com.jzb.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据库处理对象
 */
@Mapper
@Repository
public interface AuthUserMapper {
    /**
     * Query User Info
     *
     * @param param
     * @return
     */
    Map<String, Object> queryUserInfo(Map<String, Object> param);

    /**
     * 查uid czd add
     * @param param
     * @return
     */
    String queryUidByPhoneOrRegid(Map<String,Object> param);
    /**
     * 根据用户名 uid 模糊匹配是否是我所需要的数据
     *
     * hanbin
     */
    int queryPersionByName(Map<String, Object> param);

    /**
     * 统一list和key中手机号
     * @param param
     * @return
     */
    int updateUserPhoneNo1(Map<String, Object> param);

    /**
     * 认证用户user_list表信息修改
     */
    int updateIdentityList(Map<String, Object> param);


    int queryIsExists(Map<String, Object> param);

    /**
     * 认证身份证user_info表信息修改
     */
    int insertIdentityInfo(Map<String, Object> param);

    /**
     * 用户密码信息list表修改
     */
    int updateUserPassword(Map<String, Object> param);

    /**
     * 根据联系方式或者ID获取用户信息
     */
    Map<String, Object> queryUserIdNameByPhone(Map<String, Object> param);

    /**
     * 根据id获取用户角色信息
     *
     * @param param
     * @return
     */
    Map<String, Object> queryRoleByUid(Map<String, Object> param);

    /**
     * 跟据手机号查询注册表中信息
     *
     * @param map
     * @return
     */
    List<Map> searchSendCode(Map map);

    /**
     * 跟据手机号查询注册表中信息
     *
     * @param map
     * @return
     */
    List<Map> searchSendCodeByRelphone(Map map);

    /**
     * 新增用户信息,用户注册信息
     *
     * @param map
     */
    void insertUserList(Map map);


    /**
     * 最后六位身份证号
     *
     * @param map
     * @return
     */
    List<Map> idCardLastSix(Map map);

    /**
     * 单位设置-申请/邀请成员
     * 通过名字模糊搜索被邀请用户
     *
     * @author kuangbin
     */
    List<Map<String, Object>> searchInvitee(Map<String, Object> param);

    /**
     * 单位设置-申请/邀请成员
     * 查询被邀请人总数
     *
     * @author kuangbin
     */
    int queryInviteeCount(Map<String, Object> param);

    /**
     * 单位设置-申请/邀请成员
     * 根据电话搜索用户
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryInvitee(Map<String, Object> param);

    /**
     * 根据用户姓名或手机号查询用户id
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> searchUserName(Map<String, Object> param);

    /**
     * 根据用户姓名或手机号查询用户信息总数
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/11 18:18
     */
    int searchUserNameCount(Map<String, Object> map);

    /**
     * 我的基本资料
     * 点击头像后修改基本资料
     *
     * @author kuangbin
     */
    int modifyUserBasicData(Map<String, Object> param);

    /**
     * 查询APP的验证密码
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryAppSecret(Map<String, Object> param);

    /**
     * 获取用户ID
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryUserByValue(Map<String, Object> param);

    /**
     * 查询用户所有权限
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> queryUserMenuList(Map<String, Object> param);

    /**
    * 激活修改用户状态
    * @Author: DingSC
    * @param param
    * @return int
    */
    int updateUserStatus(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 点击业主下的人员中的新增人员按钮进行加入员工
     *
     * @author kuangbin
     */
    int insertCompanyEmployee(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主下的人员12
     * 点击业主下的人员中的修改人员按钮进行修改员工信息
     *
     * @author kuangbin
     */
    int updateCompanyEmployee(Map<String, Object> param);

    /**
     * 根据手机号模糊搜索用户姓名总数
     *
     * @author kuangbin
     */
    int searchUserNameListCount(Map<String, Object> param);

    /**
     * 根据手机号模糊搜索用户姓名
     *
     * @author kuangbin
     */
    List<Map<String, Object>> searchUserNameList(Map<String, Object> param);

    /*
     * @Author Reed
     * @Description 根据用户id查询密码
     * @Date 15:07 2019/12/18
     * @Param [param]
     * @return java.lang.String
    **/
    String getInitPassWd(Map<String, Object> param);

    List<Map<String, Object>> getUserNameList(Map<String, Object> param);

    List<Map<String, Object>> getUidByUname(Map<String, Object> param);
} // End interface AuthUserMapper
