package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbProductResListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProductResListService {

    @Autowired
    private TbProductResListMapper tbProductResListMapper;



    /**
     * 查询合同配置中的产品参数
     *    根据产品线的id查询产品表
     *    * 根据产品id查询出产品参数
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProductResList(Map<String, Object> param) {
        return tbProductResListMapper.getProductResList(param);

    }

    /**
     * 添加新建产品参数
     * @param paramList
     * @return
     */
    public int saveTbProductParameteItem(List<Map<String, Object>> paramList) {
        return tbProductResListMapper.saveTbProductParameteItem(paramList);
    }

    /**
     * 修改合同配置中的产品参数列表
     * @param paramList
     * @return
     */
    public int updateTbProductParameteItem(List<Map<String, Object>> paramList) {
        return tbProductResListMapper.updateTbProductParameteItem(paramList);
    }
    /**
     * 删除
     * @param list
     * @return
     */
    public int updateTbProductResList(List<String> list){
        return tbProductResListMapper.updateTbProductResList(list);
    }

    /**
     * 添加资源产品表的数据
     *
     * @param param
     * @return
     */
    public int saveTbProductResList(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        return tbProductResListMapper.saveTbProductResList(param);
    }



    /**
     * 点击修改的时候查询产品参数返回给前端
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTbProductParameteItem(Map<String, Object> param) {
          return tbProductResListMapper.getTbProductParameteItem(param);
    }

    public List<Map<String, Object>> getProductItem(Map<String, Object> param) {
        return tbProductResListMapper.getProductItem(param);
    }


    /**
     * 查询验收单的URl
     * @param param
     * @return
     */
    public List<Map<String, Object>> getURL(Map<String, Object> param) {
        return tbProductResListMapper.getURL(param);
    }

    /**
     * 查询总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbProductResListMapper.getCount(param);
    }


    /**
     * 查询引用合同模板
     * @param param
     * @return
     */
    public List<Map<String, Object>> getResList(Map<String, Object> param) {
        return tbProductResListMapper.getResList(param);
    }
}

