package com.jzb.auth.service;

import com.alibaba.fastjson.JSONArray;
import com.jzb.auth.api.redis.OrgCacheRedisApi;
import com.jzb.auth.dao.RoleMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/12 16:21
 */
@Service
public class RoleService implements Service {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * Redis获取接口
     */
    @Autowired
    private OrgCacheRedisApi orgCacheRedisApi;

    /**
     * 获取角色组总数
     *
     * @param map
     * @return
     */
    public int getRoleCount(Map<String, Object> map) {
        return roleMapper.queryRoleCount(map);
    }

    /**
     * 根据企业id获取角色组信息
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getRoleList(Map<String, Object> map) {
        return roleMapper.queryRoleList(map);
    }

    /**
     * 获取角色组角色关联表总数
     *
     * @param map
     * @return
     */
    public int getRelationCount(Map<String, Object> map) {
        return roleMapper.queryRelationCount(map);
    }

    /**
     * 获取角色组角色关联表信息
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getRelationList(Map<String, Object> map) {
        List<LinkedHashMap<String, Object>> relList = roleMapper.queryRelationList(map);
        List<Map<String, Object>> list = new ArrayList();
        if (relList.size() > 0) {
            for (int i = 0; i < relList.size(); i++) {
                Map<String, Object> relMap = relList.get(i);
                int rrType = JzbDataType.getInteger(relMap.get("rrtype"));
                if (rrType == 1) {
                    String rrId = JzbDataType.getString(relMap.get("rrid"));
                    Map<String, Object> deptMap = new HashMap(4);
                    deptMap.put("key", relMap.get("rrid"));
                    //从redis中获取部门信息
                    Response response = orgCacheRedisApi.getDeptMap(deptMap);
                    if (JzbDataType.isMap(response.getResponseEntity())) {
                        deptMap = (Map) response.getResponseEntity();
                    }
                    relMap.put("rrmc", deptMap.get("cname"));
                    list.add(relMap);
                } else {
                    list.add(relMap);
                }
            }
        }
        return list;
    }


    /**
     * 保存角色组信息
     */
    public Map<String, Object> insertRoleGroup(Map<String, Object> map) {
        String crgId = JzbDataType.getString(map.get("crgid"));
        if (JzbTools.isEmpty(crgId)) {
            crgId = JzbRandom.getRandomChar(11);
        }
        long addTime = System.currentTimeMillis();
        map.put("crgId", crgId);
        map.put("addTime", addTime);
        map.put("status", "1");
        roleMapper.insertRoleGroup(map);
        Map<String, Object> rMap = new HashMap(1);
        rMap.put("crgid", crgId);
        return rMap;
    }

    /**
     * 新增或修改角色组菜单权限信息
     *
     * @param map
     */
    public Map<String, Object> saveRoleMenuAuth(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(2);
        int code = 1;
        boolean isMap = JzbDataType.isMap(map.get("menu"));
        if (isMap) {
            code = insertRoleMenuAndCon(map);
        } else {
            //参数错误
            code = 3;
        }
        result.put("code", code);
        return result;
    }

    /**
     * 角色组新增产品权限
     *
     * @param map
     * @return void
     * @Author: DingSC
     * @DateTime: 2019/9/9 16:01
     */
    private int insertRoleMenuAndCon(Map<String, Object> map) {
        int code = 1;
        String pid = JzbDataType.getString(map.get("pid"));
        //第一步从redis获取产品权限树
        Response response = orgCacheRedisApi.getProductTree(map);
        if (JzbDataType.isMap(response.getResponseEntity())) {
            Map<String, Object> redis = (Map<String, Object>) response.getResponseEntity();
            JSONArray jsonArray = JSONArray.parseArray(JzbDataType.getString(redis.get(pid)));
            //角色组菜单表
            List<Map<String, Object>> menuList = new ArrayList<>();
            //角色组功能权限表
            List<Map<String, Object>> controlList = new ArrayList<>();

            List<List<Map<String, Object>>> tempList = new LinkedList<>();
            int size = jsonArray.size();
            List<Map<String, Object>> temp = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                //获取最上层的菜单权限
                Map<String, Object> midMap = (Map<String, Object>) jsonArray.get(i);
                temp.add(midMap);
            }
            tempList.add(temp);
            Map<String, Object> conMap = getMenuMap(map);
            do {
                if (tempList.size() > 0) {
                    //循环第一个list
                    List<Map<String, Object>> data = tempList.get(0);
                    int dataSize = data == null ? 0 : data.size();
                    for (int i = 0; i < dataSize; i++) {
                        //获取最上层的菜单权限
                        Map<String, Object> midMap = data.get(i);
                        List<Map<String, Object>> pageList = (List<Map<String, Object>>) midMap.get("children");
                        int page = JzbDataType.getInteger(pageList == null ? "0" : pageList.size());
                        if (page > 0) {
                            //存在页面，默认不存在
                            boolean existPage = false;

                            //循环children中的数据
                            for (int j = 0; j < page; j++) {
                                //循环页面数据
                                Map<String, Object> pageTemp = pageList.get(j);
                                if (JzbDataType.getInteger(pageTemp.get("status")) == 1) {
                                    existPage = true;
                                    Map<String, Object> pageMap = new HashMap(conMap);
                                    pageMap.put("mid", midMap.get("id"));
                                    if (!JzbTools.isEmpty(pageTemp.get("id"))) {
                                        pageMap.put("pageid", pageTemp.get("id"));
                                        //控件数据
                                        List<Map<String, Object>> conList = (List<Map<String, Object>>) pageTemp.get("children");
                                        if (conList != null) {
                                            int conSize = JzbDataType.getInteger(conList.size());
                                            for (int k = 0; k < conSize; k++) {
                                                Map<String, Object> conTemp = conList.get(k);
                                                if (JzbDataType.getInteger(conTemp.get("status")) == 2) {
                                                    //添加控件id
                                                    Map<String, Object> controlMap = new HashMap(pageMap);
                                                    controlMap.put("controlid", conTemp.get("id"));
                                                    controlList.add(controlMap);
                                                }
                                            }
                                        }
                                    }
                                    menuList.add(pageMap);
                                }
                            }

                            //children只有菜单没有页面
                            if (!existPage) {
                                if (JzbDataType.getInteger(midMap.get("status")) == 0) {
                                    //菜单没有页面
                                    Map<String, Object> pageMap = new HashMap(conMap);
                                    pageMap.put("mid", midMap.get("id"));
                                    menuList.add(pageMap);
                                }
                            }

                        } else {
                            if (JzbDataType.getInteger(midMap.get("status")) == 0) {
                                //菜单没有页面
                                Map<String, Object> pageMap = new HashMap(conMap);
                                pageMap.put("mid", midMap.get("id"));
                                menuList.add(pageMap);
                            }
                        }
                        //获取子菜单的数据
                        List<Map<String, Object>> children = (List<Map<String, Object>>) midMap.get("children");
                        int mapSize = children == null ? 0 : children.size();
                        if (mapSize > 0) {
                            //存在子菜单，放入临时list中
                            tempList.add(children);
                        }
                    }
                    //删除掉第一个list
                    tempList.remove(0);
                }
            } while (tempList.size() > 0);
            //第二步添加到数据库
            if (menuList.size() > 0) {
                //保存角色组菜单表
                roleMapper.insertRoleMenuAuth(menuList);
            }
            if (controlList.size() > 0) {
                //保存角色组功能权限表
                roleMapper.insertRoleControlAuth(controlList);
            }
            //将前台选中数据保存进数据库，修改状态
            updateRoleMenuAndCon(map);
        } else {
            //redis获取数据失败
            code = 4;
        }
        return code;
    }

    /**
     * 修改角色组产品权限
     *
     * @param map
     * @return void
     * @Author: DingSC
     * @DateTime: 2019/9/10 16:32
     */
    public void updateRoleMenuAndCon(Map<String, Object> map) {
        //解析前台传值，获取list数据
        Map<String, List<Map<String, Object>>> param = getListParam(map);
        //角色组菜单表
        List<Map<String, Object>> menuList = param.get("menu");
        //角色组功能权限表
        List<Map<String, Object>> controlList = param.get("control");
        //第一步修改整个角色组产品权限勾选状态
        Map<String, Object> menu = getMenuMap(map);
        menu.put("status", "2");
        //角色组菜单表
        //状态全部改为2禁止
        roleMapper.updateRoleMenuAuth(menu);
        //根据勾选情况保存菜单
        roleMapper.updateRoleMenuAuthList(menuList);
        //角色组功能权限表
        //同上
        roleMapper.updateRoleControlAuth(menu);
        roleMapper.updateRoleControlAuthList(controlList);
    }

    /**
     * 获取菜单公用数据
     *
     * @param map
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     * @DateTime: 2019/9/10 17:14
     */
    private Map<String, Object> getMenuMap(Map<String, Object> map) {
        Map<String, Object> conMap = new HashMap(10);
        conMap.put("crgid", map.get("crgid"));
        conMap.put("pid", map.get("pid"));
        conMap.put("cid", map.get("cid"));
        conMap.put("ptype", map.get("ptype"));
        conMap.put("ouid", map.get("uid"));
        conMap.put("addtime", System.currentTimeMillis());
        conMap.put("updtime", System.currentTimeMillis());
        return conMap;
    }

    /**
     * 遍历前台选择菜单权限的值
     *
     * @param map
     * @return java.util.Map<java.lang.String, java.util.List < java.util.Map < java.lang.String, java.lang.Object>>>
     * @Author: DingSC
     * @DateTime: 2019/9/9 15:26
     */
    private Map<String, List<Map<String, Object>>> getListParam(Map<String, Object> map) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>(2);
        Map<String, Object> conMap = getMenuMap(map);
        Map<String, Object> menuMap = (Map) map.get("menu");
        //角色组菜单表
        List<Map<String, Object>> menuList = new ArrayList<>();
        //角色组功能权限表
        List<Map<String, Object>> controlList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : menuMap.entrySet()) {
            //第一层mid，菜单id
            conMap.put("mid", entry.getKey());
            if (JzbDataType.isMap(entry.getValue())) {
                //页面map
                Map<String, Object> pageMap = (Map) entry.getValue();
                for (Map.Entry<String, Object> pageEntry : pageMap.entrySet()) {
                    //第二层pageid，页面id
                    Map<String, Object> param = new HashMap(conMap);
                    param.put("pageid", pageEntry.getKey());
                    if (JzbDataType.isMap(pageEntry.getValue())) {
                        param.put("status", "1");
                        //控件map
                        Map<String, Object> controlMap = (Map) pageEntry.getValue();
                        for (Map.Entry<String, Object> conEntry : controlMap.entrySet()) {
                            //第三次controlid，控件id
                            Map<String, Object> con = new HashMap(param);
                            con.put("controlid", conEntry.getKey());
                            con.put("status", JzbDataType.getString(conEntry.getValue()));
                            controlList.add(con);
                        }
                    } else {
                        //该页面下所有控件状态一致
                        param.put("status", JzbDataType.getString(pageEntry.getValue()));
                        controlList.add(param);
                    }
                    menuList.add(param);
                }
            } else {
                //该菜单下所有页面控件状态一致
                Map<String, Object> param = new HashMap(conMap);
                param.put("status", JzbDataType.getString(entry.getValue()));
                menuList.add(param);
                controlList.add(param);
            }
        }
        result.put("menu", menuList);
        result.put("control", controlList);
        return result;
    }


    /**
     * 逻辑删除角色组信息
     *
     * @param map
     */
    public int updateRoleGroup(Map<String, Object> map) {
        return roleMapper.updateRoleGroup(map);
    }

    /**
     * 修改角色组信息
     *
     * @param map
     */
    public int updateRoleGroup1(Map<String, Object> map) {
        map.put("updTime", System.currentTimeMillis());
        return roleMapper.updateRoleGroup1(map);
    }

    /**
     * 获取已经勾选的角色组菜单表信息
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryRoleMenuAuth(Map<String, Object> map) {
        return roleMapper.queryRoleMenuAuthList(map);
    }


    /**
     * 新增角色组关联信息
     *
     * @param map
     * @return
     */
    public int addRoleRelation(Map<String, Object> map) {
        int result;
        //先查询是否已有关联信息
        int count = roleMapper.queryRoleRa(map);
        boolean update = count > 0;
        long time = System.currentTimeMillis();
        map.put("time", time);
        map.put("status", "1");
        if (update) {
            //修改
            result = roleMapper.updateRoleRelation(map);
        } else {
            //新增
            result = roleMapper.insertRoleRelation(map);
        }
        return result;
    }

    /**
     * 逻辑删除角色组关联表
     *
     * @param map
     */
    public int updateRoleRelation(Map<String, Object> map) {
        long time = System.currentTimeMillis();
        map.put("time", time);
        map.put("status", "2");
        return roleMapper.updateRoleRelation(map);
    }

    /**
     * 保存角色信息
     *
     * @param map
     * @return
     */
    public Map<String, Object> addCompanyRole(Map<String, Object> map) {
        String cid = JzbDataType.getString(map.get("cid"));
        String crId = JzbDataType.getString(map.get("crid"));
        if (JzbTools.isEmpty(crId)) {
            crId = JzbRandom.getRandomCharCap(13);
            map.put("crid", crId);
        }
        map.put("addtime", System.currentTimeMillis());
        map.put("status", "1");
        List<Map<String, Object>> crList = roleMapper.queryCompanyRole(map);
        Map<String, Object> result = new HashMap();
        if (crList.size() > 0) {
            result.put("code", "2");
            result.put("add", "0");
        } else {
            int add = roleMapper.insertCompanyRole(map);
            result.put("add", add);
            result.put("crid", crId);
            result.put("code", "1");
        }

        return result;
    }

    /**
     * 修改角色信息
     *
     * @param map
     * @return
     */
    public Map<String, Object> updateCompanyRole(Map<String, Object> map) {
        map.put("updtime", System.currentTimeMillis());
        List<Map<String, Object>> crList = roleMapper.queryCompanyRole(map);
        Map<String, Object> result = new HashMap();
        if (crList.size() > 0) {
            result.put("code", "2");
            result.put("update", "0");
        } else {
            int update = roleMapper.updateCompanyRole(map);
            result.put("code", "1");
            result.put("update", update);
        }
        return result;
    }

    /**
     * 根据企业id查询角色总数
     *
     * @param map
     * @return
     */
    public int queryCompanyRoleCount(Map<String, Object> map) {
        return roleMapper.queryCompanyRoleCount(map);
    }

    /**
     * 根据企业id查询角色分页信息
     *
     * @param map
     * @return
     */
    public List<LinkedHashMap> queryCompanyRoleList(Map<String, Object> map) {
        return roleMapper.queryCompanyRoleList(map);
    }

    /**
     * 删除角色
     *
     * @param map
     * @return
     */
    public int deleteCompanyRole(Map<String, Object> map) {
        map.put("updtime", System.currentTimeMillis());
        map.put("type", "delete");
        map.put("status", "4");
        return roleMapper.updateCompanyRole(map);
    }

    /**
     * 根据用户部门信息和用户id获取用户角色组信息
     *
     * @param list
     * @return
     */
    public List<LinkedHashMap<String, Object>> getUserDeptGroup(Map<String, Object> list) {
        return roleMapper.queryUserDeptGroup(list);
    }

    /**
     * 用户角色表总数
     *
     * @param map
     * @return
     */
    public int queryUserRoleCount(Map<String, Object> map) {
        return roleMapper.queryUserRoleCount(map);
    }

    /**
     * 用户角色表list
     *
     * @param map
     * @return
     */
    public List<LinkedHashMap> queryUserRoleList(Map<String, Object> map) {
        return roleMapper.queryUserRoleList(map);
    }

    /**
     * 批量添加用户角色
     *
     * @param map
     * @return
     */
    public int insertUserRole(Map<String, Object> map) {
        int result = 0;
        map.put("status", "1");
        List<Map<String, Object>> userList = toList(map);
        if (userList.size() > 0) {
            result = roleMapper.insertUserRole(userList);
        }
        return result;
    }

    /**
     * 批量修改用户角色表
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/3 11:05
     */
    public int updateUserRole(Map<String, Object> map) {
        int result = 0;
        map.put("status", "2");
        List<Map<String, Object>> userList = toList(map);
        if (userList.size() > 0) {
            result = roleMapper.updateUserRole(userList);
        }
        return result;
    }

    /**
     * 循环获取UID
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/3 11:08
     */
    private List<Map<String, Object>> toList(Map<String, Object> map) {
        List<Map<String, Object>> userList = new ArrayList<>();
        //验证是否list
        boolean isList = JzbDataType.isCollection(map.get("list"));
        if (isList) {
            List<Map<String, Object>> uidList = (List<Map<String, Object>>) map.get("list");
            if (uidList != null && uidList.size() > 0) {
                for (int i = 0, s = uidList.size(); i < s; i++) {
                    Map<String, Object> uid = uidList.get(i);
                    Map<String, Object> userMap = new HashMap<>(6);
                    userMap.put("uid", uid.get("uid"));
                    userMap.put("cid", map.get("cid"));
                    userMap.put("ouid", map.get("uid"));
                    userMap.put("crid", map.get("crid"));
                    userMap.put("addtime", System.currentTimeMillis());
                    userMap.put("updtime", System.currentTimeMillis());
                    userMap.put("status", map.get("status"));
                    userList.add(userMap);
                }
            }
        }
        return userList;
    }
    /**
     * 查询计支宝企业内 文档管理 角色
     * 用户 给 体系建设 内超级管理员 权限
     * 查询当前用户 是否有超级权限
     * @param param
     * @Author: lifei
     * @DateTime: 2019/12/19 14:07
     */
    public int  getDocMsgPower(Map<String, Object> param){return roleMapper.getDocMsgPower(param);}
    @Override
    public String value() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
