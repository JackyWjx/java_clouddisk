package com.jzb.auth.util;

/**
 * 登录类型
 * @author Chad
 * @date 2019年08月27日
 */
public enum LoginType {
    PHONE(1),
    REGID(2),
    CARDID(3),
    MAIL(4),
    WECHAT(5),
    QQ(6),
    DING(7);

    /**
     * 类型
     */
    private final int type;

    /**
     * 初始化类型
     * @param type
     */
    private LoginType(int type) {
        this.type = type;
    } // End LoginType

    /**
     * 获取类型
     * @return
     */
    public int getType() {
        return type;
    } // End getType

    /**
     * 获取类型
     * @param type
     * @return
     */
    public static LoginType getType(int type) {
        LoginType result;
        switch (type) {
            case 1:
                result = PHONE;
                break;
            case 2:
                result = REGID;
                break;
            case 3:
                result = CARDID;
                break;
            case 4:
                result = MAIL;
                break;
            case 5:
                result = WECHAT;
                break;
            case 6:
                result = QQ;
                break;
            case 7:
                result = DING;
                break;
            default:
                result = null;
                break;
        }
        return result;
    } // End getType
} // End enum LoginType