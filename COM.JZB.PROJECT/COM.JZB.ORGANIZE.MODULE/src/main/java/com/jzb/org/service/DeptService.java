package com.jzb.org.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.tree.JzbTree;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.AuthApi;
import com.jzb.org.api.message.MessageApi;
import com.jzb.org.api.redis.OrgRedisServiceApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.dao.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/13 19:28
 */
@Service
public class DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;
    @Autowired
    private OrgRedisServiceApi orgRedisServiceApi;

    @Autowired
    private AuthApi authApi;
    @Autowired
    private CompanyService service;
    /**
     * 配置文件方法
     */
    @Autowired
    private OrgConfigProperties config;

    /**
     * 通过根据企业id获取部门信息
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDeptListByCid(Map<String, Object> map) {
        return deptMapper.queryDeptListByCid(map);
    }

    /**
     * 根据企业id和产品名称获取相似名称的有效产品
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getProductListByName(Map<String, Object> map) {
        return deptMapper.searchProListByName(map);
    }

    /**
     * 获取企业下所有产品线
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/6 10:31
     */
    public List<Map<String, Object>> searchProLine(Map<String, Object> map) {
        return deptMapper.queryProLine(map);
    }

    /**
     * 获取产品的菜单权限
     *
     * @return
     */
    public JSONArray getProductMenu(Map<String, Object> map) {
        JSONArray jsonArray;
        List<Map<String, Object>> result = new LinkedList<>();
        String pid = JzbDataType.getString(map.get("pid"));
        //从redis获取树
        Response response = orgRedisServiceApi.getProductTree(map);
        if (JzbDataType.isMap(response.getResponseEntity())) {
            Map<String, Object> redis = (Map<String, Object>) response.getResponseEntity();
            jsonArray = JSONArray.parseArray(JzbDataType.getString(redis.get(pid)));
        } else {
            List<Map<String, Object>> pageList = deptMapper.queryProductMenu(map);
            boolean dispose = pageList.size() > 0;
            // root id
            String firstParent = "000000000000000";
            if (dispose) {
                //第一步先将pageId放入pageList中,合并相同mid的数据
                //第一次合并控件id和名称
                //合并的字段
                String me1 = "controlid,conname";
                Map<Integer, String> merge1 = JzbTree.toMap(me1);
                //主干的字段
                String ma1 = "pid,mid,parentid,cname,pageid,pagename";
                Map<Integer, String> main1 = JzbTree.toMap(ma1);
                String id1 = "pageid";
                String listName1 = "conList";
                List<Map<String, Object>> conList = JzbTree.toSame(pageList, main1, id1, merge1, listName1);

                //第二次合并页面id和名称
                //合并的字段
                String me = "pageid,pagename,conList";
                Map<Integer, String> merge = JzbTree.toMap(me);
                //主干的字段
                String ma = "pid,mid,parentid,cname";
                Map<Integer, String> main = JzbTree.toMap(ma);
                String id = "mid";
                String listName = "pageList";
                List<Map<String, Object>> midList = JzbTree.toSame(conList, main, id, merge, listName);
                String parentId = "parentid";
                result = JzbTree.getTreeMap(midList, id, parentId, firstParent);
                Map<String, Object> redis = new HashMap<>(2);
                redis.put("pid", pid);
                redis.put("list", result);
                //将树保存进redis
                orgRedisServiceApi.cacheProductTree(redis);
            }
            jsonArray = JSONArray.parseArray(JSON.toJSONString(result));
        }
        return jsonArray;
    }

    /**
     * 获取部门下所有子级的用户包括自身
     *
     * @param map
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     * @DateTime: 2019/9/4 11:45
     */
    public List<Map<String, Object>> queryDeptUserChildList(Map<String, Object> map) {
        List<Map<String, Object>> result = new LinkedList<>();
        String firstParent = JzbDataType.getString(map.get("pcdid"));
        if (!JzbTools.isEmpty(firstParent)) {
            List<Map<String, Object>> uList = deptMapper.queryDeptUserChildList(map);
            boolean dispose = uList.size() > 0;
            if (dispose) {
                //合并的字段
                String me = "uid,uname";
                Map<Integer, String> merge = JzbTree.toMap(me);
                //主干的字段
                String ma = "pcdid,cname,cdid,cidx,summary";
                Map<Integer, String> main = JzbTree.toMap(ma);
                String id = "cdid";
                String listName = "list";
                List<Map<String, Object>> midList = JzbTree.toSame(uList, main, id, merge, listName);
                String pid = "pcdid";
                result = JzbTree.getTreeMap(midList, id, pid, firstParent);
            }
        }
        return result;
    }


    /**
     * 获取用户信息和用户部门信息总数
     *
     * @param map
     * @return
     */
    public int queryDeptByCnameCount(Map<String, Object> map) {
        return deptMapper.queryDeptByCnameCount(map);
    }

    /**
     * 获取用户信息和用户部门信息
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryDeptByCname(Map<String, Object> map) {
        List<Map<String, Object>> result = new ArrayList<>();
        map.put("size", config.getRowSize());
        //第一步先根据姓名查询出相似用户部门信息
        if (JzbDataType.isCollection(map.get("list"))) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
            map.put("list", list);
        }
        List<Map<String, Object>> userDList = deptMapper.queryDeptByCname(map);
        //第二步查redis中的用户数据
        if (userDList.size() > 0) {
            for (int i = 0, s = userDList.size(); i < s; i++) {
                Map<String, Object> userMap = userDList.get(i);
                Response redis = userRedisServiceApi.getCacheUserInfo(userMap);
                if (JzbDataType.isMap(redis.getResponseEntity())) {
                    Map infoMap = (Map) redis.getResponseEntity();
                    userMap.put("relphone", infoMap.get("relphone"));
                    userMap.put("regphone", infoMap.get("regphone"));
                }
                result.add(userMap);
            }
        }
        return result;
    }


    /**
     * 新建部门（组织架构）
     *
     * @param map
     * @return
     */
    public Map<String, Object> insertCompanyDept(Map<String, Object> map) {
        int a = JzbDataType.getInteger(JzbRandom.getRandomNum(1));
        if (a == 0) {
            a++;
        }
        //值为cid + ‘0000’,保留部门ID为0000-0999的部门ID
        String cdId = JzbDataType.getString(map.get("cid")) + a + JzbRandom.getRandomNum(3);
        map.put("cdid", cdId);
        map.put("addtime", System.currentTimeMillis());
        map.put("status", "1");
        map.put("index", JzbDataType.getInteger(map.get("index")));
        int add = deptMapper.insertCompanyDept(map);
        Map<String, Object> result = new HashMap<>(2);
        result.put("add", add);
        result.put("cdid", cdId);
        return result;
    }

    /**
     * 修改部门信息
     *
     * @param map
     * @return
     */
    public int updateCompanyDept(Map<String, Object> map) {
        map.put("updtime", System.currentTimeMillis());
        map.put("index", JzbDataType.getInteger(map.get("index")));
        return deptMapper.updateCompanyDept(map);
    }

    /**
     * -删除部门信息
     *
     * @param map
     */
    public void deleteCompanyDept(Map<String, Object> map) {
        map.put("time", System.currentTimeMillis());
        deptMapper.deleteCompanyDept(map);
    }

    /**
     * 根据用户id获取用户部门信息和在职状态总数
     *
     * @param map
     * @return
     */
    public int queryDeptUserCount(Map<String, Object> map) {
        return deptMapper.queryDeptUserCount(map);
    }

    /**
     * 根据用户姓名或手机号获取用户部门信息和在职状态
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryDeptUser(Map<String, Object> map) {
        return deptMapper.queryDeptUser(map);
    }

    /**
     * 将本公司员工添加到部门中
     *
     * @param map
     * @return
     */
    public int addDeptUser(Map<String, Object> map) {
        int result = 0;
        int isSel = deptMapper.queryDeptUserIF(map);
        map.put("time", System.currentTimeMillis());
        if (isSel > 0) {
            //修改
            result = deptMapper.updateDeptUser(map);
        } else {
            //新增
            result = deptMapper.insertDeptUser(map);
        }
        return result;
    }

    /**
     * 激活账户，加入邀请部门
     *
     * @param map
     * @return void
     * @Author: DingSC
     * @DateTime: 2019/10/10 10:32
     */
    public void enabledUser(Map<String, Object> map) {
        //查询出用户所有邀请信息
        List<Map<String, Object>> inviteList = null;
        String temp = "";
        if (inviteList != null && inviteList.size() > 0) {

        }

    }

    /**
     * 将用户加入资源池
     *
     * @param map
     * @return void
     * @Author: DingSC
     * @DateTime: 2019/10/10 10:43
     */
    public void addPool(Map<String, Object> map) {

    }

    /**
     * 修改部门员工表
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/3 16:18
     */
    public int updateDeptUser(Map<String, Object> map) {
        int result = 0;
        map.put("time", System.currentTimeMillis());
        //修改
        result = deptMapper.updateDeptUser(map);
        return result;
    }

    /**
     * 保存用户导入批次表
     *
     * @param map
     * @return
     */
    public int addExportBatch(Map<String, Object> map) {
        map.put("addtime", System.currentTimeMillis());
        return deptMapper.insertExportBatch(map);
    }

    /**
     * 修改用户导入批次表
     *
     * @param map
     * @return
     */
    public int updateExportBatch(Map<String, Object> map) {
        map.put("endtime", System.currentTimeMillis());
        return deptMapper.updateExportBatch(map);
    }

    /**
     * 导入主操作
     *
     * @param map
     * @return
     */
    public int addExportUserInfo(Map<String, Object> map) {
        int result = 1;
        String filePath = JzbDataType.getString(map.get("address"));
        try {
            boolean isExcel = JzbExcelOperater.isExcel(filePath);
            if (!isExcel) {
                throw new Exception("File is not found.");
            }
            //sheet的页面
            int index = 0;
            // 打开文件
            List<Map<Integer, String>> data = JzbExcelOperater.readSheet(filePath, index);
            int size = data.size();
            //保存到用户导入信息表
            List<Map<String, Object>> userInfoList = new ArrayList<>(size);
            //保存成员邀请/申请表
            List<Map<String, Object>> inviteUserList = new ArrayList<>(size);
            //直接保存到部门
            List<Map<String, Object>> deptUserList = new ArrayList<>(size);
            //读取数据，从第二行开始读取
            for (int i = 1; i < size; i++) {
                Map<Integer, String> rowData = data.get(i);
                String name = rowData.get(1);
                String sex = rowData.get(2);
                String phone = rowData.get(3);
                //用户导入信息表状态
                String status = "1";
                String summary;
                //部门id
                String cdid;
                //受邀用户id
                String uid;
                //invite表状态
                String ustatus;
                String inviteStatus = "2";
                //行数据转入Map
                Map<String, Object> addMap = rowToMap(rowData);
                addMap.put("cid", map.get("cid"));
                addMap.put("batchid", map.get("batchid"));
                summary = JzbDataType.getString(addMap.get("summary"));
                //字段校验不通过
                if (JzbDataType.getInteger(addMap.get(status)) == 2) {
                    userInfoList.add(addMap);
                    continue;
                }
                //校验必填项
                if (name.length() != 0 && sex.length() != 0 && phone.length() != 0) {
                    //根据手机号查询用户id
                    uid = JzbDataType.getString(userRedisServiceApi.getPhoneUid(phone).getResponseEntity());
                    addMap.put("uid", uid);
                    //导入excel中的部门名称
                    map.put("cname", addMap.get("dept"));
                    //获取部门id
                    cdid = getCdId(map);
                    if (JzbTools.isEmpty(cdid)) {
                        status = "2";
                        summary += "输入的部门与选定的部门不一致";
                        addMap.put("status", status);
                        addMap.put("summary", summary);
                        userInfoList.add(addMap);
                        continue;
                    }
                    if (JzbTools.isEmpty(uid)) {
                        //非用户
                        ustatus = "2";
                        summary += "系统中不存在该用户";
                        //创建用户
                        Map<String, Object> userMap = new HashMap<>(4);
                        userMap.put("phone", addMap.get("phone"));
                        userMap.put("name", name);
                        String pass = "*jzb" + JzbRandom.getRandomNum(3);
                        userMap.put("passwd", JzbDataCheck.Md5(pass).toLowerCase(Locale.ENGLISH));
                        userMap.put("status", "8");
                        try {
                            Response userRes = authApi.addRegistration(userMap);
                            Map<String, Object> uidMap = (Map<String, Object>) userRes.getResponseEntity();
                            //加入邀请
                            String newUid = JzbDataType.getString(uidMap.get("uid"));
                            if (JzbTools.isEmpty(newUid)) {
                                status = "2";
                                summary += ",创建用户失败";
                            } else {
                                addMap.put("uid", uidMap.get("newUid"));
                                addMap.put("password", pass);
                            }
                        } catch (Exception e) {
                            JzbTools.logError(e);
                            status = "2";
                            summary += ",创建用户失败";
                        }
                    } else {
                        //用户
                        ustatus = "1";
                        Map<String, Object> deptUserMap = toDeptUser(addMap, map, cdid);
                        //手机号所对应的用户姓名和输入的姓名不一致
                        if (deptUserMap.size() == 0) {
                            userInfoList.add(addMap);
                            continue;
                        } else {
                            //存在公司中
                            if (JzbDataType.getInteger(deptUserMap.get("inviteStatus")) == 10) {
                                inviteStatus = JzbDataType.getString(deptUserMap.get("inviteStatus"));
                                boolean isAdd = JzbDataType.getBoolean(deptUserMap.get("isAdd"));
                                //不在指定部门则添加
                                if (isAdd) {
                                    deptUserList.add(deptUserMap);
                                }
                            }
                        }
                    }
                    if (JzbDataType.getInteger(status) == 1) {
                        //邀请Map获取
                        Map<String, Object> invite = toInvite(map, addMap, cdid, ustatus, inviteStatus);
                        inviteUserList.add(invite);
                    }
                } else {
                    status = "2";
                    summary += "必填项未填";
                    //整行数据为空，跳过
                    if (isNull(rowData)) {
                        continue;
                    }
                }
                //最后添加到用户导入信息表
                addMap.put("status", status);
                addMap.put("summary", summary);
                userInfoList.add(addMap);
            }
            toAddList(userInfoList, inviteUserList, deptUserList, map);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = 2;
        }
        return result;
    }

    /**
     * 判断整行数据是否为空
     *
     * @param rowData
     * @return
     */
    private boolean isNull(Map<Integer, String> rowData) {
        boolean result = true;
        int size = rowData.size();
        for (int i = 0; i < size; i++) {
            if (!JzbTools.isEmpty(rowData.get(i))) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 保存数据和发送短信
     *
     * @param userInfoList
     * @param inviteUserList
     * @param deptUserList
     */
    private void toAddList(List<Map<String, Object>> userInfoList, List<Map<String, Object>> inviteUserList,
                           List<Map<String, Object>> deptUserList, Map<String, Object> map) {
        //保存用户导入信息表
        if (userInfoList.size() > 0) {
            deptMapper.insertExportUserInfoList(userInfoList);
        }
        //保存成员邀请/申请表
        if (inviteUserList.size() > 0) {
            deptMapper.insertInviteUserList(inviteUserList);
        }
        //保存部门员工表
        if (deptUserList.size() > 0) {
            deptMapper.insertDeptUserList(deptUserList);
        }
        //发送短信
        int size = inviteUserList.size();
        //模板
        String template = config.getTemplate();
        String inviteTem = config.getInvite();
        String company;
        company = deptMapper.getCompanyName(map);
        for (int i = 0; i < size; i++) {
            Map<String, Object> invite = inviteUserList.get(i);
            String phone = JzbDataType.getString(invite.get("resphone"));
            int uStatus = JzbDataType.getInteger(invite.get("ustatus"));
            Map<String, Object> param = new HashMap<>(5);
            if (uStatus == 1) {
                //用户
                param.put("relphone", phone);
                param.put("groupid", template);
            } else {
                //非用户
                param.put("relphone", phone);
                param.put("groupid", inviteTem);
                param.put("companyname", company);
                param.put("username", invite.get("cname"));
                param.put("password", invite.get("password"));
            }
            //发送短信
            // service.sendRemind(param);
        }
    }

    /**
     * 获取邀请表的map
     *
     * @param map
     * @param addMap
     * @param cdid
     * @param ustatus
     * @param inviteStatus
     * @return
     */
    private Map<String, Object> toInvite(Map<String, Object> map, Map<String, Object> addMap, String cdid, String ustatus, String inviteStatus) {
        Map<String, Object> invite = new HashMap<>();
        invite.put("reqtype", "2");
        invite.put("batchid", map.get("batchid"));
        invite.put("cid", map.get("cid"));
        invite.put("uid", map.get("uid"));
        invite.put("reqtime", System.currentTimeMillis());
        invite.put("cdid", cdid);
        invite.put("ustatus", ustatus);
        //受邀请人id
        invite.put("resuid", addMap.get("uid"));
        invite.put("resphone", addMap.get("phone"));
        invite.put("cname", addMap.get("cname"));
        invite.put("status", JzbDataType.getInteger(inviteStatus));
        return invite;
    }

    /**
     * 获取添加到部门的map
     *
     * @param addMap
     * @param map
     * @param cdid
     * @return
     */
    private Map<String, Object> toDeptUser(Map<String, Object> addMap, Map<String, Object> map, String cdid) {
        String uid = JzbDataType.getString(addMap.get("uid"));
        Response user = userRedisServiceApi.getCacheUserInfo(addMap);
        Map<String, Object> result = new HashMap<>(8);
        if (JzbDataType.isMap(user.getResponseEntity())) {
            Map<String, Object> userMap = (Map) user.getResponseEntity();
            String cname = JzbDataType.getString(userMap.get("cname"));
            //校验手机号所对应的用户姓名和输入的姓名
            if (!cname.equals(JzbDataType.getString(addMap.get("cname")))) {
                addMap.put("status", "2");
                addMap.put("summary", "手机号所对应的用户姓名和输入的姓名不一致");
                return result;
            }
        } else {
            addMap.put("status", "2");
            addMap.put("summary", "缓存中没有该用户信息");
            return result;
        }
        //判断用户是否属于该企业
        Map<String, Object> deptMap = new HashMap<>(4);
        deptMap.put("cid", map.get("cid"));
        deptMap.put("uids", uid);
        deptMap.put("status", "1");
        List<Map<String, Object>> deptCount = deptMapper.queryDeptUser(deptMap);
        int d = deptCount.size();
        //属于企业的员工
        if (d > 0) {
            result.put("inviteStatus", "10");
            boolean isAdd = true;
            for (int j = 0; j < d; j++) {
                //在指定部门
                if (cdid.equals(JzbDataType.getString(deptCount.get(j).get("cdid")))) {
                    isAdd = false;
                    break;
                }
            }
            //直接添加到指定部门
            if (isAdd) {
                result.put("cid", map.get("cid"));
                result.put("cdid", cdid);
                result.put("uid", uid);
                result.put("cname", addMap.get("cname"));
                result.put("mail", addMap.get("mail"));
                result.put("addtime", System.currentTimeMillis());
                result.put("status", "1");
            }
            result.put("isAdd", isAdd);
        } else {
            result.put("inviteStatus", "2");
        }
        return result;
    }

    /**
     * 获取用户部门id
     *
     * @param map
     * @return
     */
    private String getCdId(Map<String, Object> map) {
        Map<String, Object> deptMap = deptMapper.getDeptByIdAndName(map);
        String cdId = "";
        if (deptMap != null) {
            cdId = JzbDataType.getString(deptMap.get("cdid"));
        }
        String selectCdId = JzbDataType.getString(map.get("cdid"));
        if (JzbTools.isEmpty(selectCdId)) {
            //导入时没有选择部门
            if (JzbTools.isEmpty(cdId)) {
                //企业资源池
                cdId = JzbDataType.getString(map.get("cid")) + "0000";
            }
        } else {
            //导入时选择了部门
            if (JzbTools.isEmpty(cdId)) {
                //输入的部门不在企业范围内，空为没有输入
                if (JzbTools.isEmpty(map.get("cname"))) {
                    //取选择的部门id
                    cdId = JzbDataType.getString(map.get("cdid"));
                } else {
                    //选择部门和输入部门不一致
                    cdId = "";
                }

            } else {
                //选择部门和输入部门不一致
                if (!selectCdId.equals(cdId)) {
                    cdId = "";
                }
            }
        }
        return cdId;
    }

    /**
     * 校验手机号
     *
     * @param obj
     * @return
     */
    private boolean toPhone(String obj) {
        boolean result = true;
        try {
            String eg = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
            result = Pattern.matches(eg, obj);
        } catch (Exception e) {
            JzbTools.logError(e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断性别
     *
     * @param str
     * @return
     */
    private String toSex(String str) {
        String sex = "";
        String man = "男";
        String women = "女";
        if (!JzbTools.isEmpty(str)) {
            if (man.equals(str)) {
                sex = "1";
            } else if (women.equals(str)) {
                sex = "2";
            }
        }
        return sex;
    }

    /**
     * 将行数据储存到map中，并校验数据是否符合规范
     */
    private Map<String, Object> rowToMap(Map<Integer, String> rowData) {
        Map<String, Object> result = new HashMap<>();
        String status = "1";
        String summary = "";
        String sex = rowData.get(2);
        String phone = rowData.get(3);
        result.put("cname", rowData.get(1));
        //校验性别
        sex = toSex(sex);
        result.put("sex", sex);
        //为空就是校验不通过
        if (JzbTools.isEmpty(sex)) {
            status = "2";
            summary += "性别校验不通过";
        }
        //校验手机号
        if (!toPhone(phone)) {
            status = "2";
            summary += "手机号不合规范";
        }
        result.put("phone", phone);
        if (JzbTools.isEmpty(rowData.get(0))) {
            //排序
            result.put("idx", 0);
        } else {
            result.put("idx", JzbDataType.getInteger(rowData.get(0)));
        }
        if (rowData.get(4).length() > 18) {
            status = "2";
            summary += "身份证号字符长度过长,";
        } else {
            //身份证号
            result.put("cardid", rowData.get(4));
        }
        if (rowData.get(5).length() > 32) {
            status = "2";
            summary += "部门字符长度过长,";
        } else {
            //部门
            result.put("dept", rowData.get(5));
        }
        if (rowData.get(6).length() > 64) {
            status = "2";
            summary += "邮箱字符长度过长,";
        } else {
            //邮箱
            result.put("mail", rowData.get(6));
        }
        if (rowData.get(7).length() > 32) {
            status = "2";
            summary += "紧急联系人字符长度过长,";
        } else {
            //紧急联系人
            result.put("relperson", rowData.get(7));
        }
        if (rowData.get(8).length() > 20) {
            status = "2";
            summary += "联系人字符长度过长,";
        } else {
            //联系人
            result.put("relphone", rowData.get(8));
        }
        result.put("status", status);
        result.put("summary", summary);
        return result;
    }

    /**
     * 获取企业下属用户总数
     *
     * @param map
     * @return
     */
    public int queryDeptUserListCount(Map<String, Object> map) {
        return deptMapper.queryDeptUserListCount(map);
    }

    /**
     * 获取企业下属用户数据
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryDeptUserList(Map<String, Object> map) {
        return deptMapper.queryDeptUserList(map);
    }

    /**
     * 调整用户部门
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/4 16:36
     */
    public int updateDept(Map<String, Object> map) {
        int result;
        //先修改原先数据状态
        map.put("status", "4");
        map.put("time", System.currentTimeMillis());
        int up = deptMapper.updateDeptUser(map);
        //新增到新部门
        result = deptMapper.insertAllDeptUser(map);
        if (result != up) {
            result = 0;
        }
        return result;
    }
}
