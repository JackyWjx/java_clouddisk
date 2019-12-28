package com.jzb.resource.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbProductFunctionMapper;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProductFunctionService {

    @Autowired
    private TbProductFunctionMapper tbProductFunctionMapper;

    /**
     * 查询产品功能表对应的资源产品
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTbProductFunction(Map<String, Object> param) {
        return tbProductFunctionMapper.getTbProductFunction(param);
    }


    /**
     * 新建同级
     *
     * @param paramList
     * @return
     */
    public int saveTbProductFunction(List<Map<String, Object>> paramList) {
        long time = System.currentTimeMillis();

        for (int i = 0; i < paramList.size(); i++) {

            String funid = JzbRandom.getRandomChar(15);
            if (paramList.get(i).get("funid") != null && paramList.get(i).get("funid") != "") {
                funid = paramList.get(i).get("funid").toString()+"00";
                funid=funid.substring(0, 15);
            }
            paramList.get(i).put("funid", funid);
            paramList.get(i).put("parentid", "000000000000000");
            paramList.get(i).put("addtime", time);
            paramList.get(i).put("updtime", time);
            if (paramList.get(i).get("children") != null && paramList.get(i).get("children") != "") {
                saveTbProductFunctionSon((List<Map<String, Object>>) paramList.get(i).get("children"), funid);
            }

        }
        for (int i = 0; i < paramList.size(); i++) {
            tbProductFunctionMapper.saveTbProductFunction(paramList.get(i));
        }
        return 1;
    }

    /**
     * 新建子集
     *
     * @param paramList
     * @return
     */
    public int saveTbProductFunctionSon(List<Map<String, Object>> paramList, String paremtid) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < paramList.size(); i++) {
            String funid = JzbRandom.getRandomChar(15);
            if (paramList.get(i).get("funid") != null && paramList.get(i).get("funid") != "") {
                funid = paramList.get(i).get("funid").toString()+"00";
                funid=funid.substring(0, 15);
            }
            paramList.get(i).put("funid", funid);
            paramList.get(i).put("parentid", paremtid);
            paramList.get(i).put("addtime", time);
            paramList.get(i).put("updtime", time);
            if (paramList.get(i).get("children") != null && paramList.get(i).get("children") != "") {
                saveTbProductFunctionSon((List<Map<String, Object>>) paramList.get(i).get("children"), funid);
            }
        }

        for (int i = 0; i < paramList.size(); i++) {

            tbProductFunctionMapper.saveTbProductFunction(paramList.get(i));
        }
        return 1;
    }

    /**
     * 修改产品功能
     *
     * @param paramList
     * @return
     */
    public int updateTbProductFunction(List<Map<String, Object>> paramList) {
        return tbProductFunctionMapper.updateTbProductFunction(paramList);
    }

    /**
     * 点击修改时查询产品功能表中的数据
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProductFunction(Map<String, Object> param) {
        return tbProductFunctionMapper.getProductFunction(param);
    }

    /**
     * 查询分页的总数
     *
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbProductFunctionMapper.getCount(param);
    }

    /**
     * 根据id查询这个功能存不存在
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProductFunctions(Map<String, Object> param) {
        return tbProductFunctionMapper.getProductFunctions(param);
    }

    /**
     * 删除存在的功能，修改状态
     * @param param
     * @return
     */
    public int updateProductFunctions(Map<String, Object> param) {
        return tbProductFunctionMapper.updateProductFunctions(param);
    }

    /**
     * 根据id进行功能数据的删除
     * @param list
     * @return
     */
    public int deleteFunction(List<String> list){
        return tbProductFunctionMapper.deleteFunction(list);
    }
}
