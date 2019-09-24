package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.query.QueryPageConfig;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        QueryPageConfig.setPageSize(param);

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
            count = productMapper.insertPageControl(pageList);
            if (count > 0) {
                // 根据查询出来的控件所有信息加入控件API表
                count = productMapper.insertControlPower(pageList);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }
}


