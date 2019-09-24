package com.jzb.message.message;

import com.jzb.base.data.JzbDataApp;

/**
 * 公用接口类
 */
public interface MssageInfo {

     JzbDataApp getItem(String key);

     void setItem(String key, Object value);

     boolean isItem(String key);

     boolean isEmpty();
}
