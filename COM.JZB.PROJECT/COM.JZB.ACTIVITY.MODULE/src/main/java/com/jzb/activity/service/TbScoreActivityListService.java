package com.jzb.activity.service;

import com.jzb.activity.api.org.OrgCompanyDpt;
import com.jzb.activity.dao.TbScoreActivityListMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/9
 * @修改人和其它信息
 */
@Service
public class TbScoreActivityListService {
    @Autowired
    TbScoreActivityListMapper scoreMapper;

    @Autowired
    OrgCompanyDpt orgCompanyDpt;

    /**
     * 查询活动列表
     *
     * @param paramap
     * @return
     */
    public List<Map<String, Object>> getActivity(Map<String, Object> paramap) {
        if (!JzbTools.isEmpty(paramap.get("endTime"))) {
            paramap.put("endTime", JzbDataType.getLong(paramap.get("endTime")) + 86400);
        }
//       List<Map<String,Object>> list =  scoreMapper.queryActivityList(paramap);
//        if (list != null && list.size() > 0){
//            for (int i = 0; i < list.size(); i++) {
//                Map<String ,Object> map =  list.get(i);
//
//                map.put("userinfo",paramap.get("userinfo"));
//                Response deptList = orgCompanyDpt.getDeptList(map);
//                List<Map<String,Object>> Dlist =  deptList.getPageInfo().getList();
//                for (int j = 0; j < Dlist.size(); j++) {
//                    Map<String ,Object> Dmap =  Dlist.get(j);
//                    if (!JzbDataType.isEmpty(JzbDataType.getString(map.get("cdid"))) && map.get("cdid").toString().trim().equals(Dmap.get("cdid")) ){
//                        map.putAll(Dmap);
//                    }
//                }
//            }
//        }
        return scoreMapper.queryActivityList(paramap);
    }

    /**
     * 查询活动总数
     *
     * @param paramap
     * @return
     */
    public int getCount(Map<String, Object> paramap) {
        return scoreMapper.getCount(paramap);
    }

    /**
     * 积分列表页面-新建活动
     *
     * @param paramp
     * @return
     */
    public int addActivityList(Map<String, Object> paramp) {

        // 添加时间
        long addtime = System.currentTimeMillis();
        // 生成活动id
        String actid = JzbRandom.getRandomCharLow(11);
        // 存入字段值
        paramp.put("addtime", addtime);
        paramp.put("actid", actid);

        // 加入活动信息
        int count = scoreMapper.addActivityList(paramp);
        if (count > 0) {
            if (!JzbDataType.isEmpty(paramp.get("photoList"))) {
                // 活动图片集合
                List<Map<String, Object>> photoList = (List<Map<String, Object>>) paramp.get("photoList");
                // 移除图片字段为空的数据  以及补充字段值
                for (int i = 0; i < photoList.size(); i++) {
                    if (JzbDataType.isEmpty(photoList.get(i).get("photo"))) {
                        photoList.remove(i);
                    } else {
                        long addtim = System.currentTimeMillis();
                        photoList.get(i).put("actid", actid);
                        photoList.get(i).put("status", "1");
                        photoList.get(i).put("adduid", paramp.get("uid"));
                        photoList.get(i).put("addtime", addtim);
                    }
                }
                // 添加活动图片  不用count接收是因为怕影响
                scoreMapper.insertActivityPhoto(photoList);
            }
            //-------------------------------------------------------
            if (!JzbDataType.isEmpty(paramp.get("photoList2"))) {
                // 培训证明图片集合
                List<Map<String, Object>> photoList = (List<Map<String, Object>>) paramp.get("photoList2");
                // 移除图片字段为空的数据  以及补充字段值
                for (int i = 0; i < photoList.size(); i++) {
                    if (JzbDataType.isEmpty(photoList.get(i).get("photo"))) {
                        photoList.remove(i);
                    } else {
                        long addtim = System.currentTimeMillis();
                        photoList.get(i).put("actid", actid);
                        photoList.get(i).put("status", "4");
                        photoList.get(i).put("adduid", paramp.get("uid"));
                        photoList.get(i).put("addtime", addtim);
                    }
                }
                // 添加证明图片
                scoreMapper.insertActivityPhoto(photoList);
            }
        }

        return count;
    }
}
