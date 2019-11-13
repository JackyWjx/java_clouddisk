package com.jzb.activity.service;

import com.jzb.activity.dao.TbScoreActivityListMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 查询活动列表
     * @param paramap
     * @return
     */
    public List<Map<String, Object>> getActivity(Map<String, Object> paramap) {


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
            if (!JzbDataType.isEmpty(paramp.get("photo"))){
                Object photo = paramp.get("photo");
                if (JzbDataType.isCollection(photo)){
                    List<Map<String ,Object>> photoList = (List<Map<String, Object>>) photo;
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
                    // 加入活动图片
                    count = scoreMapper.insertActivityPhoto(photoList);
                }else {
                    count = 0;
                }
            }
        }
        return count;
    }
}
