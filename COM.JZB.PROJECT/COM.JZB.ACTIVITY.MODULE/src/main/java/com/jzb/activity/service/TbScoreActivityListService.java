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
        *@描述
        *@创建人 chenhui
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@Service
public class TbScoreActivityListService {
    @Autowired
    TbScoreActivityListMapper scoreMapper;

    @Autowired
    OrgCompanyDpt orgCompanyDpt;

    /**
     * 查询活动列表
     * @param paramap
     * @return
     */
    public List<Map<String, Object>> getActivity(Map<String, Object> paramap) {
        if (!JzbTools.isEmpty(paramap.get("endTime"))){
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
     * @param paramap
     * @return
     */
    public int getCount(Map<String, Object> paramap) {
        return scoreMapper.getCount(paramap);
    }

    /**
     * 积分列表页面-新建活动
     * @param paramp
     * @return
     */
    public int addActivityList(Map<String, Object> paramp) {

        long addtime = System.currentTimeMillis();
        paramp.put("addtime",addtime);
        String actid = JzbRandom.getRandomCharLow(11);
        paramp.put("actid",actid);
        System.out.println(paramp.get("uid"));
        // 加入活动信息
        int count = scoreMapper.addActivityList(paramp);
        if (count > 0){
            if (!JzbDataType.isEmpty(paramp.get("photoList"))){
                    List<Map<String ,Object>> photoList = (List<Map<String, Object>>) paramp.get("photoList");
                    for (int i = photoList.size() - 1; i >= 0; i--) {
                        Map<String,Object> photoMap = photoList.get(i);
                        if (!JzbDataType.isEmpty(photoMap)){
                            long addtim = System.currentTimeMillis();
                            photoMap.put("actid",actid);
                            photoMap.put("status","1");
                            photoMap.put("photo",photoList.get(i).get("photo"));
                            photoMap.put("fileid",JzbRandom.getRandomCharLow(15));
                            photoMap.put("adduid",paramp.get("uid"));
                            photoMap.put("addtime",addtim);
                        }else {
                            photoList.remove(i);
                        }
                    }
                    // 加入证明图片
                    count = scoreMapper.insertActivityPhoto(photoList);
                }else  if(!JzbDataType.isEmpty(paramp.get("photoList2"))) {
                 List<Map<String ,Object>> photoList = (List<Map<String, Object>>) paramp.get("photoList2");
                for (int i = photoList.size() - 1; i >= 0; i--) {
                    Map<String,Object> photoMap = photoList.get(i);
                    if (!JzbDataType.isEmpty(photoMap)){
                        long addtim = System.currentTimeMillis();
                        photoMap.put("actid",actid);
                        photoMap.put("status","4");
                        photoMap.put("photo",photoList.get(i).get("photo"));
                        photoMap.put("fileid",JzbRandom.getRandomCharLow(15));
                        photoMap.put("adduid",paramp.get("uid"));
                        photoMap.put("addtime",addtim);
                    }else {
                        photoList.remove(i);
                    }
                }
            }
                else{
                    count = 0;
                }
            }

        return count;
    }
}
