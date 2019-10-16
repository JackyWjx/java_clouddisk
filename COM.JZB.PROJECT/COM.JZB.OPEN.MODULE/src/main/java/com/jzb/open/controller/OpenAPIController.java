package com.jzb.open.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.entity.open.OpenApiType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.open.api.org.OpenAuthApi;
import com.jzb.open.api.org.OpenOrgApi;
import com.jzb.open.api.org.UserRedisApi;
import com.jzb.open.service.OpenAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业务控制层
 *
 * @author kuangbin
 * @date 2019年7月31日
 */
@RestController
@RequestMapping("open")
public class OpenAPIController {
    @Autowired
    private OpenAPIService openAPIService;

    /**
     * 调用企业信息api接口对象
     */
    @Autowired
    private OpenOrgApi openOrgApi;

    /**
     * 调用企业信息api接口对象
     */
    @Autowired
    private OpenAuthApi openAuthApi;

    /**
     * 调用用户信息Redisapi接口对象
     */
    @Autowired
    private UserRedisApi userRedisApi;

    /**
     * 创建文档类型
     */
    @RequestMapping(value = "/addApiType", method = RequestMethod.POST)
    @CrossOrigin
    public Response addApiType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回是否添加成功
            int count = openAPIService.addApiType(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取文档类型
     */
    @RequestMapping(value = "/getApiType", method = RequestMethod.POST)
    @CrossOrigin
    public Response getApiType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询总数
                count = openAPIService.getApiTypeCount();
            }
            // 文档类型
            List<OpenApiType> records = openAPIService.getApiType(param);

            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(records);
            pageInfo.setTotal(count > 0 ? count : records.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 创建API
     */
    @RequestMapping(value = "/addApi", method = RequestMethod.POST)
    @CrossOrigin
    public Response addApi(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回是否加入成功
            int count = openAPIService.addApi(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取文档类型的API列表
     */
    @RequestMapping(value = "/getApiList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getApiList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取前台传过来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询总数
                count = openAPIService.getApiListCount();
            }
            // 获取API列表list
            List<Map<String, Object>> records = openAPIService.getApiList(param);

            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(records);
            pageInfo.setTotal(count > 0 ? count : records.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取API内容
     */
    @RequestMapping(value = "/getApiContent", method = RequestMethod.POST)
    @CrossOrigin
    public Response getApiContent(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取api内容
            List<Map<String, Object>> records = openAPIService.getApiContent(param);

            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(records);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取使用帮助文档
     */
    @RequestMapping(value = "/getHelper", method = RequestMethod.POST)
    @CrossOrigin
    public Response getHelper(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取前台传过来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 获取数据库总数
                count = openAPIService.getHelperCount();
            }
            List<Map<String, Object>> records = openAPIService.getHelper(param);
            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(records);
            pageInfo.setTotal(count > 0 ? count : records.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询企业信息
     */
    @RequestMapping(value = "/getOrgInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response getOrgInfo(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 调用企业模块查询企业地区信息
            result = openOrgApi.getOrgInfo(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 入驻开放平台(数据库中暂时没有地区表,先不插入地区信息)
     */
    @RequestMapping(value = "/addOpenPlatform", method = RequestMethod.POST)
    @CrossOrigin
    public Response addOpenPlatform(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回是否插入成功
            int insert = openAPIService.addOpenPlatform(param);
            result = insert == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 创建应用
     */
    @RequestMapping(value = "/addApp", method = RequestMethod.POST)
    @CrossOrigin
    public Response addApp(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回是否创建成功
            int count = openAPIService.addApp(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 获取应用列表
     */
    @RequestMapping(value = "/getOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
    public Response getOrgApplication(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取前台传过来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 获取应用列表总数
                count = openAPIService.getApplicationCount(param);
            }
            // 返回所有符合条件的应用
            List<Map<String, Object>> records = openAPIService.getOrgApplication(param);

            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(count > 0 ? count : records.size());
            pageInfo.setList(records);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 获取入驻企业列表
     */
    @RequestMapping(value = "/getPlatformOrg", method = RequestMethod.POST)
    @CrossOrigin
    public Response getPlatformOrg(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取前台传过来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 获取入驻企业总数
                count = openAPIService.getPlatformCount(param);
            }
            List<Map<String, Object>> records = openAPIService.getPlatformOrg(param);
            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(count > 0 ? count : records.size());
            pageInfo.setList(records);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 模糊查询API
     */
    @RequestMapping(value = "/searchApiByName", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchApiByName(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取前台传过来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 获取模糊查询API总数
                count = openAPIService.getApiCount(param);
            }
            // 返回所有API
            List<Map<String, Object>> records = openAPIService.searchOpenApiList(param);
            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(count > 0 ? count : records.size());
            pageInfo.setList(records);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 获取开发者列表
     */
    @RequestMapping(value = "/getAppDeveloperID", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAppDeveloperID(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取开发者ID
            List<Map<String, Object>> records = openAPIService.getAppDeveloper(param);
            if (!JzbTools.isEmpty(records)) {
                for (Map<String, Object> map : records) {
                    // 查询缓存中是否存在用户信息
                    Response userData = userRedisApi.getCacheUserInfo(param);

                    // 加入开发者姓名
                    Object obj = userData.getResponseEntity();
                    if (JzbDataType.isMap(obj)) {
                        Map<Object, Object> mapdata = (Map<Object, Object>) obj;
                        // 加入开发者姓名
                        map.put("cname", mapdata.get("cname"));

                        // 加入开发者联系方式
                        map.put("relphone", mapdata.get("relphone"));
                    }
                }
            }
            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(records);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 添加开发者管理员或者普通开发者
     * 使用电话获取id操作
     */
    @RequestMapping(value = "/addDeveloper", method = RequestMethod.POST)
    @CrossOrigin
    public Response addDeveloper(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

            // 获取当前用户ID,并改成userId以作区别
            param.put("userId", JzbDataType.getString(userInfo.get("uid")));

            // 去缓存中查询用户信息
            String id = JzbDataType.getString(
                    userRedisApi.getNameByPhone(JzbDataType.getString(param.get("phone"))).getResponseEntity());
            param.put("uid", id);
            int count = openAPIService.addDeveloper(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 使用电话获取用户id,name操作
     */
    @RequestMapping(value = "/getNameByPhone", method = RequestMethod.POST)
    @CrossOrigin
    public Response getNameByPhone(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 去缓存中查询用户信息
            String userID = JzbDataType.getString(
                    userRedisApi.getNameByPhone(JzbDataType.getString(param.get("phone"))).getResponseEntity());
            param.put("id", userID);
            // 根据联系方式获取用户id,name
            Object obj = openAuthApi.getUserIdNameByPhone(param).getResponseEntity();
            if (JzbDataType.isMap(obj)) {
                Map<Object, Object> userNameId = (Map<Object, Object>) obj;
                if (userNameId == null && userNameId.size() == 0) {
                    result = Response.getResponseError();
                    result.setResponseEntity("该用户不存在或手机号码输入错误!");
                } else {
                    // 获取用户表中的ID
                    userID = JzbDataType.getString(userNameId.get("uid"));
                    param.put("uid", userID);
                    // 查询是否已经在开发者列表中存在
                    int count = openAPIService.getPhoneCount(param);
                    if (count >= 1) {
                        result = Response.getResponseError();
                        result.setResponseEntity("该人员已加入此应用开发!");
                    } else {
                        // 获取用户资料和token
                        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                        result = Response.getResponseSuccess(userInfo);
                        List<Object> list = new ArrayList();
                        list.add(userNameId);
                        PageInfo pageInfo = new PageInfo();
                        pageInfo.setList(list);
                        result.setPageInfo(pageInfo);
                    }
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 显示企业是否入驻
     */
    @RequestMapping(value = "/getWhetherEnter", method = RequestMethod.POST)
    @CrossOrigin
    public Response getWhetherEnter(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = openAPIService.getWhetherEnter(param);
            // 获取用户资料和token
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改应用信息
     */
    @RequestMapping(value = "/modifyApp", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyApp(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            int count = openAPIService.modifyApp(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 移除开发者
     */
    @RequestMapping(value = "/removeDeveloper", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeDeveloper(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 去缓存中查询用户信息
            String id = JzbDataType.getString(
                    userRedisApi.getNameByPhone(JzbDataType.getString(param.get("phone"))).getResponseEntity());
            // 加入开发者ID
            param.put("userId", id);
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            openAPIService.removeDeveloper(param);
            result = Response.getResponseSuccess(userInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
