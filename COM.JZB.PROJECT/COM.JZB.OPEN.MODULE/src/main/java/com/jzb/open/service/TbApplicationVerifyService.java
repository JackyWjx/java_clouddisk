package com.jzb.open.service;

import com.jzb.open.dao.TbApplicationVerifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbApplicationVerifyService {

    @Autowired
    private TbApplicationVerifyMapper tbApplicationVerifyMapper;



    /**
     * 提交应用列表到审批列表
     * @param param
     * @return
     */
    public int saveApplicationVerify(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("status", "1");
        //查询这个应用在审批表中有没有；如果有只能进行修改
       int counts = tbApplicationVerifyMapper.getApplicationVerify(param);
        int count;
        if (counts > 0) {
            //如果存在在提交审批,则把状态修改成未审批状态,内容也修改成修改后的
            count = tbApplicationVerifyMapper.updateApplicationVerify(param);
        } else {
            count = tbApplicationVerifyMapper.saveApplicationVerify(param);
        }

        return count;
    }

    /**
     * 提交应用菜单列表到审批表
     * @param param
     * @return
     */
    public int saveApplicationMenuVerify(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);

        return tbApplicationVerifyMapper.saveApplicationMenuVerify(param);
    }

    /**
     * 提交应用页面到审批表
     * @param param
     * @return
     */
    public int saveApplicationPageVerify(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);

        return tbApplicationVerifyMapper.saveApplicationPageVerify(param);
    }

}
