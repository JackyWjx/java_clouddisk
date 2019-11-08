package com.jzb.operate.service;

import com.jzb.base.message.Response;
import com.jzb.operate.api.org.TbCompanyListApi;
import com.jzb.operate.api.redis.UserRedisServiceApi;
import com.jzb.operate.dao.TbHandleItemMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TbHandleItemService {

    @Autowired
    private TbHandleItemMapper tbHandleItemMapper;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private TbCompanyListApi tbCompanyListApi;




    /**
     * 跟进人详情查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> getHandleItem(Map<String, Object> param) {

        List<Map<String, Object>> list = tbHandleItemMapper.getHandleItem(param);
        for (int i = 0; i < list.size(); i++) {
            //拿到cid 进行销售统计分析的查询
            //HashMap<String, Object> map = new HashMap<>();
            param.put("cid", list.get(i).get("cid"));
            //调用org服务的api进行销售统计分析的数据进行查询
            Response response = tbCompanyListApi.queryCompanyList(param);
            //查询出来的结果转成map
            List<Map<String,Object>> entity = (List<Map<String, Object>>) response.getResponseEntity();

            //根据业主名称进行条件查询的判断
            if (param.get("cname") != null && param.get("cname") != "") {
                if (entity.size() <= 0 || entity == null) {
                    list.get(i).clear();
                }
            }

            //根据等级进行条件查询
            if (param.get("dictvalue") != null && param.get("dictvalue") != "") {
                if (entity.size() <= 0 || entity == null) {
                    list.get(i).clear();
                }
            }

            for (int j = 0; j < entity.size(); j++) {
                if (entity.get(j) != null) {
                    list.get(i).put("projectname", entity.get(j).get("projectname"));
                    list.get(i).put("dictvalue", entity.get(j).get("dictvalue"));
                    list.get(i).put("unitName", entity.get(j).get("cname"));
                    list.get(i).put("contamount", entity.get(j).get("contamount"));
                    continue;
                }
        }

        }
            return list;
        }


    /**
     * 所有业主-销售统计分许-根据页面的超链接进行查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryHandleItem(Map<String, Object> param) {

        List<Map<String, Object>> list = tbHandleItemMapper.queryHandleItem(param);
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            //拿到cid 进行销售统计分析的查询
            //HashMap<String, Object> map = new HashMap<>();
            param.put("cid", list.get(i).get("cid"));
            //调用org服务的api进行销售统计分析的数据进行查询
            Response response = tbCompanyListApi.queryCompanyList(param);
            //查询出来的结果转成map
            List<Map<String,Object>> entity = (List<Map<String, Object>>) response.getResponseEntity();
            if (entity.size() <= 0) {
                list.get(i).put("projectname", "");
                list.get(i).put("dictvalue", "");
                list.get(i).put("unitName", "");
                list.get(i).put("contamount", "");
            }
            for (int j = 0; j < entity.size(); j++) {
                if (entity.get(j) != null) {
                    list.get(i).put("projectname", entity.get(j).get("projectname"));
                    list.get(i).put("dictvalue", entity.get(j).get("dictvalue"));
                    list.get(i).put("unitName", entity.get(j).get("cname"));
                    list.get(i).put("contamount", entity.get(j).get("contamount"));
                    break;
                }
                //根据等级进行条件查询
                if (param.get("dictvalue") != null && param.get("dictvalue") != "") {
                    if (entity.get(i) == null) {
                        list.get(i).clear();
                    }
                    //根据业主名称进行条件查询的判断
                } else if (param.get("cname") != null && param.get("cname") != "") {
                    if (entity.get(i) == null) {
                        list.get(i).clear();
                    }
                }

            }
        }
        return list;
    }
}
