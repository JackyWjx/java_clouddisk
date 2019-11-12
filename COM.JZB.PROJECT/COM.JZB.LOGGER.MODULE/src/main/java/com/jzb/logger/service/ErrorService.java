package com.jzb.logger.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.logger.api.message.MessageApi;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/4 18:52
 */
@Service
public class ErrorService {


    @Autowired
    private MessageApi messageApi;
    /**
     * 是否发送短信
     */
    private boolean send;

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    /**
     * 告警警告日志
     *
     * @param client
     * @return void
     * @Author: DingSC
     */
    public void warnErrorLog(RestHighLevelClient client, String index, String email) {
        //报错接口保存临时map
        Map<String, Object> tempError = new ConcurrentHashMap<>(10);
        BoolQueryBuilder boolQ = QueryBuilders.boolQuery();
        //不存在iserror字段的日志信息
        boolQ.mustNot(QueryBuilders.existsQuery("iserror"));
        //设置消息等级为ERROR的数据
        boolQ.must(QueryBuilders.matchQuery("grade", "ERROR"));

        //设置时间范围
        RangeQueryBuilder rangeQ = QueryBuilders.rangeQuery("addtime");
        long now = System.currentTimeMillis();
        //大于和等于
        rangeQ.gte(getNowDay(now - 300000, "yyyy-MM-dd HH:mm:ss-SSS"));
        //小于
        rangeQ.lt(getNowDay(now, "yyyy-MM-dd HH:mm:ss-SSS"));
        boolQ.must(rangeQ);
        //创建封装查询条件参数的对象SearchSourceBuilder，所有的查询条件都会封装到此类。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQ);
        //每个批次实际返回的数量
        sourceBuilder.size(100);
        //创建请求对象，如果不传参数，这将针对所有索引运行，这里搜索多个索引
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(sourceBuilder);
        Scroll scroll = new Scroll(TimeValue.timeValueSeconds(60));
        searchRequest.scroll(scroll);
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            //添加数据到temp
            getErrorLogApi(searchResponse, tempError);
            //分页调用下一页
            //每次循环完后取得scrollId,用于记录下次将从这个游标开始取数
            String scrollId;
            assert searchResponse != null;
            //调用scroll
            do {
                //每次循环完后取得scrollId,用于记录下次将从这个游标开始取数
                scrollId = searchResponse.getScrollId();
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                //添加数据到temp
                getErrorLogApi(searchResponse, tempError);
            } while (searchResponse.getHits().getHits().length != 0);
            //清除分页，释放资源
            //清除滚屏
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            //批量处理索引的字段值
            updateError(client, boolQ, index);

            //发邮件
            sendEmail(tempError, email);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送短信
     *
     * @param
     * @return void
     * @Author: DingSC
     */
    public void sendMessage(Map<String, Object> param) {
        if (send) {
            param.put("groupid", 1020);
            send(param,1);
            setSend(false);
        }
    }

    /**
     * 发送邮件
     *
     * @param tempError
     * @return void
     * @Author: DingSC
     */
    public void sendEmail(Map<String, Object> tempError, String email) {
        if (tempError != null && tempError.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : tempError.keySet()) {
                stringBuilder.append(key + ",");
            }
            String value = stringBuilder.toString();
            int length = value.length();
            if (length > 0) {
                value = value.substring(0, length - 1);
            }
            Map<String, Object> param = new HashMap<>(2);
            param.put("vlaue", value);
            param.put("phone", email);
            param.put("groupid", 1021);
            send(param,2);
            //改发送短信的状态，允许发送短信
            setSend(true);
        }
    }

    /**
     * 发送实现
     *
     * @param param
     * @return void
     * @Author: DingSC
     */
    private void send(Map<String, Object> param,int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("groupid", param.get("groupid"));
        Map<String, Object> codeMap = new HashMap<>(2);
        codeMap.put("vlaue", param.get("vlaue"));
        Map<String, Object> smsMap = new HashMap<>(2);
        if (type == 1) {
            smsMap.put("sms", "{}");
        }else{
            smsMap.put("emil", codeMap);
        }

        //短信发送参数填写
        map.put("sendpara", JSON.toJSONString(smsMap));
        map.put("usertype", "1");
        map.put("title", "计支云");
        Map<String, Object> telMap = new HashMap<>(2);
        telMap.put("photo", param.get("phone"));
        List<Map<String, Object>> telList = new ArrayList<>(1);
        telList.add(telMap);

        if (type == 1) {
            smsMap.put("sms", telList);
        }else{
            smsMap.put("emil", telList);
        }
        map.put("receiver", JSON.toJSONString(smsMap));
        try {
            //加密内容
            String appId = "SADJHJ1FHAUS45FAJ455";
            String secret = "ABSUY0FASD4AA";
            String groupId = JzbDataType.getString(map.get("groupid"));
            String title = JzbDataType.getString(map.get("title"));
            String userType = JzbDataType.getString(map.get("usertype"));
            String receivers = JzbDataType.getString(map.get("receiver"));
            String checkCode = "FAHJKSFHJK400800FHAJK";
            String md5 = JzbDataCheck.Md5(appId + secret + groupId + title + userType + receivers + checkCode);
            map.put("checkcode", md5);
            map.put("appid", appId);
            map.put("secret", secret);
            map.put("msgtag", "00001");
            map.put("senduid", "0000");
            //发送短信
            messageApi.sendShortMsg(map);
        } catch (Exception e) {
            JzbTools.logError(e);
            e.printStackTrace();
        }
    }

    /**
     * 批量添加 iserror字段
     *
     * @param client
     * @param boolQ
     * @param index
     * @return void
     * @Author: DingSC
     */
    private void updateError(RestHighLevelClient client, BoolQueryBuilder boolQ, String index) {

        UpdateByQueryRequest updateByQuery =
                new UpdateByQueryRequest(index);
        updateByQuery.setQuery(boolQ);
        try {
            updateByQuery.setScript(new Script(ScriptType.INLINE,
                    "painless",
                    "ctx._source.iserror='1'",
                    Collections.emptyMap()));
            client.updateByQuery(updateByQuery, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将查询出来的日志接口名称放入临时map中并去重
     *
     * @param search
     * @param tempError
     * @return void
     * @Author: DingSC
     */
    private void getErrorLogApi(SearchResponse search, Map<String, Object> tempError) {
        SearchHit[] sh = search.getHits().getHits();
        int length = sh.length;
        for (int i = 0; i < length; i++) {
            Map<String, Object> hit = sh[i].getSourceAsMap();
            tempError.put(JzbDataType.getString(hit.get("api")), i);
        }
    }

    /**
     * 获取昨天的日期
     *
     * @param start
     * @return java.lang.String
     * @Author: DingSC
     */
    public String getYesterday(String start) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        String day2 = "";
        try {
            date = format.parse(start);
            //得到日历
            Calendar calendar = Calendar.getInstance();
            //把当前时间赋给日历
            calendar.setTime(date);
            //设置为昨天
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            day2 = format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day2;
    }


    /**
     * 根据时间戳获取日期
     *
     * @param time
     * @param pattern
     * @return java.lang.String
     * @Author: DingSC
     */
    public String getNowDay(long time, String pattern) {
        //当前日期
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String now = format.format(time);
        return now;
    }
}
