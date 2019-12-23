package com.jzb.open.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.entity.open.OpenApiType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.open.dao.OpenAPIMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * api服务类
 *
 * @author kuangbin
 */
@Service
public class OpenAPIService {
    /**
     * api数据库操作对象
     */
    @Autowired
    private OpenAPIMapper mapper;

    @Autowired
    private PlatformComService platformComService;
    /**
     * 创建文档类型
     */
    public int addApiType(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间毫秒数
            long authtime = System.currentTimeMillis();

            // 加入文档类型ID
            param.put("otid", JzbRandom.getRandomCharCap(5));

            // 加入创建时间
            param.put("addtime", authtime);

            // 加入修改时间
            param.put("updtime", authtime);
            count = mapper.insertApiType(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 获取文档类型
     *
     * @return
     */
    public List<Map<String, Object>> getApiType(Map<String, Object> param) {
        List<Map<String, Object>> result;
        // 设置分页参数
        param = setPageSize(param);
        List<Map<String, Object>> records = mapper.queryApiType(param);
        /*result = new ArrayList<>(records.size());
        for (int i = 0, l = records.size(); i < l; i++) {
            OpenApiType name = new OpenApiType();
            name.addData(records.get(i));
            result.add(name);
        }*/
        return records;
    } // End getApiType

    /**
     * 创建API
     */
    public int addApi(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间毫秒数
            long authtime = System.currentTimeMillis();

            // 获取随机7为字符ID
            param.put("apiid", JzbRandom.getRandomCharCap(7));
            param.put("addtime", authtime);
            param.put("updtime", authtime);
            count = mapper.insertApi(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    } // End addApi

    /**
     * 获取文档类型的API列表
     *
     * @return
     */
    public List<Map<String, Object>> getApiList(Map<String, Object> param) {
        // 设置分页参数
        param = setPageSize(param);
        return mapper.queryApiList(param);
    } // End getApiList

    /**
     * 获取API内容
     *
     * @return
     */
    public List<Map<String, Object>> getApiContent(Map<String, Object> param) {
        return mapper.queryApiContent(param);
    } // End getApiContent


    /**
     * 获取使用帮助文档
     *
     * @return
     */
    public List<Map<String, Object>> getHelper(Map<String, Object> param) {
        int maybe = JzbDataType.getInteger(param.get("maybe"));
        List<Map<String, Object>> list;
        // 判断页数是否合理并操作
        if (maybe == 1) {
            param = setPageSize(param);
            list = mapper.queryHelperTitle(param);
        }else {
            list = mapper.queryHelperContent(param);
        }
        return list;
    } // End getApiType

    /**
     * 入驻开放平台
     *
     * @param param
     * @return
     */
    public int addOpenPlatform(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间
            long authtime = System.currentTimeMillis();
            // 加入时间
            param.put("addtime", authtime);
            count = mapper.insertOpenPlatform(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 获取api类型的总数
     *
     * @return
     */
    public int getApiTypeCount() {
        int count;
        try {
            count = mapper.queryApiTypeCount();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 获取文档类型的API列表的总数
     *
     * @return
     */
    public int getApiListCount() {
        int count;
        try {
            count = mapper.queryApiListCount();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 设置分页数
     */
    public Map<String, Object> setPageSize(Map<String, Object> param) {
        // 获取参数中的页数
        int pageno = JzbDataType.getInteger(param.get("pageno"));

        // 获取参数中每页显示大小
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));

        // 判断每页显示大小是否合理
        pagesize = pagesize <= 0 ? 15 : pagesize;

        // 判断页数是否合理
        pageno = pageno <= 0 ? 1 : pageno;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }

    /**
     * 获取使用帮助文档总数
     *
     * @return
     */
    public int getHelperCount() {
        int count;
        try {
            count = mapper.queryHelperCount();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 创建应用
     *
     * @param param
     * @return
     */
    public int addApp(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间毫秒数
            long authtime = System.currentTimeMillis();

            // 随机获取11位字符串ID做为AppID
            param.put("appid", JzbRandom.getRandomCharCap(11));
            param.put("appkey", JzbRandom.getRandomCharCap(16));
            param.put("appsecret", JzbRandom.getRandomCharCap(32));
            param.put("addtime", authtime);
            param.put("updtime", authtime);
            String key = "apptype";
            if (!JzbTools.isEmpty(param.get(key))) {
                param.put(key,JzbDataType.getInteger(param.get(key)));
            }
            count = mapper.insertApp(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /***
     * 获取应用列表
     */
    public List<Map<String, Object>> getOrgApplication(Map<String, Object> param) {
        // 设置分页参数
        //param = setPageSize(param);
        return mapper.queryRogApplication(param);
    }
    /***
     * 获取应用列表
     */
    public List<Map<String, Object>> getOrgApplications(Map<String, Object> param) {
        // 设置分页参数
        //param = setPageSize(param);
        return mapper.queryRogApplications(param);
    }
    /***
     * 获取入驻企业列表
     */
    public List<Map<String, Object>> getPlatformOrg(Map<String, Object> param) {
        // 设置分页参数
        param = setPageSize(param);
        return mapper.queryPlatformOrg(param);
    }

    /***
     * 模糊查询API
     */
    public List<Map<String, Object>> searchOpenApiList(Map<String, Object> param) {
        // 设置分页参数
        param = setPageSize(param);
        return mapper.searchApiByName(param);
    }

    /***
     * 获取开发者列表
     */
    public List<Map<String, Object>> getAppDeveloper(Map<String, Object> param) {
        // 设置分页参数
        param = setPageSize(param);
        return mapper.queryAppDeveloper(param);
    }

    /**
     * 添加/移除开发者
     *
     * @param param
     * @return
     */
    public int addDeveloper(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间毫秒数
            Long addtime = System.currentTimeMillis();
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            // 获取前台添加权限是加入管理员还是普通开发者
            int devauth = JzbDataType.getInteger(param.get("devauth"));
            if (devauth == 1) {
                // 修改当前应用的管理员为普通开发者
                mapper.updateDevauth(param);
            }
            // 根据开发者iD查询数据库中是否存在当前开发者,状态为2或者1的的开发者
            int countId = mapper.queryIdCount(param);
            if (countId >= 1) {
                // 把状态为2的开发者转换为状态1
                count = mapper.updateStatus(param);
            } else {
                count = mapper.insertDeveloper(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 验证是否已加入开发者列表
     *
     * @param param
     */
    public int getPhoneCount(Map<String, Object> param) {
        int count;
        try {
            count = mapper.queryPhoneCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 显示企业是否入驻
     *
     * @param param
     * @return
     */
    public int getWhetherEnter(Map<String, Object> param) {
        int count;
        try {
            count = mapper.queryWhetherEnter(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 修改应用信息
     *
     * @param param
     * @return
     */
    public int modifyApp(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间毫秒数
            long updtime = System.currentTimeMillis();
            // 加入修改时间
            param.put("updtime", updtime);
            count = mapper.updateApp(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 移除开发者
     */
    public int removeDeveloper(Map<String, Object> param) {
        return mapper.updateDeveloper(param);
    }

    /***
     * 获取应用列表总数
     */
    public int getApplicationCount(Map<String, Object> param) {
        int count;
        try {
            count = mapper.queryApplicationCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /***
     * 获取入驻企业总数
     */
    public int getPlatformCount(Map<String, Object> param) {
        int count;
        try {
            count = mapper.queryPlatformCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /***
     * 获取模糊查询API总数
     */
    public int getApiCount(Map<String, Object> param) {
        int count;
        try {
            count = mapper.queryApiCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 查询应用管理员
     * @param map1
     * @return
     */
    public List<Map<String, Object>> getAppDevelopers(Map<Object, Object> map1) {
        return mapper.getAppDevelopers(map1);
    }
}
