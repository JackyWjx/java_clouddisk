package com.jzb.message.message;

import com.jzb.base.data.JzbDataApp;
import com.jzb.base.util.JzbTools;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件
 */
public class MailMessage implements MssageInfo {

    private final Map<String, Object> mailMessage;

    public MailMessage() {
        this(new HashMap<>());
    } // End MailMessage

    public MailMessage(Map<String, Object> mail) {
        mailMessage = mail;
    } // End MailMessage

    public JzbDataApp getItem(String key) {
        return new JzbDataApp(mailMessage.get(key));
    } // End getItem

    public void setItem(String key, Object value) {
        mailMessage.put(key, value);
    } // End setItem

    @Override
    public boolean isItem(String key) {
        return mailMessage.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return JzbTools.isEmpty(mailMessage);
    }

} // End class MailMessage
