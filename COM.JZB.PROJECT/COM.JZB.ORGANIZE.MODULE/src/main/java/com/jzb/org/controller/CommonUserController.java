package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.dao.DeptMapper;
import com.jzb.org.service.CommonUserService;
import com.jzb.org.service.CompanyService;
import com.jzb.org.service.DeptService;
import com.jzb.org.service.OrgToken;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/19
 * @修改人和其它信息
 */
@RestController
@RequestMapping("/orgCommon")
public class CommonUserController {
    @Autowired
    CommonUserService userService;

    /**
     * 查询地区信息
     */
    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private OrgToken orgToken;

    @Autowired
    private CompanyService companyService;
    /**
     * 查询redis缓存地区对象
     */
    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    @Autowired
    private OrgConfigProperties config;

    @Autowired
    private RegionBaseApi RegionBaseApi;

    @Autowired
    OrgConfigProperties orgConfigProperties;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptMapper deptMapper;
    /*
    新建公海用户
     */
    @RequestMapping("/addCommonUser")
    public Response addCommonUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("adduid",userinfo.get("uid"));
            // 添加公海用户
            int count = userService.addCommUser(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 查询公海用户
    @RequestMapping("/queryCommonUser")
    public Response queryCommonUser(@RequestBody Map<String,Object> param){
        Response result;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid",userinfo.get("uid"));
            int count = JzbDataType.getInteger(param.get("count"));

            JzbPageConvert.setPageRows(param);
            List<Map<String, Object>> regionList = new ArrayList<>();
            if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("province")))) {
                // 传入3代表查询县级地区
                if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("county")))) {
                    // 定义存放每个省市县地区的map
                    Map<String, Object> regionMap = new HashMap<>();
                    // 加入县级地区id到参数对象中
                    regionMap.put("region", JzbDataType.getString(param.get("county")));
                    regionList.add(regionMap);
                    // 等于2代表传入的是市级地区ID
                } else if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("city")))) {
                    // 添加查询地区的key
                    param.put("key", "jzb.system.city");

                    // 获取所有的地区信息
                    Response response = tbCityRedisApi.getCityJson(param);

                    // 将字符串转化为map
                    Map<String, Object> myJsonArray = (Map<String, Object>) JSON.parse(response.getResponseEntity().toString());
                    // 判断返回值中是否存在省信息
                    if (!JzbDataType.isEmpty(myJsonArray.get(JzbDataType.getString(param.get("province"))))) {
                        // 获取对应省下所有的城市信息
                        List<Map<String, Object>> myJsonList = (List<Map<String, Object>>) myJsonArray.get(JzbDataType.getString(param.get("province")));
                        for (int i = 0; i < myJsonList.size(); i++) {
                            // 获取省份下所有城市的信息
                            Map<String, Object> provinceMap = myJsonList.get(i);

                            // 如果为传入的城市ID则进行下一步
                            if (!JzbDataType.isEmpty(provinceMap.get(JzbDataType.getString(param.get("city"))))) {
                                // 获取城市下所有的县级信息
                                List<Map<String, Object>> countyMap = (List<Map<String, Object>>) provinceMap.get(JzbDataType.getString(param.get("city")));
                                Map<String, Object> county =  countyMap.get(0);
                                List<Map<String, Object>> cityList = (List<Map<String, Object>>) county.get("list");
                                for (int b = 0; b < cityList.size(); b++) {
                                    // 获取城市下单个的县级信息
                                    Map<String, Object> cityMap = cityList.get(b);

                                    // 定义存放每个省市县地区的map
                                    Map<String, Object> regionMap = new HashMap<>();

                                    // 将县级ID加入地区map对象中
                                    regionMap.put("region", JzbDataType.getString(cityMap.get("creaid")));
                                    regionList.add(regionMap);
                                }
                            }
                        }
                    }
                } else if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("province")))) {
                    // 添加查询地区的key
                    param.put("key", "jzb.system.city");

                    // 查询本身
                    Map<String, Object> regionProvince = new HashMap<>();
                    regionProvince.put("region", param.get("province"));
                    regionList.add(regionProvince);

                    // 获取所有的地区信息
                    Response response = tbCityRedisApi.getCityJson(param);

                    // 将字符串转化为map
                    Map<String, Object> myJsonArray = (Map<String, Object>) JSON.parse(response.getResponseEntity().toString());
                    if (!JzbDataType.isEmpty(myJsonArray.get(JzbDataType.getString(param.get("province"))))) {
                        List<Map<String, Object>> myJsonList = (List<Map<String, Object>>) myJsonArray.get(JzbDataType.getString(param.get("province")));
                        for (int i = 0; i < myJsonList.size(); i++) {
                            // 获取城市信息
                            Map<String, Object> provinceMap = myJsonList.get(i);
                            for (Map.Entry<String, Object> entry : provinceMap.entrySet()) {
                                if (!"list".equals(entry.getKey())) {
                                    String key = entry.getKey();
                                    // 定义存放每个省市县地区的map
                                    Map<String, Object> regionMap = new HashMap<>();
                                    regionMap.put("region", key);
                                    regionList.add(regionMap);
                                    List<Map<String, Object>> cityList = (List<Map<String, Object>>) entry.getValue();
                                    Map<String, Object> cityMap = cityList.get(0);
                                    List<Map<String, Object>> city = (List<Map<String, Object>>) cityMap.get("list");
                                    for (int k = 0; k < city.size(); k++) {
                                        // 获取城市下单个的县级信息
                                        Map<String, Object> cityMap1 = city.get(k);

                                        // 定义存放每个省市县地区的map
                                        Map<String, Object> region = new HashMap<>();

                                        // 将县级ID加入地区map对象中
                                        region.put("region", JzbDataType.getString(cityMap1.get("creaid")));
                                        regionList.add(region);
                                    }
                                }
                            }
                        }
                    }
                }

                // 将所有结果加入参数中传入
                param.put("list", regionList);
            }
            // 查询用户总数
            count = count > 0 ? count:userService.getCount(param);

            // 查询用户
            List<Map<String,Object>> list = userService.queryCommonUser(param);
            //获取用户信息
            for (int i = 0; i < list.size(); i++) {
                Response cityList = RegionBaseApi.getRegionInfo(list.get(i));
                list.get(i).put("region",cityList.getResponseEntity());
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            result = Response.getResponseSuccess(userinfo);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    // 修改公海用户
    @RequestMapping("/updCommonUser")
    public Response updCommonUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            int count = 0;
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            if (!JzbTools.isEmpty(paramp.get("uid"))){
                 count = userService.updComUser(paramp);
            }
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 删除公海用户
    @RequestMapping("/delUser")
    public Response delUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            // 删除公海用户
            List<Map<String,Object>> list = (List<Map<String, Object>>) paramp.get("list");
            if (!JzbTools.isEmpty(list)){
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).put("updtime",System.currentTimeMillis());
                    list.get(i).put("status",'2');
                }
            }
            paramp.put("list",list);
            int count = userService.delUser(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }
    // 公海用户关联单位
    @RequestMapping("/relCompanyUser")
    public Response relUser(@RequestBody Map<String,Object> param){
        Response result = null;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) param.get("userinfo");
            List<Map<String , Object>> plist = (List<Map<String, Object>>) param.get("list");
            for (int i = 0; i < plist.size(); i++) {
                Map<String,Object> paramp = plist.get(i);
                // 公海用户关联单位
                int num = userService.relCompanyUser(paramp);
                result = num > 0 ? Response.getResponseSuccess(userinfo) : Response.getResponseError();

                param.put("ouid", JzbDataType.getString(userinfo.get("uid")));
                // 返回是否邀请和加入资源池成功
                int count = companyService.addInvitee(paramp);
                if (count > 0) {
                    result = Response.getResponseSuccess();
                    Response sendResult;
                    int maybe = JzbDataType.getInteger(param.get("maybe"));
                    // 1发送邀请加入单位模板,2取消加入单位模板
                    if (maybe == 1) {
                        // 发送邀请信息模板
                        param.put("groupid", orgConfigProperties.getInvitationToJoin());
                        param.put("msgtag", "addInvitee1014");
                        param.put("senduid", "addInvitee1014");
                        sendResult = companyService.sendRemind(param);
                    } else {
                        // 发送取消信息模板
                        param.put("groupid", orgConfigProperties.getDisinvite());
                        param.put("msgtag", "addInvitee1012");
                        param.put("senduid", "addInvitee1012");
                        sendResult = companyService.sendRemind(paramp);
                    }
                    result.setResponseEntity(sendResult);
            }

            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    // 公海用户取消关联单位
    @RequestMapping("/cancelCompanyUser")
    public Response cancelCompanyUser(@RequestBody Map<String,Object> param){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) param.get("userinfo");
            param.put("ouid", JzbDataType.getString(userinfo.get("uid")));
            // 公海用户关联单位
            int count = userService.cancelCompanyUser(param);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }


    // 查询已关联单位用户
    @RequestMapping("/queryRelCommonUser")
    public Response queryRelCommonUser(@RequestBody Map<String,Object> param){
        Response response;
        try {
            Map<String,Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid",userInfo.get("uid"));
            int count = userService.querRelCommonCount(param);
            List<Map<String,Object>> list = userService.queryRelCommonUser(param);
            //获取用户信息
            for (int i = 0; i < list.size(); i++) {
                Response cityList = RegionBaseApi.getRegionInfo(list.get(i));
                list.get(i).put("region",cityList.getResponseEntity());
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response = Response.getResponseSuccess(userInfo);
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
    return response;
    }


    /**
     * CRM-销售业主-公海-用户
     * 点击用户中的导入Excel表格上传模板
     *
     * @param
     * @author chenhui
     */
    @RequestMapping(value = "/createCommonUser", method = RequestMethod.POST)
    @CrossOrigin
    public void createCommonUser(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            // 模板路径
            String srcFilePath = "static/excel/importCommonuser.xlsx";
            // 资源路径
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            // 创建输入流
            InputStream in = resource.getInputStream();
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);

            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=importCommonuser.xlsx");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 将excel写入到输出流中
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
    }

    /**
     * CRM-销售业主-公海-用户
     * 点击项目中的导入Excel表格获取模板数据
     *
     * @param
     * @author chenhui
     */
    @RequestMapping(value = "/ImportCommonUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response ImportCommonUser(@RequestBody MultipartFile file,
                                         @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            // 获取用户信息token
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("adduid", JzbDataType.getString(userInfo.get("uid")));
            param.put("userinfo", userInfo);
            // 获取文件名
            String fileName = file.getOriginalFilename();

            // 生成批次ID
            String batchId = JzbRandom.getRandomCharCap(11);

            //获取后缀名
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String filepath = config.getImportPath() + "/" + batchId + suffix;
            param.put("batchid", batchId);
            param.put("address", filepath);
            param.put("status", "2");
//            param.put("cname", fileName);
            try {
                // 保存文件到本地
                File intoFile = new File(filepath);
                intoFile.getParentFile().mkdirs();
                String address = intoFile.getCanonicalPath();
                file.transferTo(new File(address));
            } catch (Exception e) {
                JzbTools.logError(e);
                // 保存失败信息到批次表
                param.put("status", "4");
                param.put("summary", "保存文件到本地失败");
            }
            // 添加批次信息到用户导入批次表
            deptService.addExportBatch(param);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable ImportCommonUser = new ImportCommonUser(filepath, param, result);
            Future future = pool.submit(ImportCommonUser);
            // 获取返回值结果
            result = (Response) future.get();
            result.setResponseEntity(param);
            // 关闭线程池
            pool.shutdown();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
    /***
     * 读取新建用户模板中的数据时启动线程
     * @author kuangbin
     */
    public class ImportCommonUser implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportCommonUser(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            List<Map<String, Object>> errorList=new ArrayList<>();

            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);

            // 保存到用户导入信息表
            List<Map<String, Object>> userInfoList = new ArrayList<>();
            Map<String, Object> exportMap = null;
            Map<String, Object> pmap = new HashMap<>();
            // 遍历结果行,菜单数据从第2行开始
            for (int i = 1; i < list.size(); i++) {
                exportMap = new HashMap<>(param);
                // 设置行信息
                exportMap.put("idx", i);
                Map<Integer, String> map = list.get(i);

                // 获取序号
                if (JzbTools.isEmpty(map.get(0))){
                    break;
                }
                // 获取模板中的用户名称
                String uname = JzbDataType.getString(map.get(1));
                // 定义批次中的备注
                String summary = "";
                exportMap.put("uname", uname);
                // 在参数中加入用户名称
//                param.put("uname", uname);
                pmap.put("uname", uname);
                if (JzbDataType.isEmpty(uname)) {
                    summary = "用户名称不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    errorList.add(exportMap);
                    continue;
                }
                // 获取模板中的用户性别
                String sex = JzbDataType.getString(map.get(2));
//                param.put("sex", sex);
                pmap.put("sex", sex);
//                if (JzbDataType.isEmpty(sex)) {
//                    summary += "用户性别不能为空!";
//                    exportMap.put("status", "2");
//                    exportMap.put("summary", summary);
//                    userInfoList.add(exportMap);
//                    errorList.add(exportMap);
//                    continue;
//                }
                // 获取模板中的用户电话号码
                String phone = JzbDataType.getString(map.get(3));
              
//                param.put("phone", phone);
                pmap.put("phone", phone);
                if (JzbTools.isEmpty(userService.getPhoneKey(param))){
                    if (JzbDataType.isEmpty(phone)) {
                        summary += "招标人手机号不能为空!";
                        exportMap.put("status", "2");
                        exportMap.put("summary", summary);
                        userInfoList.add(exportMap);
                        errorList.add(exportMap);
                        continue;
                    } else {
                        if (!toPhone(phone)) {
                            exportMap.put("status", "2");
                            summary += "手机号不合规范";
                            exportMap.put("summary", summary);
                            userInfoList.add(exportMap);
                            errorList.add(exportMap);
                            continue;
                        }
                    }
                }else {
                    exportMap.put("status", "2");
                    summary += "该手机号已重复";
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    errorList.add(exportMap);
                    continue;
                }


                // 获取模板中的身份证号
                String cardid = JzbDataType.getString(map.get(4));
//                param.put("cardid", cardid);
                pmap.put("cardid", cardid);
                // 获取模板中的邮箱
                String mail = JzbDataType.getString(map.get(5));
//                param.put("mail",mail);
                pmap.put("mail", mail);
              // 获取模板中的用户地区
                String regionName = JzbDataType.getString(map.get(6));
                if (!JzbTools.isEmpty(regionName)) {
//                    param.put("regionName", regionName);
                    pmap.put("regionName", regionName);
//                    if (JzbDataType.isEmpty(regionName)) {
//                        summary += "用户所属地区不能为空!";
//                        exportMap.put("status", "2");
//                        exportMap.put("summary", summary);
//                        userInfoList.add(exportMap);
//                        errorList.add(exportMap);
//                        continue;
//                    }
                    // 调用获取地区ID的接口
                    Response regionID = regionBaseApi.getRegionID(pmap);
                    Object obj = regionID.getResponseEntity();
                    // 定义地区ID
                    String region = "";
                    if (JzbDataType.isMap(obj)) {
                        Map<Object, Object> regionMap = (Map<Object, Object>) obj;
                        region = JzbDataType.getString(regionMap.get("creaid"));
                    }
//                    param.put("region", region);
                    pmap.put("region", region);
                }
                // 获取模板中的用户单位名称
                String cname = JzbDataType.getString(map.get(7));
//                param.put("cname", cname);
                pmap.put("cname", cname);
                // 获取模板中的用户年龄
                int age = JzbDataType.getInteger(map.get(8));
//                param.put("age", age);
                pmap.put("age", age);
                // 获取模板中的用户职务
                String job = JzbDataType.getString(map.get(9));
//                param.put("job", job);
                pmap.put("job", job);
                // 获取模板中的用户籍贯
                String address = JzbDataType.getString(map.get(10));
//                param.put("native", address);
                pmap.put("native", address);
                // 获取模板中的用户毕业院校
                String graduated = JzbDataType.getString(map.get(11));
//                param.put("graduated", graduated);
                pmap.put("graduated", graduated);
                // 获取模板中的用户学历
                String education = JzbDataType.getString(map.get(12));
//                param.put("education", education);
                pmap.put("education", education);
                // 获取模板中的用户爱好
                String likes = JzbDataType.getString(map.get(13));
//                param.put("likes", likes);
                pmap.put("likes", likes);

                // 获取模板中的用户婚姻状态
                String marriage = JzbDataType.getString(map.get(14));
                if (!JzbDataType.isEmpty(marriage)) {
                    if ("已婚".equals(marriage)) {
                        marriage = "1";
                    } else if ("未婚".equals(marriage)) {
                        marriage = "2";
                    } else {
                        marriage = "";
                    }
                }
//                param.put("marriage", marriage);
                pmap.put("marriage", marriage);

                // 获取模板中的用户工作经历
                String works = JzbDataType.getString(map.get(15));
//                param.put("works", works);
                pmap.put("works", works);

                // 获取模板中的用户固定电话
                String telphone = JzbDataType.getString(map.get(16));
//                param.put("telphone",telphone);
                pmap.put("telphone",telphone);
                // 调用接口
//                param.put("uid",JzbRandom.getRandomCharCap(12));
                pmap.put("uid",JzbRandom.getRandomCharCap(12));
                int count = userService.addCommUser(pmap);
                pmap = new HashMap<>();
                if (count == 0) {
                    exportMap.put("status", "2");
                    exportMap.put("summary", "创建项目失败!");
                    userInfoList.add(exportMap);
                    continue;
                } else {
                    exportMap.put("status", "1");
                    userInfoList.add(exportMap);
                }

            }
            int export = 0;
            //保存用户导入信息表
            if (userInfoList.size() > 0) {
                export = deptMapper.insertExportUserInfoList(userInfoList);
            }
            //导入完成后修改状态
            if (export >= 1) {
                exportMap.put("status", "8");
            } else if (export == 0) {
                exportMap.put("status", "4");
            }
            deptService.updateExportBatch(exportMap);
            result.setResponseEntity(errorList);
            return result;
        }
    }



    /**
     * 校验手机号
     *
     * @param obj
     * @return
     */
    private boolean toPhone(String obj) {
        boolean result = true;
        try {
            String eg = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
            result = Pattern.matches(eg, obj);
        } catch (Exception e) {
            JzbTools.logError(e);
        }
        return result;
    }
}
