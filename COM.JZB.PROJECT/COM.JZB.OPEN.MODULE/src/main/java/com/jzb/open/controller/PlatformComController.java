package com.jzb.open.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.open.api.auth.CompanyControllerApi;
import com.jzb.open.api.org.PlatformCompanyApi;
import com.jzb.open.service.PlatformComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/7 15:03
 */
@RestController
@RequestMapping("open/com")
public class PlatformComController {

    @Autowired
    private PlatformComService platformComService;

    @Autowired
    private CompanyControllerApi companyApi;

    @Autowired
    private PlatformCompanyApi platformCompanyApi;

    /**
     * 获取所有开放平台的企业id
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchPlatformId", method = RequestMethod.POST)
    @CrossOrigin
    public Response getPlatformId(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            Map<String, Object> platCIds = platformComService.queryPlatformIds(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(platCIds);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据cid和集获取管理员信息
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/getComAndMan", method = RequestMethod.POST)
    @CrossOrigin
    public Response getComAndMan(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            List<Map<String, Object>> platCIds = platformComService.getComAndMan(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(platCIds);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 开发者列表查询
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchAppDeveloper", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchAppDeveloper(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("start", rows * (page - 1));
                param.put("pagesize", rows);

                if (!JzbTools.isEmpty(param.get("value"))) {
                    //根据姓名或去用户id
                    Response userRes = companyApi.searchUidByUidCname(param);
                    String uIds = JzbDataType.getString(userRes.getResponseEntity());
                    param.put("uids", uIds);
                }

                List<Map<String, Object>> deList = platformComService.searchAppDeveloper(param);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(deList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = platformComService.searchAppDeveloperCount(param);
                    Info.setTotal(size > 0 ? size : deList.size());
                }
                result.setPageInfo(Info);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 产品列表审批查询
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchApplicationVerify", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchApplicationVerify(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("pagesize", rows);
                param.put("start", rows * (page - 1));

                //如果传入的value值不为空
                if (!JzbTools.isEmpty(param.get("value"))) {
                    //根据value取单位cid
                    Response comRes = platformCompanyApi.searchCidByCidCname(param);
                    String cidS = JzbDataType.getString(comRes.getResponseEntity());
                    param.put("cids", cidS);

                }

                List<Map<String, Object>> AppVList = platformComService.searchApplicationVerify(param);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(AppVList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = platformComService.searchApplicationVerifyC(param);
                    Info.setTotal(size > 0 ? size : AppVList.size());
                }
                result.setPageInfo(Info);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 审批产品列表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/modifyVerify", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"status", "appid", "appvsn"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                param.put("appvsn", JzbDataType.getInteger(param.get("appvsn")));
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int update = platformComService.updateVerify(param);
                result = update > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 新增平台开发文档表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addPlatformHelper", method = RequestMethod.POST)
    @CrossOrigin
    public Response addPlatformHelper(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"title", "context"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int add = platformComService.insertPlatformHelper(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改平台开发文档表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/modifyPlatformHelper", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyPlatformHelper(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"helpid", "title", "context"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int update = platformComService.updatePlatformHelper(param);
                result = update > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 移除平台开发文档表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/removePlatformHelper", method = RequestMethod.POST)
    @CrossOrigin
    public Response removePlatformHelper(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"helpid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("status", "2");
                param.put("uid", userInfo.get("uid"));
                int update = platformComService.updatePlatformHelper(param);
                result = update > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询平台开发文档表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchPlatformHelper", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchPlatformHelper(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("pagesize", rows);
                param.put("start", rows * (page - 1));
                List<Map<String, Object>> AppVList = platformComService.searchPlatformHelper(param);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(AppVList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = platformComService.searchPlatformHelperCount(param);
                    Info.setTotal(size > 0 ? size : AppVList.size());
                }
                result.setPageInfo(Info);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 新增开放文档类型
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addOpenApiType", method = RequestMethod.POST)
    @CrossOrigin
    public Response addOpenApiType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cname"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                if (JzbTools.isEmpty(param.get("potid"))) {
                    param.put("potid", "00000");
                }
                int add = platformComService.insertOpenApiType(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改开放文档类型
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/modifyOpenApiType", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyOpenApiType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"otid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int add = platformComService.updateOpenApiType(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 移除开放文档类型
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/removeOpenApiType", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeOpenApiType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"otid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("status", "2");
                param.put("uid", userInfo.get("uid"));
                int add = platformComService.updateOpenApiType(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取开放文档类型树
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/getOpenApiType", method = RequestMethod.POST)
    @CrossOrigin
    public Response getOpenApiType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", userInfo.get("uid"));
            List<Map<String, Object>> list = platformComService.getOpenApiType(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(list);

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 添加文档类型接口表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addOpenApiList", method = RequestMethod.POST)
    @CrossOrigin
    public Response addOpenApiList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"curl", "cname", "otid", "apidesc", "reqtype", "apibody"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int add = platformComService.insertOpenApiList(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改文档类型接口表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/modifyOpenApiList", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyOpenApiList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"apiid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int upd = platformComService.updateOpenApiList(param);
                result = upd > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 移除文档类型接口表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/removeOpenApiList", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeOpenApiList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"apiid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("status", "2");
                param.put("uid", userInfo.get("uid"));
                int upd = platformComService.updateOpenApiList(param);
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 模糊查询文档类型接口
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchOpenApiList", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchOpenApiList(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("pagesize", rows);
                param.put("start", rows * (page - 1));
                String start = "starttime";
                if (!JzbTools.isEmpty(param.get(start))) {
                    param.put(start, JzbDataType.getLong(param.get(start)));
                }
                String end = "endtime";
                if (!JzbTools.isEmpty(param.get(end))) {
                    param.put(end, JzbDataType.getLong(param.get(end)));
                }
                List<Map<String, Object>> openApiList = platformComService.searchOpenApiList(param);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(openApiList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = platformComService.searchOpenApiListCou(param);
                    Info.setTotal(size > 0 ? size : openApiList.size());
                }
                result.setPageInfo(Info);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
