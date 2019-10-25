package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.query.QueryPageConfig;
import com.jzb.base.tree.JzbTree;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.AuthApi;
import com.jzb.org.api.auth.AuthRoleApi;
import com.jzb.org.api.redis.OrgRedisServiceApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.controller.CompanyController;
import com.jzb.org.controller.CompanyUserController;
import com.jzb.org.dao.DeptMapper;
import com.jzb.org.dao.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description: 产品实现类
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/13 9:46
 */
@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private AuthApi authApi;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private AuthRoleApi authRoleApi;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private OrgRedisServiceApi orgRedisServiceApi;
    @Autowired
    private DeptMapper deptMapper;

    /**
     * 根据产品查询产品下的所有菜单
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getMenuDate(Map<String, Object> param) {
        return productMapper.queryProductMenuList(param);
    }

    /**
     * 分页查询产品信息
     *
     * @param param 用 kv 存储
     * @return List<Map < String, Object>> 返回数据
     */
    public List<Map<String, Object>> getProductList(Map<String, Object> param) {
        // 调用分页查询方法
//        QueryPageConfig.setPageSize(param);
        JzbPageConvert.setPageRows(param);

        // 查询数据
        return productMapper.queryProductList(param);
    } // End getProductList

    /**
     * 分页查询产品信息
     *
     * @param param 用 kv 存储
     * @return List<Map < String, Object>> 返回数据
     */
    public int getProductTotal(Map<String, Object> param) {
        return productMapper.queryProductTotal(param);
    } // End getProductTotal

    /**
     * 模板导入新建菜单或子级
     *
     * @author kuangbin
     */
    public int addProductMenu(List<Map<String, Object>> param) {
        int count;
        try {
            // 加入菜单表
            count = productMapper.insertProductMenu(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 从模板中获取产品信息,加入数据库中
     *
     * @param productMap 从菜单模板中查询出来的产品信息
     * @param size       菜单模板中有多少列
     * @param cid        企业ID
     * @return
     */
    public Map<String, Object> getProductParam(Map<Integer, String> productMap, int size, String cid) {
        String pid;
        // 存放产品参数
        Map<String, Object> param = new HashMap();

        // 零时存放产品参数
        Map<Integer, String> productParam = new HashMap();

        // 定义零时存放的参数位置
        int k = 0;

        // 获取产品名称
        String name = productMap.get(0);
        if (!JzbDataType.isEmpty(name)) {
            // 遍历产品参数放置临时map中
            for (int i = 0; i < size; i++) {
                if (productMap.get(i) != null) {
                    productParam.put(k, productMap.get(i));
                    k++;
                }
            }
            // 获取产品用户端URL
            String weburl = productParam.get(1);

            // 获取产品管理端URL
            String manurl = productParam.get(2);

            // 获取产品线ID
            int plid = JzbDataType.getInteger(productParam.get(3));

            // 获取产品备注
            String psummary = productParam.get(4);

            // 加入参数
            param.put("cname", name);
            param.put("weburl", weburl);
            param.put("manurl", manurl);
            param.put("plid", plid);
            param.put("summary", psummary);
            param.put("logo", "");
            param.put("ptype", "1");
            // 获取产品ID
            pid = JzbRandom.getRandomCharCap(11);
            param.put("pid", pid);
            // 加入企业ID
            param.put("cid", cid);
        } else {
            param.put("pid", "");
        }
        return param;
    }

    /**
     * 模板导入新建菜单或子级
     *
     * @author kuangbin
     */
    public int addProduct(Map<String, Object> param) {
        int count;
        try {
            // 加入产品表
            count = productLineService.addCompanyProduct(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 根据查询出来的页面所有信息加入数据库
     *
     * @author kuangbin
     */
    public int addProductPage(List<Map<String, Object>> pageList) {
        return productMapper.insertProductPage(pageList);
    }

    /**
     * 根据产品查询产品下的所有页面
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getPageDate(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");
        return productMapper.queryProductPageList(param);
    }

    /**
     * 根据查询出来的控件所有信息加入数据库
     *
     * @author kuangbin
     */
    public int addPageControl(List<Map<String, Object>> pageList) {
        int count;
        try {
            // 根据查询出来的控件所有信息加入页面控件表
            count = productMapper.insertPageControlList(pageList);
            if (count > 0) {
                // 根据查询出来的控件所有信息加入控件API表
                count = productMapper.insertControlPowerList(pageList);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 新建单位和用户
     *
     * @author kuangbin
     */
    public Response addRegistrationCompany(Map<String, Object> param) {
        Response result;
        try {
            Object redisRes = userRedisServiceApi.getPhoneUid(JzbDataType.getString(param.get("phone"))).getResponseEntity();
            param.put("uid", JzbDataType.getString(redisRes));
            if (JzbTools.isEmpty(redisRes)) {
                // 获取随机密码
                String passwd = "*jzb" + JzbRandom.getRandomNum(3);
                param.put("passwd", JzbDataCheck.Md5(passwd).toLowerCase(Locale.ENGLISH));
                param.put("password", passwd);
                param.put("status", "8");
                // 创建用户返回用户UID
                result = authApi.addRegistration(param);
                Object objUser = result.getResponseEntity();
                // 判断返回的是否是MAP
                if (JzbDataType.isMap(objUser)) {
                    Map<String, Object> map = (Map<String, Object>) objUser;
                    // 判断map中是否包含uid
                    if (!JzbDataType.isEmpty(JzbDataType.getString(map.get("uid")))) {
                        param.put("uid", JzbDataType.getString(map.get("uid")));
                    }
                }
            }
            if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("uid")))) {
                // 加入状态,1为创建单位
                param.put("type", "1");
                // 单位默认初级认证
                param.put("authid", "8");
                param.put("manager", JzbDataType.getString(param.get("uid")));
                result = addCompany(param);
                Object objCompany = result.getResponseEntity();
                // 判断返回的是否是MAP
                if (JzbDataType.isMap(objCompany)) {
                    Map<String, Object> mapCompany = (Map<String, Object>) objCompany;
                    // 判断map中是否包含cid
                    if (!(JzbDataType.getInteger(mapCompany.get("message")) == 4)) {
                        String cid = JzbDataType.getString(mapCompany.get("cid"));
                        param.put("cid", cid);
                        // 加入公海单位
                        Response send = companyController.addCompanyCommon(param);
                        result = Response.getResponseSuccess();
                        // 获取短信接口返回值并加入到此接口返回值中
                        result.setResponseEntity(send.getResponseEntity());
                    } else {
                        result.setResponseEntity("此单位已存在!");
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (
                Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 创建单位 & 加入单位
     * 参数type为1创建单位，2加入单位
     *
     * @Author:KuangBin
     * @DateTime: 2019/9/19 9:08
     */
    public Response addCompany(Map<String, Object> param) {
        Response result;
        int type = Integer.parseInt(param.get("type").toString());
        //type的种类
        int found = 1;
        result = companyController.addCompany(param);
        if (type == found) {
            //创建单位类型，创建默认角色
            Map<String, Object> cidMap = (Map<String, Object>) result.getResponseEntity();
            //创建默认角色
            String cid = JzbDataType.getString(cidMap.get("cid"));
            String crId = cid + "000000";
            param.put("cid", cid);
            param.put("crid", crId);
            param.put("cname", "默认角色");
            param.put("status", "1");
            param.put("addtime", System.currentTimeMillis());
            authRoleApi.addCompanyRole(param);
            //将用户加入默认角色中
            List<Map<String, Object>> list = new ArrayList<>(1);
            Map<String, Object> uidMap = new HashMap<>(2);
            uidMap.put("uid", param.get("uid"));
            list.add(uidMap);
            param.put("list", list);
            authRoleApi.addUserRole(param);
            //创建默认角色组，资源池角色组合管理员角色组
            saveInitRoleGroup(param);

        }
        return result;
    }

    /**
     * 创建默认角色组，资源池角色组合管理员角色组
     *
     * @param param
     */
    private void saveInitRoleGroup(Map<String, Object> param) {
        //创建默认角色组，资源池角色组合管理员角色组
        String cid = JzbDataType.getString(param.get("cid"));
        param.put("cname", "资源池角色组");
        param.put("crgid", cid + "0000");
        authRoleApi.saveRoleGroup(param);
        param.put("cname", "管理员角色组");
        param.put("crgid", cid + "1111");
        authRoleApi.saveRoleGroup(param);
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
                String listName = "children";
                List<Map<String, Object>> midList = JzbTree.toSame(conList, main, id, merge, listName);
                String parentId = "parentid";
                //处理页面和控件
                midList = getPageAndCon(midList);
                //生成树
                id = "id";
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

    private List<Map<String, Object>> getPageAndCon(List<Map<String, Object>> midList) {
        int size = JzbDataType.getInteger(midList.size());
        List<Map<String, Object>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Map<String, Object> midMap = midList.get(i);
            Map<String, Object> map = new HashMap<>(6);
            //处理页面的list
            if (JzbDataType.isCollection(midMap.get("children"))) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) midMap.get("children");
                int listSize = JzbDataType.getInteger(list.size());
                List<Map<String, Object>> tempList = new ArrayList<>(listSize);
                //处理list中数据
                for (int j = 0; j < listSize; j++) {
                    Map<String, Object> pageTemp = list.get(j);
                    Map<String, Object> pageMap = new HashMap<>(3);
                    pageMap.put("status", "1");
                    pageMap.put("id", pageTemp.get("pageid"));
                    pageMap.put("cname", pageTemp.get("pagename"));
                    //处理控件的list
                    if (JzbDataType.isCollection(pageTemp.get("conList"))) {
                        List<Map<String, Object>> conListTemp = (List<Map<String, Object>>) pageTemp.get("conList");
                        int conSize = JzbDataType.getInteger(conListTemp.size());
                        List<Map<String, Object>> conList = new ArrayList<>(conSize);
                        for (int k = 0; k < conSize; k++) {
                            Map<String, Object> conTemp = conListTemp.get(k);
                            Map<String, Object> conMap = new HashMap<>(3);
                            conMap.put("status", "2");
                            conMap.put("id", conTemp.get("controlid"));
                            conMap.put("cname", conTemp.get("conname"));
                            conList.add(conMap);
                        }
                        pageMap.put("children", conList);
                    }
                    tempList.add(pageMap);
                }
                map.put("children", tempList);
            }
            map.put("status", "0");
            map.put("id", midMap.get("mid"));
            map.put("pid", midMap.get("pid"));
            map.put("cname", midMap.get("cname"));
            map.put("parentid", midMap.get("parentid"));
            result.add(map);
        }
        return result;
    }
}


