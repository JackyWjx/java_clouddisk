package com.jzb.message.message;

import com.jzb.base.data.JzbDataApp;
import com.jzb.base.util.JzbTools;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信
 */
public class ShortMessage implements MssageInfo {
    private final Map<String, Object> shoryMessage;

    public ShortMessage() {
        this(new HashMap<>());
    } // End ShortMessage

    public ShortMessage(Map<String, Object> mail) {
        shoryMessage = mail;
    } // End ShortMessage

    public JzbDataApp getItem(String key) {
        return new JzbDataApp(shoryMessage.get(key));
    } // End getItem

    public void setItem(String key, Object value) {
        shoryMessage.put(key, value);
    } // End setItem

    @Override
    public boolean isItem(String key) {
        return shoryMessage.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return JzbTools.isEmpty(shoryMessage);
    }
}
