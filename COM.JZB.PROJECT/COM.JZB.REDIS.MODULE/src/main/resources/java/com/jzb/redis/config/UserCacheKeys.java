package com.jzb.redis.config;

/**
 * 用户模块Redis缓存的KEY定义
 * @author Chad
 * @date 2019年7月20日
 */
public final class UserCacheKeys {
    /**
     * 私有构造函，不允许实例化
     */
    private UserCacheKeys() {
    } // End UserCacheKeys

    /**
     * 用户信息缓存，键名加UID做为缓存键值。
     */
    public final static String JZB_USER_INFO_CACHE = "jzb.user.info.";

    /**
     * 用户信息缓存，键名加UID做为缓存键值。
     */
    public final static String JZB_USER_TOKEN_CACHE = "jzb.user.token.";

    /**
     * 用户模块缓存，ID和NAME的映射键名
     */
    public final static String JZB_USER_ID_TO_NAME = "jzb.user.idname";

    /**
     * 用户模块缓存，ID和NAME的映射键名
     */
    public final static String JZB_PHONE_TO_USER_ID = "jzb.user.phoneuid";

    /**
     * 配置类的键
     */
    public final static String JZB_RESOURCE_TYPE_TABLE = "jzb.config.resource.type.table";
} // End class UserCacheKeys
