package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TenderResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/tenderResult")
public class TbTenderResultController {

    @Autowired
    private TenderResultService tenderResultService;

    /**
     * 获取招投标信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTenderResultList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderResultList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断指定参数为空返回error
            if (JzbCheckParam.haveEmpty(param,new String[]{"page","rows"})) {

                result = Response.getResponseError();

            } else {

                setPageRows(param);
                // 结果集
                List<Map<String, Object>> list = tenderResultService.getTenderResultList(param);

                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("opendate", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("opendate")), JzbDateStr.yyyy_MM_dd));
                    list.get(i).put("addtime",JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }

                // 定义返回结果
                result = Response.getResponseSuccess();

                // 定义pageinfo
                PageInfo pi=new PageInfo();

                pi.setList(list);

                // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
                pi.setTotal(tenderResultService.quertTenderResultCount(param));
                result.setPageInfo(pi);
            }
        } catch (Exception e) {

            JzbTools.logError(e);
            result = Response.getResponseError();

        }

        return result;

    }

    /**
     * 设置好分页参数
     * @param params
     */
    public static void setPageRows(Map<String ,Object> params){
        int rows = JzbDataType.getInteger(params.get("rows"));
        int page = JzbDataType.getInteger(params.get("page"));
        params.put("page", JzbDataType.getInteger(page * rows - rows < 0 ? 0 : page * rows - rows));
        params.put("rows", rows);
    }
}
