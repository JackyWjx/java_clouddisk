package com.jzb.resource.service;

import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbTempItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbTempItemService {

    @Autowired
    private TbTempItemMapper tbTempItemMapper;

    /*
     * 1.根据模板类型查询条目
     * */
    public List<Map<String, Object>> getTempItem(Map<String, Object> param) {
        return tbTempItemMapper.queryTempItem(param);
    }

    /*
     * 2.添加条目 同级
     * */
    public int saveTempItemBrother(Map<String, Object> param) {
        // 判断没有传brotherid则添加为第一级  否则根据传的brotherid查询parentid
        if (JzbCheckParam.haveEmpty(param, new String[]{"brotherid"})) {
            param.put("parentid", "00000000000");
        } else {
            param.put("parantid", getParentByBrotherId(param));
        }
        Long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("itemid", param.get("typeid")+JzbRandom.getRandomCharCap(4));
        param.put("idx", getTempItemIdx());
        return tbTempItemMapper.saveTempItem(param);
    }

    /*
     * 3.添加条目 子级
     * */
    public int saveTempItemSon(Map<String, Object> param) {
        Long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("itemid", param.get("typeid")+JzbRandom.getRandomCharCap(4));
        param.put("idx", getTempItemIdx());
        return tbTempItemMapper.saveTempItem(param);
    }

    /*
     * 4.根据同级id查询父级id
     * */
    public String getParentByBrotherId(Map<String, Object> param) {
        return tbTempItemMapper.queryParentByBrotherId(param);
    }

    /*
     * 5.修改条目
     * */
    public int updateTempItem(Map<String, Object> param) {
        param.put("updtime", System.currentTimeMillis());
        return tbTempItemMapper.updateTempItem(param);
    }

    /*
     * 6.修改状态  1正常  2不正常  4 删除
     * */
    public int setDelete(Map<String, Object> param) {
        param.put("status", "4");
        return tbTempItemMapper.updateStatus(param);
    }

    /*
     * 7.获取排序
     * */
    public int getTempItemIdx() {
        return tbTempItemMapper.getTempItemIdx();
    }

}
