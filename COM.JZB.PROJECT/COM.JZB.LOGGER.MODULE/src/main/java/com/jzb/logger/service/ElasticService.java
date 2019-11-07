package com.jzb.logger.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import com.jzb.logger.dao.ElasticMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
 * @Date: 2019/10/30 11:10
 */
@Service
public class ElasticService {

    @Autowired
    private ElasticMapper elasticMapper;

    /**
     * 处理数据外接方法
     *
     * @param map
     * @return void
     * @Author: DingSC
     */
    public int dealElValue(Map<String, Object> map) {
        int result = 1;
        String index = JzbDataType.getString(map.get("index"));
        String hostName = JzbDataType.getString(map.get("hostName"));
        int port = 9200;
        String scheme = JzbDataType.getString(map.get("scheme"));
        HttpHost httpHost = new HttpHost(hostName, port, scheme);
        //创建RestHighLevelClient
        //如果是集群，添加所有的HttpHost对象（以英文逗号分隔），RestClient.builder()函数的参数是数组。
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(httpHost));

        //获取当前日期
        String pattern = "yyyy-MM-dd";
        long nowDay = System.currentTimeMillis();
        //当前日期
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String now = format.format(nowDay);
        //查询的条件添加
        Map<String, Object> param = new HashMap<>();
        param.put("isdeal", map.get("isdeal"));
        //统计开始时间，固定日期
        String selDate = elasticMapper.queryRequestLog();
        String start;
        if (JzbTools.isEmpty(selDate)) {
            start = "2019-10-23";
        } else {
            start = selDate.substring(0, 10);
        }
        try {
            Map<String, Map<String, Object>> temp = new ConcurrentHashMap<>(100);
            do {
                //开始计时
                String indexName = index + start;
                boolean flag = isExists(client, indexName);
                if (flag) {
                    //开始查询数据
                    //初始化scroll,设置滚动时间间隔，默认值60s
                    //设定滚动时间间隔,60秒,不是处理查询结果的所有文档的所需时间
                    //游标查询的过期时间会在每次做查询的时候刷新，所以这个时间只需要足够处理当前批的结果就可以了
                    Scroll scroll = new Scroll(TimeValue.timeValueSeconds(60));
                    //第一次调用
                    SearchResponse searchResponse = getSResByScroll(param, indexName, client, scroll);
                    List<Map<String, Object>> elasticList = getListBySearch(searchResponse);
                    //数据添加到数据库
                    addSQL(elasticList, temp);

                    //分页调用下一页
                    //每次循环完后取得scrollId,用于记录下次将从这个游标开始取数
                    String scrollId;
                    assert searchResponse != null;
                    //调用scroll
                    do {
                        //每次循环完后取得scrollId,用于记录下次将从这个游标开始取数
                        scrollId = searchResponse.getScrollId();
                        searchResponse = getERByScrollId(scrollId, client, scroll);
                        elasticList = getListBySearch(searchResponse);
                        //数据添加到数据库
                        addSQL(elasticList, temp);

                    } while (searchResponse.getHits().getHits().length != 0);
                    //清除分页，释放资源
                    boolean isClear = clearScroll(scrollId, client);
                    //批量处理索引的字段值
                    updateBatchElastic(client, indexName, param);
                }
                //开始下一天的日志
                start = getNextDay(start);
                //处理超过30分钟的接口
                dealTempMap(temp, start);
            } while (dateToStamp(now, pattern) - dateToStamp(start, pattern) >= 0);
            //假如temp还有数据，将今天的请求数据改变状态
            if (temp.size() > 0) {
                //已处理，改为未处理
                dealTempStatus(temp, client, index + now);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = 2;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    result = 2;
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 将临时表中的数据状态改回
     *
     * @param temp
     * @param client
     * @param indexName
     * @return void
     * @Author: DingSC
     */
    private void dealTempStatus(Map<String, Map<String, Object>> temp, RestHighLevelClient client,
                                String indexName) {
        for (String key : temp.keySet()
        ) {
            updateElastic(client, indexName, JzbDataType.getString(temp.get(key).get("esid")), "1");
        }
    }

    /**
     * 处理临时map中遗留下的请求数据
     *
     * @param temp
     * @param start
     * @return void
     * @Author: DingSC
     */
    private void dealTempMap(Map<String, Map<String, Object>> temp, String start) {
        int size = temp.size();
        //索引创建时区原因加8小时的毫秒值
        long endTime = dateToStamp(start, "yyyy-MM-dd") + 28800000;
        List<Map<String, Object>> insertList = new ArrayList<>(size);
        for (String key : temp.keySet()) {
            Map<String, Object> reqMap = temp.get(key);
            long request = dateToStamp(JzbDataType.getString(reqMap.get("addtime")), "yyyy-MM-dd HH:mm:ss-SSS");
            long times = endTime - request;
            if (times > 28800000) {
                reqMap.put("status", 1);
                Map<String, Object> insertMap = toInsertMap(reqMap, 28800000);
                insertList.add(insertMap);
            }
        }
        //将过滤出的数据放入数据库中
        insertRequestApiLog(insertList);
    }

    /**
     * 处理查询出的elastic数据，将符合条件的数据插入数据库
     *
     * @param list
     * @param temp
     * @return void
     * @Author: DingSC
     */
    private void addSQL(List<Map<String, Object>> list, Map<String, Map<String, Object>> temp) {
        int size = list.size();
        //日志格式
        String logPat = "yyyy-MM-dd HH:mm:ss-SSS";
        List<Map<String, Object>> insertList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Map<String, Object> elMap = list.get(i);
            String esId = JzbDataType.getString(elMap.get("esid"));
            String cid = JzbDataType.getString(elMap.get("cid"));
            String api = JzbDataType.getString(elMap.get("api"));
            String token = JzbDataType.getString(elMap.get("token"));
            String uid = JzbDataType.getString(elMap.get("uid"));
            String sign = JzbDataType.getString(elMap.get("sign"));
            int type = JzbDataType.getInteger(elMap.get("type"));
            String addTime = JzbDataType.getString(elMap.get("addtime"));
            //拼接key
            String key = cid + api + token + uid + sign;
            if (type == 1) {
                //请求日志
                temp.put(key, elMap);
            } else if (type == 2) {
                //响应日志
                if (JzbDataType.isMap(temp.get(key))) {
                    Map<String, Object> reqMap = temp.get(key);
                    long request = dateToStamp(JzbDataType.getString(reqMap.get("addtime")), logPat);
                    long repose = dateToStamp(addTime, logPat);
                    long times = repose - request;
                    //如果请求响应时间超过一秒钟
                    if (times > 1000) {
                        reqMap.put("status", 1);
                        Map<String, Object> insertMap = toInsertMap(reqMap, JzbDataType.getInteger(times));
                        insertList.add(insertMap);
                    }
                    //删除临时map中的请求数据
                    temp.remove(key);
                } else {
                    //没有请求日志
                    //暂不做处理
                }
            }
        }
        //将过滤出的数据放入数据库中
        insertRequestApiLog(insertList);
    }

    /**
     * 获取插入数据库的map
     *
     * @param map
     * @param times
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     */
    private Map<String, Object> toInsertMap(Map<String, Object> map, int times) {
        Map<String, Object> result = new HashMap<>(10);
        result.put("cid", map.get("cid"));
        result.put("pid", map.get("pid"));
        result.put("uid", map.get("uid"));
        result.put("reqip", map.get("ipaddress"));
        result.put("reqtime", map.get("addtime"));
        result.put("reqapi", map.get("api"));
        result.put("times", times);
        result.put("status", map.get("status"));
        result.put("summary", map.get("meesage"));
        return result;
    }

    /**
     * 批量新加请求日志
     *
     * @param list
     * @return void
     * @Author: DingSC
     */
    private void insertRequestApiLog(List<Map<String, Object>> list) {
        if (list.size() > 0) {
            elasticMapper.insertRequestApiLog(list);
        }
    }


    /**
     * 根据索引批量更新数据
     *
     * @param client
     * @param indexName
     * @param param     修改限制条件
     * @return void
     * @Author: DingSC
     */
    private void updateBatchElastic(RestHighLevelClient client, String indexName, Map<String, Object> param) {
        UpdateByQueryRequest updateByQuery =
                new UpdateByQueryRequest(indexName);
        for (Map.Entry<String, Object> map : param.entrySet()
        ) {
            updateByQuery.setQuery(new TermQueryBuilder(map.getKey(), map.getValue()));
        }
        updateByQuery.setScript(new Script(ScriptType.INLINE,
                "painless",
                "ctx._source['isdeal']='2'",
                Collections.emptyMap()));
        try {
            BulkByScrollResponse bulkResponse =
                    client.updateByQuery(updateByQuery, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新isdeal字段
     *
     * @param client
     * @param indexName
     * @param esId
     * @return void
     * @Author: DingSC
     */
    public void updateElastic(RestHighLevelClient client, String indexName, String esId, String isDeal) {
        UpdateRequest request = new UpdateRequest(indexName, esId).doc("isdeal", isDeal);
        try {
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            JzbTools.logError(e);
        }
    }

    /**
     * 清除分页，释放资源
     *
     * @param scrollId
     * @param client
     * @return boolean
     * @Author: DingSC
     */
    private static boolean clearScroll(String scrollId, RestHighLevelClient client) {
        //清除滚屏
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        //也可以选择setScrollIds()将多个scrollId一起使用
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = null;
        try {
            clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            JzbTools.logError("清除滚屏错误 ->", e);
        }
        boolean succeeded = false;
        if (clearScrollResponse != null) {
            succeeded = clearScrollResponse.isSucceeded();
        }
        return succeeded;
    }

    /**
     * 通过scrollId获取下一页数据
     *
     * @param scrollId
     * @return org.elasticsearch.action.search.SearchResponse
     * @Author: DingSC
     */
    public static SearchResponse getERByScrollId(String scrollId, RestHighLevelClient client, Scroll scroll) {
        SearchResponse searchResponse = null;
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(scroll);
        try {
            //进行下次查询
            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            JzbTools.logError("获取数据错误2 ->", e);
            e.printStackTrace();
        }
        return searchResponse;
    }

    /**
     * 获取el数据根据scroll分页第一次查询
     *
     * @param map       查询条件
     * @param indexName 索引名称
     * @param client
     * @param scroll    分页时间
     * @return
     */
    private static SearchResponse getSResByScroll(Map<String, Object> map, String indexName,
                                                  RestHighLevelClient client, Scroll scroll) {
        //储存条件
        BoolQueryBuilder boolQ = QueryBuilders.boolQuery();
        // 循环添加多个条件
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            boolQ.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
        }
        //创建封装查询条件参数的对象SearchSourceBuilder，所有的查询条件都会封装到此类。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQ);
        //每个批次实际返回的数量
        sourceBuilder.size(100);
        //对数据进行排序
        sourceBuilder.sort("api", SortOrder.ASC);
        sourceBuilder.sort("type", SortOrder.ASC);

        //创建请求对象，如果不传参数，这将针对所有索引运行，这里搜索多个索引
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        searchRequest.scroll(scroll);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }


    /**
     * 判断索引是否存在：
     *
     * @param client
     * @param indexName
     * @return boolean
     * @Author: DingSC
     */
    public boolean isExists(RestHighLevelClient client, String indexName) {
        boolean result;
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            result = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 将时间转换为时间戳
     *
     * @param str
     * @param pattern
     * @return java.lang.String
     * @Author: DingSC
     */
    public static long dateToStamp(String str, String pattern) {
        long res;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = simpleDateFormat.parse(str);
            res = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }

    /**
     * 获取当前天数的第二天
     *
     * @param start
     * @return java.lang.String
     * @Author: DingSC
     */
    private String getNextDay(String start) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //得到日历
        Calendar calendar = Calendar.getInstance();
        //把当前时间赋给日历
        calendar.setTime(date);
        //设置为后一天
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        String day2 = format.format(calendar.getTime());
        return day2;
    }

    /**
     * 处理返回数据
     *
     * @param response
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    private static List<Map<String, Object>> getListBySearch(SearchResponse response) {
        SearchHit[] sh = response.getHits().getHits();
        int length = sh.length;
        List<Map<String, Object>> result = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            Map<String, Object> hit = sh[i].getSourceAsMap();
            //添加数据的唯一id
            hit.put("esid", sh[i].getId());
            result.add(hit);
        }
        return result;
    }

    public List<Map<String, Object>> queryApiTimeLog(Map<String, Object> map) {
        return elasticMapper.queryApiTimeLog(map);
    }

}
