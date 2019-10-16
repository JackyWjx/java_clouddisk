package com.jzb.message.message;

import com.jzb.base.data.JzbDataApp;
import com.jzb.base.util.JzbTools;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统消息
 */
public class SystemMessage implements MssageInfo {
    private final Map<String, Object> systemMessage;

    public SystemMessage() {
        this(new HashMap<>());
    } // End SystemMessage

    public SystemMessage(Map<String, Object> mail) {
        systemMessage = mail;
    } // End SystemMessage

    public JzbDataApp getItem(String key) {
        return new JzbDataApp(systemMessage.get(key));
    } // End getItem

    public void setItem(String key, Object value) {
        systemMessage.put(key, value);
    } // End setItem

    @Override
    public boolean isItem(String key) {
        return systemMessage.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return JzbTools.isEmpty(systemMessage);
    }
}
