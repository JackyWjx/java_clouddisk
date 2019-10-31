package com.jzb.operate.service;

import com.jzb.operate.dao.TbHandleItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbHandleItemService {

    @Autowired
    private TbHandleItemMapper tbHandleItemMapper;

    /**
     * 跟进人详情查询
     * @param param
     * @return
     */
    public Map<String, Object> getHandleItem(Map<String, Object> param) {
        return tbHandleItemMapper.getHandleItem(param);
    }
}
