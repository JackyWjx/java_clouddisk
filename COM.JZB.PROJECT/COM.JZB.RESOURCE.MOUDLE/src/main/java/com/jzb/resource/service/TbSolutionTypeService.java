package com.jzb.resource.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
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
    public List<Map<String, Object>> getSolutionType(Map<String, Object> param) {
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

    /**
     * CRM-运营管理-解决方案-文件列表-分类2
     * 点击新增按钮新建文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int addSolutionType(Map<String, Object> param) {
        long addtime = System.currentTimeMillis();
        // 获取活动ID
        String typeid = JzbRandom.getRandomCharCap(7);
        param.put("addtime", addtime);
        param.put("typeid", typeid);
        param.put("status", "1");
        return tbSolutionTypeMapper.insertSolutionType(param);
    }

    /**
     * CRM-运营管理-解决方案-文件列表-分类3
     * 点击修改按钮进行文章分类修改
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int modifySolutionType(Map<String, Object> param) {
        long updtime = System.currentTimeMillis();
        // 获取活动ID
        param.put("updtime", updtime);
        param.put("status", "1");
        return tbSolutionTypeMapper.updateSolutionType(param);
    }

    /**
     * CRM-运营管理-解决方案-文件列表-分类1
     * 点击新建显示文章分类的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int getSolutionTypeDataCount(Map<String, Object> param) {
        param.put("status", "1");
        return tbSolutionTypeMapper.querySolutionTypeDataCount(param);
    }

    /**
     * CRM-运营管理-解决方案-文件列表-分类1
     * 点击新建显示文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getSolutionTypeData(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> list = tbSolutionTypeMapper.getSolutionTypeData(param);
        return list;
    }

    /**
     * 设置分页数
     */
    public Map<String, Object> setPageSize(Map<String, Object> param) {
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        pagesize = pagesize <= 0 ? 15 : pagesize;
        pageno = pageno <= 0 ? 1 : pageno;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }

    /**
     * CRM-运营管理-解决方案-文件列表-分类4
     * 点击新建显示文章分类
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int removeSolutionType(Map<String, Object> param) {
        int count = tbSolutionTypeMapper.getSolutionTypeDocument(param);
        if (count == 0){
            long updtime = System.currentTimeMillis();
            // 获取活动ID
            param.put("updtime", updtime);
            param.put("status", "2");
            count = tbSolutionTypeMapper.deleteSolutionType(param);
        }else {
            count = 4;
        }
        return count;
    }
}
