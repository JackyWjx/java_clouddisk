package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import com.jzb.resource.service.TbSolutionDomService;
import com.jzb.resource.util.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 获取方案文档
 */
@RestController
@RequestMapping(value = "/solutionDom")
public class TbSolutionDomController {

    @Autowired
    private TbSolutionDomService tbSolutionDomService;

    /**
     * 根据方案类别查询方案文档 param传参  有typeid查改typeid下方案文档，无则查所有
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSolutionDom", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionDom(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 如果参数typeid为"" 则不进mapper.xml
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);

                List<Map<String, Object>> list = tbSolutionDomService.getSolutionDom(param);

                PageInfo pi = new PageInfo();
                pi.setList(list);

                int count = tbSolutionDomService.queryCount(param);
                pi.setTotal(count);

                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 模糊查询方案文档列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSolutionDomCname", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionDomCname(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            // 判断如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                result = Response.getResponseError();
            } else {
                // 查询结果
                List<Map<String, Object>> list = tbSolutionDomService.getSolutionDomCname(param);

                // 查询总数
                int count = tbSolutionDomService.queryCount(param);

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setTotal(count);
                pi.setList(list);

                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询单篇文档详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSolutionDomByDomid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionDomByDomid(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"domid"})) {
                result = Response.getResponseError();
            } else {
                // 查询结果
                List<Map<String, Object>> list = tbSolutionDomService.queryDomByDomid(param);

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击解决方案中的新建后加入新建的方案文章
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addSolutionDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response addSolutionDom(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid", JzbDataType.getString(userInfo.get("uid")));
            // 加入新建的活动文章内容
            int count = tbSolutionDomService.addSolutionDom(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End addSolutionDom

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击保存后对解决方案中的文章进行修改
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifySolutionDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifySolutionDom(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 加入新建的活动文章内容
            int count = tbSolutionDomService.modifySolutionDom(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End modifySolutionDom

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击删除后对解决方案中的文章进行删除操作(即修改状态)
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/removeSolutionDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeSolutionDom(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回修改成功数
            int count = tbSolutionDomService.removeSolutionDom(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End removeSolutionDom

    /**
     * CRM-运营管理-活动-文章列表
     * 点击搜索解决方案文章标题后进行模糊搜索,可加入时间
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/searchSolutionDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchSolutionDom(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String startTime = JzbDataType.getString(param.get("starttime"));
            // 判断是否有开始时间的查询条件
            if (!JzbDataType.isEmpty(startTime)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(startTime);
                long time = date.getTime();
                // 将时间转化为时间戳存入参数中
                param.put("starttime", time);
            }
            // 获取前端传来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询所有符合条件的总数
                count = tbSolutionDomService.searchSolutionDomCount(param);
            }
            // 返回所有的推广信息列表
            List<Map<String, Object>> activityList = tbSolutionDomService.searchSolutionDom(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            result = Response.getResponseSuccess(userInfo);
            pageInfo.setList(activityList);
            pageInfo.setTotal(count > 0 ? count : activityList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End modifyAdvertData

    /**
     * CRM-运营管理-解决方案-SEO优化
     * 点击保存后对解决方案中的SEO优化进行修改
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifySolutionDomSEO", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifySolutionDomSEO(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 修改修改成功数
            int count = tbSolutionDomService.modifySolutionDomSEO(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End modifySolutionDomSEO
}
