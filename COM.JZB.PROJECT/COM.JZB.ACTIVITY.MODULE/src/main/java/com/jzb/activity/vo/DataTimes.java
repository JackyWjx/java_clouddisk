package com.jzb.activity.vo;

import com.jzb.base.data.JzbDataType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/13 18:35
 */
public class DataTimes {

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        System.out.println(res);
        return res;
    }

    /**
     * 将时间转换为时间戳
     * @param s
     * @return
     * @throws ParseException
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }




    /**
     *  获取一小时前的接口
     */
    public static String beforeToDate(Long s){
        String res;
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm分钟前");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = dateFormat.format(date);
        return res;
    }

    /**
     *  获取一小时后的接口
     */
    public static String laterToDate(Long s){
        String res;
        SimpleDateFormat dateFormat = new SimpleDateFormat("今天 HH小时mm分");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = dateFormat.format(date);
        return res;
    }

    /**
     *  获取昨天时间后的接口
     */
    public static String yesterdayToDate(Long s){
        String res;
        SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH小时mm分");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = dateFormat.format(date);
        return res;
    }

    /**
     *  获取以前时间的接口
     */
    public static String ypreviouslyToDate(Long s){
        String res;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = dateFormat.format(date);
        return res;
    }



    /**
     * 计算时间
     * @param s
     * @return
     */
    public static String getCurrentTime(long s){
        //返回时间
        String reuslt = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Long current = System.currentTimeMillis();

            Calendar calendar = Calendar.getInstance();

            //获取一小时前的时间
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
            String format = simpleDateFormat.format(calendar.getTime());
            long date = JzbDataType.getLong(dateToStamp(format));


            //获取一天的时间
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 24);
            String format1 = simpleDateFormat.format(calendar.getTime());
            long date1 = JzbDataType.getLong(dateToStamp(format));


            //获取昨天的时间
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 48);
            String format2 = simpleDateFormat.format(calendar.getTime());
            long date2 = JzbDataType.getLong(dateToStamp(format));


            if(current > s &&  s  >  date){
                reuslt = beforeToDate(s);
            }else if(date > s && s > date1){
                reuslt = laterToDate(s);
            }else  if(date1 > s && s >date2){
                reuslt = laterToDate(s);
            }else {
                reuslt = ypreviouslyToDate(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  reuslt;
    }

    public static void main(String[] args) throws ParseException {
        String currentTime = getCurrentTime(1565775854914L);
        System.out.println(currentTime);
        long l = System.currentTimeMillis();System.out.println(l);





    }
}
