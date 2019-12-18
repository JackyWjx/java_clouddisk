package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.entity.auth.CompanyInfo;
import com.jzb.base.entity.organize.DeptUser;
import com.jzb.base.entity.organize.RoleGroup;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.CompanyMapper;
import com.jzb.org.dao.ProductLineMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 企业服务类
 *
 * @author kuangbin
 */
@Service
public class ProductLineService {
    /**
     * 企业数据库操作对象
     */
    @Autowired
    private ProductLineMapper productLineMapper;

    /**
     * 显示产品线列表
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getProductLineList(Map<String, Object> param) {
        return productLineMapper.queryProductLineList(param);
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 记支宝电脑端下新建产品线
     *
     * @author kuangbin
     */
    public int addProductLine(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间的毫秒值
            long addtime = System.currentTimeMillis();
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            // 获取产品线的总数
            count = productLineMapper.queryProductLineCount(param);
            int plid;
            if (count >= 0 && count < 32) {
                // 产品线ID设为2的次方
                plid = JzbDataType.getInteger(Math.pow(2, count));
                param.put("plid", plid);
                count = productLineMapper.insertProductLine(param);
            } else {
                // 数据库已有32条数据则获取状态为2的产品ID
                List<Map<String, Object>> plidList = productLineMapper.queryProductLineId(param);
                if (plidList.size() != 0 && plidList != null) {
                    for (int i = 0; i < plidList.size(); i++) {
                        Map<String, Object> plidMap = plidList.get(i);
                        // 获取状态为2的第一个产品线ID
                        plid = JzbDataType.getInteger(plidMap.get("plid"));
                        param.put("plid", plid);
                        param.put("status", "1");
                        break;
                    }
                    // 修改状态为2的产品用作新的产品线
                    count = productLineMapper.updateProductLine(param);
                } else {
                    count = 0;
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 记支宝电脑端下修改,删除产品线
     *
     * @author kuangbin
     */
    public int modifyProductLine(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间的毫秒值
            long updtime = System.currentTimeMillis();

            // 防止sql注入问题
            param.put("plid", JzbDataType.getInteger(param.get("plid")));
            param.put("updtime", updtime);
            if (JzbDataType.getInteger(param.get("maybe")) == 1) {
                // 执行修改产品线操作
                count = productLineMapper.updateProductLine(param);
            } else {
                param.put("status", "2");
                // 执行删除产品线操作,修改产品线状态为2
                count = productLineMapper.updateProductLine(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面显示企业下所有产品的菜单列表
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyMenuList(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");

        // 1代表单产品,2代表产品包
        param.put("ptype", "1");
        return productLineMapper.queryCompanyMenuList(param);
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面显示企业下所有产品的顶级菜单下的页面
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyPageList(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");

        // 1代表单产品,2代表产品包
        param.put("ptype", "1");
        return productLineMapper.queryCompanyPageList(param);
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面显示企业下所有产品的顶级菜单下的页面
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyPageLists(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");

        // 1代表单产品,2代表产品包
        param.put("ptype", "1");
        return productLineMapper.queryCompanyPageLists(param);
    }

    /**
     * 电脑端-全界面-记支宝电脑端下全界面显示菜单下的所有页面
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getProductPageList(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");
        return productLineMapper.queryProductPageList(param);
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面新建同级页面
     *
     * @author kuangbin
     */
    public int addProductPage(Map<String, Object> param) {
        int count;
        try {

            // 获取当前时间
            long addtime = System.currentTimeMillis();
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            // 获取产品页面ID
            String pageid = JzbRandom.getRandomCharCap(17);
            param.put("pageid", pageid);
            param.put("status", "1");
            // 加入产品页面表
            count = productLineMapper.insertProductPage(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面新建同级页面
     *
     * @author kuangbin
     */
    public int addProductPages(Map<String, Object> param) {
        int count;
        try {

            // 获取当前时间
            long addtime = System.currentTimeMillis();
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            // 获取产品页面ID
            String pageid = JzbRandom.getRandomCharCap(17);
            param.put("pageid", pageid);
            param.put("status", "1");
            // 加入产品页面表
            count = productLineMapper.insertProductPage(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面修改页面或者删除页面
     *
     * @author kuangbin
     */
    public int modifyProductPage(Map<String, Object> param) {
        int count;
        try {
            int maybe = JzbDataType.getInteger(param.get("maybe"));
            // 获取当前时间
            long updtime = System.currentTimeMillis();
            param.put("updtime", updtime);
            // 1为修改操作,2为删除操作
            if (maybe == 2) {
                param.put("status", "2");
            }
            count = productLineMapper.updateProductPage(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面根据页面名称模糊搜索页面
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> searchProductPage(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");

        // 1代表单产品,2代表产品包
        param.put("ptype", "1");
        return productLineMapper.searchProductPageList(param);
    }

    /**
     * CRM菜单管理
     * 获取企业下单一产品的菜单信息
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getProductMenuList(Map<String, Object> param) {
        return productLineMapper.queryProductMenuList(param);
    }

    /**
     * CRM菜单管理
     * 获取企业下单一产品的菜单信息
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getProductMenuLists(Map<String, Object> param) {
        return productLineMapper.queryProductMenuLists(param);
    }

    /**
     * CRM菜单管理
     * CRM菜单新建同级菜单或子级
     *
     * @author kuangbin
     */
    public int addProductMenu(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间
            long addtime = System.currentTimeMillis();
            String mid = JzbRandom.getRandomCharCap(15);
            param.put("mid", mid);
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            if (param.get("\uD83D\uDE01") != null && param.get("\uD83D\uDE01") .equals("1")) {
                param.put("status", '3');
            } else {
                param.put("status", '1');
            }
            // 加入菜单表
            count = productLineMapper.insertProductMenu(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理
     * CRM菜单新建同级菜单或子级
     *
     * @author kuangbin
     */
    public int addProductMenus(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间
            long addtime = System.currentTimeMillis();
            String mid = JzbRandom.getRandomCharCap(15);
            param.put("mid", mid);
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            param.put("status", '1');
            if (param.get("appid") != null && param.get("appid") != "") {
                Map<String, Object> map = new HashMap<>();
                map.put("appid", param.get("appid"));
                //查询出来应用  添加到产品表中
                List<Map<String,Object>> list = getOrgApplication(map);
                param.put("pid", param.get("appid"));
                param.put("plid", param.get("appline"));
                //添加产品
                for (int i = 0; i < list.size(); i++) {
                    long time = System.currentTimeMillis();
                    list.get(i).put("pid", param.get("appid"));
                    list.get(i).put("addtime", time);
                    list.get(i).put("updtime", time);
                    list.get(i).put("cname", list.get(i).get("appname"));
                    list.get(i).put("cid", param.get("cid"));
                    list.get(i).put("ouid", param.get("uid"));
                    list.get(i).put("upduid", param.get("uid"));
                    list.get(i).put("plid", list.get(i).get("appline"));
                    productLineMapper.addProductList(list.get(i));
                }
            }
            // 加入菜单表
            count = productLineMapper.insertProductMenu(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理
     * CRM菜单修改菜单信息或删除菜单信息
     *
     * @author kuangbin
     */
    public int modifyProductMenu(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间
            long updtime = System.currentTimeMillis();
            param.put("updtime", updtime);
            // 获取前台传入的操作方式
            int maybe = JzbDataType.getInteger(param.get("maybe"));

            // 判断操作方式,1代表修改操作,2代表删除操作
            if (maybe == 1) {
                count = productLineMapper.updateProductMenu(param);
            } else {
                // 删除即根据MID将状态从1改为2
                param.put("status", "2");
                count = productLineMapper.deleteProductMenu(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-记支宝电脑端下产品线下显示所有产品
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getProductList(Map<String, Object> param) {
        param.put("status", "1");
        // 产品线ID为int值,为防止sql注入,先做处理
        param.put("plid", JzbDataType.getInteger(param.get("plid")));
        return productLineMapper.queryProductList(param);
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下新建企业产品
     *
     * @author kuangbin
     */
    public int addCompanyProduct(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间戳
            long addtime = System.currentTimeMillis();

            // 获取11位随机字符串产品ID
            String pid = JzbDataType.getString(param.get("pid")) == null || JzbDataType.getString(param.get("pid")) == "" ?
                    JzbRandom.getRandomCharCap(11) : JzbDataType.getString(param.get("pid"));
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            param.put("pid", pid);
            param.put("packid", JzbRandom.getRandomCharCap(7));
            // 加入默认状态
            param.put("status", "1");
            count = productLineMapper.insertProduct(param);
            if (count == 1) {
                count = productLineMapper.insertCompanyProduct(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下修改企业产品或删除企业产品
     *
     * @author kuangbin
     */
    public int modifyCompanyProduct(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间戳
            long updtime = System.currentTimeMillis();
            param.put("updtime", updtime);
            // 获取前台传入的操作方式
            int maybe = JzbDataType.getInteger(param.get("maybe"));

            // 判断操作方式,1代表修改操作,2代表删除操作
            if (maybe == 1) {
                // 更新产品表信息
                count = productLineMapper.updateProductList(param);
                if (count == 1) {
                    // 更新企业产品表信息
                    count = productLineMapper.updateCompanyProduct(param);
                }
            } else {
                // 删除即将状态从1改为2
                param.put("status", "2");
                count = productLineMapper.updateProductList(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-点击从页面添加按钮,将页面加入当前菜单中
     *
     * @author kuang Bin
     */
    public int addExistingPage(List<Map<String, Object>> list, Map<String, Object> param) {
        int count;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            String uid = JzbDataType.getString(userInfo.get("uid"));
            // 获取当前时间
            long addtime = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                map.put("addtime", addtime);
                map.put("updtime", addtime);
                map.put("status", "1");
                map.put("uid", uid);
            }
            // 加入产品页面表
            count = productLineMapper.insertExistingPage(list);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面新建控件
     *
     * @author kuang Bin
     */
    public int addPageControl(Map<String, Object> param) {
        int count;
        try {
            // 加入页面控件表
            count = productLineMapper.insertPageControl(param);
            if (count > 0) {
                // 加入控件API表
                count = productLineMapper.insertControlPower(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    public List<Map<String, Object>> getProductPageLists(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");
        return productLineMapper.queryProductPageLists(param);
    }

    /**
     * CRM菜单管理-计支宝电脑端-全界面，应用新增
     * @param param
     * @return
     */
    public int addOrgApplication(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("appid", JzbRandom.getRandomCharCap(11));
        return productLineMapper.addOrgApplication(param);
    }

    /**
     * 获取全界面的应用
     * @return
     */
    public List<Map<String, Object>> getOrgApplication(Map<String,Object> param) {
        return productLineMapper.getOrgApplication(param);
    }
}
