package com.jzb.api.filter;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由配置
 */
public class RouteConfig {
    /**
     * 配置
     */
    private Map<String, Object> config;

    /**
     * 默认构造
     */
    public RouteConfig() {
        this(new HashMap<>());
    } // End RouteConfig

    /**
     * 构造方法
     * @param config
     */
    public RouteConfig(Map<String, Object> config) {
        this.config = config;
    } // End RouteConfig

    /**
     * 获取路由ID
     * @return
     */
    public String getRouteId() {
        return getStringValue("routeid");
    } // End getRouteId

    /**
     * 设置路由ID
     * @param id
     */
    public void setRouteId(String id) {
        config.put("routeid", id);
    } // End setRouteId

    /**
     * 获取路径
     * @return
     */
    public String getPath() {
        return getStringValue("path");
    } // End getPath

    /**
     * 设置路径
     * @param path
     */
    public void setPath(String path) {
        config.put("path", path);
    } // End setPath

    /**
     * 获取重定向地址
     * @return
     */
    public String getRepath() {
        return getStringValue("repath");
    } // End getRepath

    /**
     * 设置重定向地址
     * @param path
     */
    public void setRepath(String path) {
        config.put("repath", path);
    } // End setRepath

    /**
     * 获取组
     * @return
     */
    public String getGroup() {
        return getStringValue("group");
    } // End getGroup

    /**
     * 设置组
     * @param group
     */
    public void setGroup(String group) {
        config.put("group", group);
    } // End setGroup

    /**
     * 获取权重
     * @return
     */
    public int getWeight() {
        return JzbDataType.getInteger(config.get("weight"));
    } // End getWeight

    /**
     * 设置权重
     * @param weight
     */
    public void setWeight(int weight) {
        config.put("weight", weight);
    } // End setWeight

    /**
     * 获取URI
     * @return
     */
    public String getUri() {
        return getStringValue("uri");
    } // End getUri

    /**
     * 设置URI
     * @param uri
     */
    public void setUri(String uri) {
        config.put("uri", uri);
    } // End setUri

    /**
     * 获取字符串结果
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        return config.containsKey(key) ? config.get(key).toString() : "";
    } // End getStringValue

    public static List<RouteConfig> getRouteConfigs() {
        List<RouteConfig> configs = new ArrayList<>();
        RouteConfig auth = new RouteConfig();
        auth.setRouteId("jzb-auth");
        auth.setPath("/JZB-AUTH/**");
        auth.setGroup("auth");
        auth.setWeight(100);
        auth.setUri("http://localhost:9000/JZB-AUTH");
        auth.setRepath("JZB-AUTH");
        configs.add(auth);

        RouteConfig activity = new RouteConfig();
        activity.setRouteId("jzb-activity");
        activity.setPath("/JZB-ACTIVITY/**");
        activity.setGroup("activity");
        activity.setWeight(100);
        activity.setUri("http://localhost:9000/JZB-ACTIVITY");
        activity.setRepath("JZB-ACTIVITY");
        configs.add(activity);

        RouteConfig logger = new RouteConfig();
        logger.setRouteId("jzb-logger");
        logger.setPath("/JZB-LOGGER/**");
        logger.setGroup("logger");
        logger.setWeight(100);
        logger.setUri("http://localhost:9000/JZB-LOGGER");
        logger.setRepath("JZB-LOGGER");
        configs.add(logger);

        RouteConfig media = new RouteConfig();
        media.setRouteId("jzb-media");
        media.setPath("/JZB-MEDIA/**");
        media.setGroup("media");
        media.setWeight(100);
        media.setUri("http://localhost:9000/JZB-MEDIA");
        media.setRepath("JZB-MEDIA");
        configs.add(media);

        RouteConfig message = new RouteConfig();
        message.setRouteId("jzb-message");
        message.setPath("/JZB-MESSAGE/**");
        message.setGroup("message");
        message.setWeight(100);
        message.setUri("http://localhost:9000/JZB-MESSAGE");
        message.setRepath("JZB-MESSAGE");
        configs.add(message);

        RouteConfig open = new RouteConfig();
        open.setRouteId("jzb-open");
        open.setPath("/JZB-OPEN/**");
        open.setGroup("open");
        open.setWeight(100);
        open.setUri("http://localhost:9000/JZB-OPEN");
        open.setRepath("JZB-OPEN");
        configs.add(open);

        RouteConfig org = new RouteConfig();
        org.setRouteId("jzb-org");
        org.setPath("/JZB-ORG/**");
        org.setGroup("org");
        org.setWeight(100);
        org.setUri("http://localhost:9000/JZB-ORG");
        org.setRepath("JZB-ORG");
        configs.add(org);

        RouteConfig resource = new RouteConfig();
        resource.setRouteId("jzb-resource");
        resource.setPath("/JZB-RESOURCE/**");
        resource.setGroup("resource");
        resource.setWeight(100);
        resource.setUri("http://localhost:9000/JZB-RESOURCE");
        resource.setRepath("JZB-RESOURCE");
        configs.add(resource);

        return configs;
    }
} // End class RouteConfig
