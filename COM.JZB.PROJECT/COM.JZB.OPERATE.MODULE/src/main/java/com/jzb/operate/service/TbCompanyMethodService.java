package com.jzb.operate.service;

import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.resource.MethodDataApi;
import com.jzb.operate.dao.TbCompanyMethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbCompanyMethodService {
    @Autowired
    private TbCompanyMethodMapper tbCompanyMethodMapper;

    @Autowired
    private MethodDataApi methodDataApi;


    /**
     * 添加单位方法论（修改同步）
     * @param list
     * @return
     */
    public int addCompanyMethod(List<Map<String, Object>> list){
        int count = tbCompanyMethodMapper.addCompanyMethod(list);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("status",'3');
            // 查询是否已有我的方法论父级
            Map<String,Object> map = tbCompanyMethodMapper.queryMyMethod(list.get(i));
            if (JzbTools.isEmpty(map)){
                // 插入我的方法论 方法父级
                tbCompanyMethodMapper.addMymethod(list.get(i));
            }
        }

        return count;
    }


    /**
     * 查询单位方法论
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyMethod(Map<String, Object> param){
        List<Map<String, Object>> list = tbCompanyMethodMapper.queryCompanyMethod(param);
        return list;
    }

    /**
     * @Author Reed
     * @Description //获取我的方法论
     * @Date 15:08 2020/1/17
     * @Param [param]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    public List<Map<String, Object>> queryMyCompanyMethod(Map<String, Object> param){
        List<Map<String, Object>> list = tbCompanyMethodMapper.queryMyCompanyMethod(param);
        return list;
    }

    /**
     * 设置方法论状态
     * @param param
     * @return
     */
    public int updateCompanyMethodStatus(Map<String, Object> param){
        param.put("status",'0');
        return tbCompanyMethodMapper.updateCompanyMethodStatus(param);
    }

    public List<Map<String, Object>> getCompanyMethodByids(Map<String, Object> param) {

        return tbCompanyMethodMapper.getCompanyMethodByids(param);
    }

    public int addMethodData(List<Map<String, Object>> datalist) {
        int count = tbCompanyMethodMapper.addMethodData(datalist);
        for (int i = 0; i < datalist.size(); i++) {
            datalist.get(i).put("status",'3');
            // 查询是否已经存在我的方法论i
            Map<String,Object> map = tbCompanyMethodMapper.queryMyMethodData(datalist.get(i));
            if (JzbTools.isEmpty(map)){
                // 插入可供增删改的我的方法论
                tbCompanyMethodMapper.addMyMethodData(datalist.get(i));
            }
        }
        return count;
    }

    public List<Map<String, Object>> getCompanyMethoddataAll(Map<String, Object> param) {
        return tbCompanyMethodMapper.getCompanyMethoddataAll(param);
    }
    public List<Map<String, Object>> getMyCompanyMethoddataAll(Map<String, Object> param) {
        return tbCompanyMethodMapper.getMyCompanyMethoddataAll(param);
    }

    public int delCompanyMethod(String param) {
        return tbCompanyMethodMapper.delCompanyMethod(param);
    }
    public int delMyCompanyMethod(String param) {
        return tbCompanyMethodMapper.delMyCompanyMethod(param);
    }

    public void delcidsandprojectid(Map<String, Object> map) {
         tbCompanyMethodMapper.delcidsandprojectid(map);
    }

    public int addMyMethodTypeBrother(Map<String, Object> param) {
        if (JzbCheckParam.haveEmpty(param, new String[]{"brotherid"})) {
            param.put("parentid", "0000000");
        } else {
            param.put("score",100);
        }
        if(JzbCheckParam.haveEmpty(param, new String[]{"score"})){
            param.put("score",100);
        }
        Long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("methodid", JzbRandom.getRandomCharCap(7));
        param.put("status",'3');
        param.put("numberone",JzbRandom.getRandomCharCap(20));
        //param.put("idx", getMethodTypeIdx());
        return tbCompanyMethodMapper.addMymethod(param);
    }

    public int updateMyMethodType(Map<String, Object> param) {
        Long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbCompanyMethodMapper.updateMyMethodType(param);
    }

    // 添加我的方法论同级
    public int addMyMethodDataBrother(Map<String, Object> param) {
        Long time=System.currentTimeMillis();
        if(param.get("parentid")==null || param.get("parentid").toString().equals("")){
            param.put("parentid", "0000000");
        }
        param.put("addtime",time);
        param.put("updtime",time);
        param.put("status",'3');
        return tbCompanyMethodMapper.addMyMethodData(param);
    }

    public int updateMyMethodData(Map<String, Object> param) {
        Long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbCompanyMethodMapper.updateMyMethodData(param);
    }

    public List<Map<String, Object>> queryMyMethodType(Map<String, Object> param) {
        return tbCompanyMethodMapper.queryMyMethodType(param);
    }
}
