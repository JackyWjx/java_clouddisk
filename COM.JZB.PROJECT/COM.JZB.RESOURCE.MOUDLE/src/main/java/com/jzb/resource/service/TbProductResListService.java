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
     * 查询模板功能信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryProductResList(Map<String, Object> param){
        return tbProductResListMapper.queryProductResList(param);
    }

    /**
     * 添加模板功能信息
     * @param param
     * @return
     */
    public int addProductRes(Map<String, Object> param){
        return tbProductResListMapper.addProductResList(param);
    }

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
     * 根据产品的名称获取产品参数列表
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProductResListCname(Map<String, Object> param) {
        return tbProductResListMapper.getProductResListCname(param);
    }

    /**
     * 合同配置中产品参数中新建中查询出产品名称
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryProductListCname(Map<String, Object> param) {
        return tbProductResListMapper.queryProductListCname(param);
    }


    /*
     * 3.获取排序
     * */
    public int getTbProductParameteItemIdx() {
        return tbProductResListMapper.getTbProductParameteItemIdx();
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
     * @param param
     * @return
     */
    public int updateTbProductParameteItem(List<Map<String, Object>> paramList) {
        return tbProductResListMapper.updateTbProductParameteItem(paramList);
    }

    /**
     * 添加资源产品表的数据
     *
     * @param param
     * @return
     */
    public int saveTbProductResList(Map<String, Object> param) {
        return tbProductResListMapper.saveTbProductResList(param);
    }

    /**
     * 修改资源产品表中的数据
     * @param param
     * @return
     */
    public int updateTbProductResList(Map<String, Object> param) {
      return tbProductResListMapper.updateTbProductResList(param);
    }
}
