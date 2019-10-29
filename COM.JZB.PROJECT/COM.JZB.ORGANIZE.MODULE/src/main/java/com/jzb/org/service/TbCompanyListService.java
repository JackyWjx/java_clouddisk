package com.jzb.org.service;

import com.jzb.base.message.Response;
import com.jzb.org.api.operate.HandleItemApi;
import com.jzb.org.dao.TbCompanyListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyListService {

    @Autowired
    private TbCompanyListMapper tbCompanyListMapper;

    @Autowired
    private HandleItemApi handleItemApi;
    /**
     * 所有业主-销售统计分析
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCompanyList(Map<String, Object> param) {
        List<Map<String, Object>> companyList = tbCompanyListMapper.getCompanyList(param);
        for (int i = 0; i < companyList.size(); i++) {

            Response handleItem = handleItemApi.getHandleItem(companyList.get(i));
            //设置operate 每一个
            companyList.get(i).put("kkk", handleItem.getResponseEntity());

        }
        return companyList;
    }

    /**
     * 所有业主-销售统计分析查询出来的总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbCompanyListMapper.getCount(param);
    }
}
