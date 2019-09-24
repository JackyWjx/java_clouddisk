package com.jzb.resource.service;

import com.jzb.resource.dao.TbSolutionTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TbSolutionTypeService {

    @Autowired
    private TbSolutionTypeMapper tbSolutionTypeMapper;


    /**
     * 1.查询方案类型（父子级）
     */
    public List<Map<String, Object>> getSolutionType(Map<String, Object> param){
        //  定义返回list
        List<Map<String, Object>> resultList = null;

        //  调用方法查询结果list
        List<Map<String, Object>> list = tbSolutionTypeMapper.querySolutionType(param);

        //  判断如果查询值不为空则进入
        if (list != null && list.size() > 0) {

            //  新建返回list对象
            resultList = new ArrayList<>();

            //  循环sql查询结果
            for (int i = 0; i < list.size(); i++) {
                if ("0000000".equals(list.get(i).get("parentid").toString().trim())) {

                    //  判断如果父级id为0的话,把该map放入返回list中
                    resultList.add(list.get(i));

                    //  定义一个子级list
                    List<Map> childrenList = new ArrayList<>();

                    //循环sql查询结果
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).get("parentid").toString().trim().equals(resultList.get(resultList.size() - 1).get("typeid").toString().trim())) {

                            //判断如果父级id是返回list（resultList）中的id的话就将其放入返回list（resultList）  父级map中
                            childrenList.add(list.get(j));
                        }
                    }
                    //添加子级list
                    resultList.get(resultList.size() - 1).put("children", childrenList);
                }
            }
            return resultList;
        }
        return null;
    }
}
