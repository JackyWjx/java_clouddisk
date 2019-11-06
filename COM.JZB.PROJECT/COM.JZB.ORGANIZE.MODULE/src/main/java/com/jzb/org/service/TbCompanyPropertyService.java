package com.jzb.org.service;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.org.dao.TbCompanyPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyPropertyService {
    @Autowired
    private TbCompanyPropertyMapper tbCompanyPropertyMapper;

    /**
     * 添加单位动态属性
     * @param param
     * @return
     */
    public int addCompanyProperty(Map<String, Object> param){
        // 时间
        long time=System.currentTimeMillis();
        param.put("addtime",time);
        param.put("updtime",time);
        // 生成属性ID
        param.put("dictid", JzbRandom.getRandomCharLow(11));
        return tbCompanyPropertyMapper.addCompanyProperty(param);
    }


    /**
     * 查询企业id 以及 类型id 是否存在，给设置业主等级提供
     * @param param
     * @return
     */
    public int queryexists(Map<String, Object> param){
        return tbCompanyPropertyMapper.queryexists(param);
    }

    /**
     * 修改属性默认值
     * @param param
     * @return
     */
    public int updatePropertyByCidAndTypeid(Map<String, Object> param){
        // 时间
        long time=System.currentTimeMillis();
        param.put("updtime",time);
        return tbCompanyPropertyMapper.updatePropertyByCidAndTypeid(param);
    }

    /**
     * 所有业主-业主列表-分配售后人员
     * @param param
     * @return
     */
    public int saveCompanyProperty(Map<String, Object> param) {
        param.put("typeid", "t000000");
        param.put("dictid", JzbRandom.getRandomChar(11));
        long time = System.currentTimeMillis();
        param.put("addtime",time);
        param.put("updtime",time);
        return tbCompanyPropertyMapper.saveCompanyProperty(param);
    }

    /**
     * 所有业主-业主列表-设置等级
     * @param param
     * @return
     */
    public int saveCompanyPropertys(Map<String, Object> param) {


         List<Map<String,Object>> list = tbCompanyPropertyMapper.getsaveCompanyPropertys(param);
        int count;
        if (list.size() > 0) {
            param.put("updtime", System.currentTimeMillis());
           count = tbCompanyPropertyMapper.updateompanyPropertys(param);
        } else {

            param.put("typeid", "t111111");
            param.put("dictid", JzbRandom.getRandomChar(11));
            long time = System.currentTimeMillis();
            param.put("addtime",time);
            param.put("updtime",time);
           count =  tbCompanyPropertyMapper.saveCompanyPropertys(param);
        }
        return count;
    }


    /**
     * 查各个等级次数
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryLevelCount(Map<String, Object> param){
        return tbCompanyPropertyMapper.queryLevelCount(param);
    }

    /**
     *  分配服务人员
     * @param param
     * @return
     */
    public int addCompanyPropert(Map<String, Object> param) {
        param.put("typeid", "t222222");
        param.put("dictid", JzbRandom.getRandomChar(11));
        long time = System.currentTimeMillis();
        param.put("addtime",time);
        param.put("updtime",time);
        return tbCompanyPropertyMapper.addCompanyPropert(param);
    }

    /**
     * 根据A类 B类  C类进行分页
     * @param param
     * @return
     */
    public List<Map<String, Object>> GroupCompanyPropert(Map<String, Object> param) {
        return tbCompanyPropertyMapper.GroupCompanyPropert(param);
    }
}
