package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyProjectService;
import com.jzb.org.util.SetPageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "org/CompanyProject")
public class TbCompanyProjectController {

    @Autowired
    private TbCompanyProjectService tbCompanyProjectService;

    /**
     *销售业主-公海-项目-数据查询 LBQ
     * @param param
     * @return
     */
    @RequestMapping(value = "/getComProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response getComProject(@RequestBody Map<String, Object> param) {
        Response result;
        //判断请求参数如果为空则返回404
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize"})) {
                result = Response.getResponseError();
            } else {
                //设置好分页参数
                SetPageSize setPageSize = new SetPageSize();
                param = setPageSize.setPagenoSize(param);

                //判断前端传过来的分页总数
                int count = JzbDataType.getInteger(param.get("count"));
                // 获取产品报价总数
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    count = tbCompanyProjectService.getCount(param);
                }
                //查询项目模块下的数据
                List<Map<String, Object>> list = tbCompanyProjectService.getComProject(param);
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

    /**
     * 项目的添加
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveComProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果参数为为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectname"})) {
                result = Response.getResponseError();
            } else {
              int count = tbCompanyProjectService.saveComProject(param);
                if (count > 0) {
                    //存入用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    PageInfo pageInfo = new PageInfo();
                    //返回成功的响应消息
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //否则就输出错误信息
                    result = Response.getResponseError();
                }
            }

        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 关联业主  根据项目id对项目表做修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateComProject",method = RequestMethod.POST)
    @CrossOrigin
    public Response updateComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                result = Response.getResponseError();
            } else {
              int count = tbCompanyProjectService.updateComProject(param);
                if (count > 0) {
                    //存入用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    PageInfo pageInfo = new PageInfo();
                    //返回成功的响应消息
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
