package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.resource.service.TbTempItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板条目
 *
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/tempItem")
public class TbTempItemController {

    @Autowired
    private TbTempItemService tbTempItemService;

    /**
     * @param param
     * @return
     * @deprecated 1.根据模板类型查询条目
     */
    @RequestMapping(value = "/getTempItem", method = RequestMethod.POST)
    @ResponseBody
    public Response getTempItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断参数为空返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                //------------------------------
                // 查询结果
                List<Map<String, Object>> list = tbTempItemService.getTempItem(param);
                Map<String, Integer> map1 = new HashMap<>();

                //循环把各等级id录入
                for (Map<String, Object> child : list) {

                    // 首先录入第一个父级
                    if (!map1.containsKey(child.get("parentid")) && "00000000000".equals(child.get("parentid").toString())) {
                        map1.put(child.get("itemid").toString(), 1);
                    }

                    // 判断map1里面是否存在结果集中的itemid，不存在并且父id存在的话就将其id录入  level为父id level加1
                    if (!map1.containsKey(child.get("itemid")) && map1.containsKey(child.get("parentid"))) {
                        map1.put(child.get("itemid").toString(), Integer.parseInt(map1.get(child.get("parentid")).toString()) + 1);
                    }
                }
                // 遍历结果集 将level录入每一条记录
                for (Map<String, Object> child : list) {
                    child.put("level", map1.get(child.get("itemid")));
//                    child.put("addtime", JzbDateUtil.toDateString(child.get("addtime"), JzbDateStr))
                }
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                PageInfo pi = new PageInfo<>();
                pi.setList(list);
                result.setPageInfo(pi);
                //-----------------------------------------
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param brotherid
     * @return
     * @deprecated 添加条目
     */
    @RequestMapping(value = "addTempItemBrother", method = RequestMethod.POST)
    @ResponseBody
    public Response addTempItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "typeid", "ouid"})) {
                result = Response.getResponseError();
            } else {

                // 添加返回值大于0 则添加成功
                int count = tbTempItemService.saveTempItemBrother(param);
                result = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param parentid
     * @return
     * @deprecated 添加条目
     */
    @RequestMapping(value = "addTempItemSon", method = RequestMethod.POST)
    @ResponseBody
    public Response addTempItemSon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "typeid", "ouid", "parentid"})) {
                result = Response.getResponseError();
            } else {
                // 添加返回值大于0 则添加成功
                int count = tbTempItemService.saveTempItemSon(param);
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param itemid
     * @return
     * @deprecated 修改条目
     */
    @RequestMapping(value = "updateTempItem", method = RequestMethod.POST)
    @ResponseBody
    public Response updateTempItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "itemid"})) {
                result = Response.getResponseError();
            } else {

                // 添加返回值大于0 则添加成功
                int count = tbTempItemService.updateTempItem(param);

                // 返回count大于0 返回success 否则error
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }

            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param(itemid)
     * @return
     * @deprecated 设置删除状态
     */
    @RequestMapping(value = "/setDelete", method = RequestMethod.POST)
    @ResponseBody
    public Response updateDataType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"itemid"})) {
                result = Response.getResponseError();
            } else {

                //设置删除条目
                int count = tbTempItemService.setDelete(param);

                // 返回count大于0 返回success 否则error
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }
}
