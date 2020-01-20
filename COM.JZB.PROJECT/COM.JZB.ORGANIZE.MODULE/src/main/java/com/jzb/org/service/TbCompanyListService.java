package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.operate.HandleItemApi;
import com.jzb.org.dao.TbCompanyListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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
        int count = tbCompanyListMapper.getCount(param);
        for (int i = companyList.size()-1; i>= 0; i--) {

            companyList.get(i).put("beginTime", param.get("beginTime"));
            companyList.get(i).put("endTime", param.get("endTime"));
            companyList.get(i).put("item", param.get("item"));
            companyList.get(i).put("service", param.get("service"));
            Response handleItem = handleItemApi.getHandleItem(companyList.get(i));

            if (JzbDataType.isEmpty(handleItem.getResponseEntity())) {
                companyList.get(i).clear();
                count--;
            } else {
                //设置operate 每一个
                companyList.get(i).put("kkk", handleItem.getResponseEntity());
                companyList.get(i).put("count", count);
            }
        }
        return companyList;
    }


    /**
     * 查询该单位下的管理员
     * @param param
     * @return
     */
    public   String queryManagerByCid(Map<String, Object> param){
        return  tbCompanyListMapper.queryManagerByCid(param);
    }

    /**
     * 所有业主-销售统计分析查询出来的总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbCompanyListMapper.getCount(param);
    }

    /**
     * 获取今天添加业主的的数量
     * @param param
     * @return
     */
    public int getCompanyListCount(Map<String, Object> param) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String now = sdf.format(date);
        try
        {
            //当前时区下的0时0分0秒
            Date newDate = sdf.parse(now);
            long start1 = newDate.getTime();
            param.put("startTime", start1);
            //获取当天23时59分59秒
            param.put("endTime", start1 + 24 * 60 * 60 * 1000 - 1);
        }
        catch (Exception e)
        {
            JzbTools.logError(e);
        }

        return tbCompanyListMapper.getCompanyListCount(param);
    }

    /**
     * 销售统计分析的查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyList(Map<String, Object> param) {
        List<Map<String, Object>> list = tbCompanyListMapper.queryCompanyList(param);

        return list;
    }
}
