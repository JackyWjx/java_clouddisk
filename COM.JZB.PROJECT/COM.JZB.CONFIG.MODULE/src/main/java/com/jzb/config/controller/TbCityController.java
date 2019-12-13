package com.jzb.config.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.config.service.TbCityService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/config/city")
public class TbCityController {

    @Autowired
    private TbCityService tbCityService;

//    public static <T> String exportCsv(List<Map<Integer, String>> list) throws IOException, IllegalArgumentException {
//        File file = new File("C:\\Users\\john\\Desktop\\test2.csv");
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        //构建输出流，同时指定编码
//        OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(file), "gbk");
//
//        for (int i = 0, l = list.size(); i < l; i++) {
//            ow.write(list.get(i).get(1) + "," + list.get(i).get(3) + "," + list.get(i).get(5) + ",");
//            ow.write("\r\n");
//        }
//        ow.flush();
//        ow.close();
//        return "0";
//    }
//
//    @RequestMapping(value = "/readExcel", method = RequestMethod.POST)
//    @ResponseBody
//    public int coty() {
//        String filePath = "C:\\Users\\john\\Desktop\\newCity.xlsx";
//        List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filePath);
//        String pid = "10";
//        String cname = "长的";
//        String qname = "长的";
//        List<Map<String, Object>> result = new ArrayList<>();
//        int cid = 0;
//        int qid = 0;
//        for (int i = 0, l = list.size(); i < l; i++) {
//            Map map = new HashMap();
//            if (list.get(i).get(3).equals(cname)) {
//                cid = cid;
//            } else {
//                cid++;
//                cname = list.get(i).get(3);
//                qid = 0;
//            }
//            if (list.get(i).get(5).equals(qname)) {
//                qid = qid;
//            } else {
//                qid++;
//                qname = list.get(i).get(5);
//            }
//            map.put("creaid", pid + (JzbDataType.getString(cid).length() == 1 ? "0" + cid : cid) + (JzbDataType.getString(qid).length() == 1 ? "0" + qid : qid) + "00");
//            map.put("pcode", JzbDataType.getInteger(list.get(i).get(0)));
//            map.put("province", list.get(i).get(1));
//            map.put("ccode", JzbDataType.getInteger(list.get(i).get(2)));
//            map.put("city", list.get(i).get(3));
//            map.put("tcode", JzbDataType.getInteger(list.get(i).get(4)));
//            map.put("county", list.get(i).get(5));
//            result.add(map);
//        }
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        int count = tbCityService.addCityList(result);
//        return count;
//    }
//

    /**
     * 获取省市县   (淘汰)
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCityListJon", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getCityListJon(@RequestBody(required = false) Map<String, Object> params) {
        Response result;
        try {
            // 结果集
            List<Map<String, Object>> list = tbCityService.getCityList(params);

            // 返回map
            Map<String, List<Map<String, List<Map<String, Object>>>>> resultMap = new HashMap<>();

            Map<String, Object> map11 = new HashMap<>();
            //  begin  给省提供
            List<Map<String, List<Map<String, Object>>>> provinceList = new ArrayList<>();

            Map<String, List<Map<String, Object>>> provinceMap = new HashMap<>();

            List<Map<String, Object>> provinceListSon = new ArrayList<>();
            //  end    给省提供

            // begin 给市提供
            Map<String, List<Map<String, Object>>> cityMap = new HashMap<>();

            List<Map<String, Object>> citylist = new ArrayList<>();

            // end 给市提供

//            定义湖南省id
            String cid = "10000000";

            // 遍历结果集
            for (int i = 0, l = list.size(); i < l; i++) {

                // 如果ccode为空则是省
                if (JzbTools.isEmpty(list.get(i).get("ccode"))) {

                    //  取值
                    Map<String, Object> map1 = list.get(i);

                    //  去除无用的字段
                    map1.remove("tcode");
                    map1.remove("ccode");
                    map1.remove("city");
                    map1.remove("county");
                    map1.remove("idx");
                    // 按照需求格式添加数据
                    provinceListSon.add(map1);

                    // 按照需求格式添加数据
                    provinceMap.put("list", provinceListSon);

                    // 按照需求格式添加数据
                    provinceList.add(provinceMap);

                    // 按照需求格式添加数据
                    resultMap.put("list", provinceList);

                    // 如果tcode为空则是市
                } else if (JzbTools.isEmpty(list.get(i).get("tcode"))) {
//                    获取pid
                    String pid = list.get(i).get("creaid").toString().substring(0, 2) + "000000";

                    // 市级用
                    List<Map<String, List<Map<String, Object>>>> cityListSon = new ArrayList<>();
                    Map<String, Object> cityMapSon = list.get(i);

                    //  去除无用的字段
                    cityMapSon.remove("tcode");
                    cityMapSon.remove("county");
                    cityMapSon.remove("idx");

                    // 按照需求格式添加数据
                    citylist.add(cityMapSon);

                    // 按照需求格式添加数据
                    cityMap.put("list", citylist);

                    // 按照需求格式添加数据
                    cityListSon.add(cityMap);

                    // 按照需求格式添加数据
                    resultMap.put(pid, cityListSon);

                    // 如果tcode不为空则是县
                } else if (!JzbTools.isEmpty(list.get(i).get("tcode")) && !JzbTools.isEmpty(list.get(i).get("ccode"))) {

                    //   获取市id
                    String pid = list.get(i).get("creaid").toString().substring(0, 4) + "0000";

                    Map<String, String> countyMapSon = new HashMap<>();

                    List<Map<String, Object>> countyList = new ArrayList<>();

                    Map<String, Object> countyMap = new HashMap<>();

                    for (int aa = 0, bb = list.size(); aa < bb; aa++) {
                        if ((list.get(aa).get("creaid").toString().substring(0, 4) + "0000").equals(pid) && list.get(aa).get("tcode") != null) {
                            if (!countyMapSon.containsKey(list.get(aa).get("creaid"))) {
                                Map<String, Object> map1 = list.get(aa);
                                countyList.add(map1);
                                countyMap.put("list", countyList);
                                countyMapSon.put(list.get(aa).get("creaid").toString(), "1");
                            }
                        }
                    }
                    List<Map<String, Object>> countryListChild = new ArrayList<>();
                    countryListChild.add(countyMap);
                    resultMap.get(cid).get(0).put(pid, countryListChild);
                }
            }
            // 定义返回结果
            result = Response.getResponseSuccess();
            map11.put(cid, resultMap.get(cid));
            if (resultMap.get("list") != null) {
                map11.put("list", resultMap.get("list").get(0).get("list"));
            } else {
                map11.put("list", resultMap.get("list"));
            }

            result.setResponseEntity(map11);

        } catch (Exception e) {

            e.printStackTrace();
            result = Response.getResponseError();

        }
        return result;
    }

    /**
     * 获取省市县
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getCityJson(@RequestBody(required = false) Map<String, Object> params) {
        Response response;
        try {

            // 结果集
            List<Map<String, Object>> list = tbCityService.getCityList(params);
            // 省List
            List<Map<String, Object>> provinceList = new ArrayList<>();
            // 返回格式map
            Map<String, Object> provinceCodeMap = new HashMap<>();
            // 用来做唯一储存
            Map<String, Object> listMap = new HashMap<>();
            // 获取所有省
            for (int i = 0, l = list.size(); i < l; i++) {
                // 省的记录判断
                if (listMap.get(list.get(i).get("pcode").toString()) == null && list.get(i).get("spell") != null && list.get(i).get("city") == null) {
                    // 存入map
                    listMap.put(list.get(i).get("pcode").toString(), list.get(i));
                    provinceList.add((Map<String, Object>) listMap.get(list.get(i).get("pcode").toString()));
                }
            }

            for (int i = 0, l = provinceList.size(); i < l; i++) {
//            建Citylist
                List<Map<String, Object>> cityList = new ArrayList<>();
                // 储存City
                List<Map<String, Object>> cityListNew = new ArrayList<>();

                Map<String, Object> cityMap = new HashMap<>();

                // 存储唯一city
                Map<String, Object> cityMapNew = new HashMap<>();

                provinceCodeMap.put(provinceList.get(i).get("creaid").toString(), cityListNew);
                for (int k = 0, j = list.size(); k < j; k++) {
                    if (list.get(k).get("spell") != null && list.get(k).get("city") != null && !cityMapNew.containsKey(list.get(k).get("pcode").toString() + list.get(k).get("ccode")) && list.get(k).get("pcode").toString().equals(provinceList.get(i).get("pcode").toString())) {
                        cityMapNew.put(list.get(k).get("pcode").toString() + list.get(k).get("ccode"), "1");
                        if (!cityList.contains(list.get(k)) && list.get(k).get("spell") != null) {
                            cityList.add(list.get(k));

                            Map<String, Object> countyMap = new HashMap<>();
                            List<Map<String, Object>> countyList = new ArrayList<>();
                            for (int q = 0, w = list.size(); q < w; q++) {
                                if (list.get(q).get("tcode") != null && list.get(q).get("pcode").equals(cityList.get(cityList.size() - 1).get("pcode")) && list.get(q).get("ccode").equals(cityList.get(cityList.size() - 1).get("ccode"))) {
                                    countyList.add(list.get(q));
                                    countyMap.put("list", countyList);
                                }
                            }
                            List<Map<String, Object>> countyList1 = new ArrayList<>();
                            countyList1.add(countyMap);
                            cityMap.put(cityList.get(cityList.size() - 1).get("creaid").toString(), countyList1);
                            cityMap.put("list", cityList);
                        }
                    }
                }
                cityListNew.add(cityMap);
            }
            provinceCodeMap.put("list", provinceList);
            response = Response.getResponseSuccess();
            response.setResponseEntity(provinceCodeMap);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }



    /**
     * 存redis用
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCityListToo", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getCityListToo(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> cityList = tbCityService.getCityList(param);
            Map<String, Object> map = new HashMap<>();
            for (int i = 0, l = cityList.size(); i < l; i++) {
                String value = JSONObject.fromObject(cityList.get(i)).toString();
                map.put(cityList.get(i).get("creaid").toString(), value);
            }
            result = Response.getResponseSuccess();
            result.setResponseEntity(map);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据地区名称获取地区ID信息
     *
     * @param param(包含地区名称)
     * @author kuangbin
     */
    @RequestMapping(value = "/getRegionID", method = RequestMethod.POST)
    @CrossOrigin
    public Response getRegionID(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> regionID = tbCityService.getRegionID(param);
            result = Response.getResponseSuccess();
            result.setResponseEntity(regionID);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据地区ID获取地区信息
     *
     * @param param(包含地区ID)
     * @author kuangbin
     */
    @RequestMapping(value = "/getRegionInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response getRegionInfo(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> regionID = tbCityService.getRegionInfo(param);
            result = Response.getResponseSuccess();
            result.setResponseEntity(regionID);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
    
    /**
     * @Author sapientia
     * @Date 10:35 根据省id获取省名称
     * @Description        
     **/
    @RequestMapping(value = "/queryProvinceNameByid", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryProvinceNameByid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> regionID = tbCityService.queryProvinceNameByid(param);
            result = Response.getResponseSuccess();
            result.setResponseEntity(regionID);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}