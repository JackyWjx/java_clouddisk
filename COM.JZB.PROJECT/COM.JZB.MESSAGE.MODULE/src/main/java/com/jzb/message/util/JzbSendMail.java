package com.jzb.message.util;

import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.util.JzbTools;
import net.sf.json.JSONObject;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @Description: 邮件工具类
 * @Author Han Bin
 */
public final class JzbSendMail {
    /**
     * 私有构造 不允许实例
     */
    private JzbSendMail(){

    }

    /***
     *  发送邮件
     */
    public static boolean sendMime(JSONObject json)throws Exception{
        boolean result;
        try{
            //连接邮件服务器的参数配置
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", json.getString("validate"));
            props.setProperty("mail.transport.protocol", json.getString("smtp"));
            props.setProperty("mail.smtp.host", json.getString("smtphost"));
            props.setProperty("mail.smtp.port", json.getString("port"));
            Session session = Session.getInstance(props);
            Message msg = getMimeMessage(session,json);
            if(!JzbTools.isEmpty(msg)){
                Transport transport = session.getTransport();
                //设置发件人的账户名和密码
                transport.connect(json.getString("userName"), json.getString("passWord"));
                transport.sendMessage(msg,msg.getAllRecipients());
                transport.close();
                result = true;
            }else{
                result = false;
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    /***
     *  创建一封邮件
     */
    private static MimeMessage getMimeMessage(Session session,JSONObject json) throws Exception{
        MimeMessage msg = new MimeMessage(session);
        try{
            msg.setFrom(new InternetAddress(json.getString("userName")));
            if(!JzbTools.isEmpty(json.getString("receiver"))){
                String[] strings = json.getString("receiver").split(",");
                for(int i = 0; i <strings.length ;i++){
                    msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(strings[i]));
                }
            }
            if(!JzbTools.isEmpty(json.getString("copier"))){
                String[] strings = json.getString("copier").split(",");
                for(int i = 0; i < strings.length ;i++){
                    msg.setRecipient(MimeMessage.RecipientType.CC,new InternetAddress(strings[i]));
                }
            }
            if(!JzbTools.isEmpty(json.getString("secretSendOff"))){
                String[] strings = json.getString("secretSendOff").split(",");
                for(int i = 0; i < strings.length ;i++){
                    msg.setRecipient(MimeMessage.RecipientType.BCC,new InternetAddress(strings[i]));
                }
            }
            //设置参数
            msg.setSubject(json.getString("title"),"UTF-8");
            MimeBodyPart text = new MimeBodyPart();
            if(json.getString("ishtml").equals("true")){
                text.setContent(json.getString("html"), "text/html;charset=UTF-8");
            }else{
                text.setText(json.getString("html"));
            }
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            msg.setContent(mm);
            msg.setSentDate(new Date());
            if(!JzbTools.isEmpty(json.getString("datetime"))){
                msg.setSentDate(JzbDateUtil.getDate(json.getString("datetime"), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
            }
        }catch (Exception e){
            msg = null;
        }
        return msg;
    }

}
