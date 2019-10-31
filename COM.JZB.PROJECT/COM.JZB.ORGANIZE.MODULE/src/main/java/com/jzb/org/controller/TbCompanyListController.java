package com.jzb.org.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyListService;
import com.jzb.org.util.SetPageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/CompanyList")
public class TbCompanyListController {

    @Autowired
    private TbCompanyListService tbCompanyListService;

    /**
     * 所有业主-销售统计分析
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyList",method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyList(@RequestBody(required = false) Map<String,Object> param) {
        Response result;
        try {
                 if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                //设置分页参数
                SetPageSize setPageSize = new SetPageSize();
               param = setPageSize.setPagenoSize(param);
                //获取前端传过来的分页参数
                int count = JzbDataType.getInteger(param.get("count"));
                //获取总数
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    count = tbCompanyListService.getCount(param);
                }
                //查询数据
              List<Map<String,Object>> list = tbCompanyListService.getCompanyList(param);
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                //设置分页总数
                pageInfo.setTotal(count > 0 ? count : list.size());
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
