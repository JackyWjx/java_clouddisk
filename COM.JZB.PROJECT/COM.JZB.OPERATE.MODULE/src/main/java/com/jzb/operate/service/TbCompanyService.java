package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.operate.dao.TbCompanyServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TbCompanyService {

    @Autowired
    private TbCompanyServiceMapper tbCompanyServiceMapper;
    /**
     *  项目跟进
     * @param param
     * @return
     */
    public int saveProject(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("planid", JzbRandom.getRandomChar(11));
        param.put("addtime", time);
        param.put("updtime", time);
        Date handletime = JzbDataType.getDateTime(param.get("handletime"));
        Date nexttime = JzbDataType.getDateTime(JzbDataType.getInteger(param.get("nexttime"))/1000);
        param.put("handletime", handletime.getTime());
        param.put("nexttime", nexttime.getTime());
        return tbCompanyServiceMapper.saveProject(param);
    }

    /**
     * 项目跟进的查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProject(Map<String, Object> param) {
        return tbCompanyServiceMapper.getProject(param);
    }

    /**
     * 修改跟进记录
     * @return
     */
    public boolean upComanyService(Map<String, Object> param){
        param.put("updtime",System.currentTimeMillis());
        if(param.containsKey("ouid")){
            param.put("upduid",param.get("ouid"));
        }
        return tbCompanyServiceMapper.upComanyService(param) > 0 ? true : false;
    }

    /**
     * 修改跟进记录
     * @return
     */
    public boolean saveComanyService(Map<String, Object> param){
        param.put("addtime", System.currentTimeMillis());
        param.put("updtime", System.currentTimeMillis());
        if(param.containsKey("ouid")){
            param.put("upduid",param.get("ouid"));
            param.put("adduid",param.get("ouid"));
        }
        return tbCompanyServiceMapper.saveComanyService(param) > 0 ? true : false;
    }
}
