package com.jzb.api.controller.org;

import com.jzb.api.service.CompanyService;
import com.jzb.api.service.DeptUserService;
import com.jzb.api.service.JzbUserAuthService;
import com.jzb.api.util.ApiToken;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/5 15:25
 */
@RestController
@RequestMapping(value = "/api/org")
public class DeptUserController {
    @Autowired
    private DeptUserService deptUserService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    ApiToken apiToken;

    /**
     * 用户认证服务
     */
    @Autowired
    private JzbUserAuthService authService;

    /**
     * 根据企业id和部门中的手机号或用户姓名获取组织与用户的数据
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/5 18:59
     */
    @RequestMapping(value = "/getDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptUser(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                    if (userInfo.size() > 0) {
                        param.put("userinfo", userInfo);
                        PageInfo pageInfo = deptUserService.getUserDept(param);
                        result = Response.getResponseSuccess(userInfo);
                        result.setPageInfo(pageInfo);
                    } else {
                        result = Response.getResponseError();
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 中台新建单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 16:59
     */
    @RequestMapping(value = "/addControlCompanyAll", method = RequestMethod.POST)
    @CrossOrigin
    public Response addControlCompanyAll(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cname", "region", "phone"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    param.put("uid", userInfo.get("uid"));
                    param.put("type", "1");
                    result = companyService.addCompany(param);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 管理员创建伙伴单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/23 17:37
     */
    @RequestMapping(value = "/addCompanyFriendAll", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyFriendAll(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cname", "region", "phone"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    param.put("uid", userInfo.get("uid"));
                    param.put("type", "1");
                    param.put("status", "8");
                    result = companyService.addCompany(param);
                    authService.addAdmin(param);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 注册时创建单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/19 11:08
     */
    @RequestMapping(value = "/addControlCompany", method = RequestMethod.POST)
    @CrossOrigin
    public Response addControlCompany(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cname", "phone"};
            String regular = "[\\w\\W]{3,20}";
            if (Pattern.matches(regular, JzbDataType.getString(param.get("cname")))) {
                if (JzbCheckParam.allNotEmpty(param, str)) {
                    Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                    if (userInfo.size() > 0) {
                        param.put("uid", userInfo.get("uid"));
                        param.put("type", "1");
                        param.put("userinfo", userInfo);
                        result = companyService.addCompany(param);
                    } else {
                        result = Response.getResponseError();
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
                Map<String, Object> code = new HashMap<>(2);
                code.put("message", "2");
                code.put("cid", "");
                result.setResponseEntity(code);
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取所有用户列表数据接口拼接
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 11:03
     */
    @RequestMapping(value = "/getAllUserList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAllUserList(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    result = deptUserService.getAllUserList(param);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 读取菜单模板中的数据并存入数据库
     *
     * @param file
     * @author kuangbin
     */
   /* @RequestMapping(value = "/importCompanyTemplate", method = RequestMethod.POST)
    @CrossOrigin
    public Response importCompanyTemplate(@RequestBody MultipartFile file,
                                    @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("userinfo", "");
            // 获取上传文件名称
            String fileName = file.getOriginalFilename();
            long time = System.currentTimeMillis();
            String filepath = "D:\\v3\\static\\Import\\" + time + fileName;

            // 保存文件到本地
            File intoFile = new File(filepath);
            file.transferTo(intoFile);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable importMenuThread = new ImportMenuThread(filepath, param, result);
            Future future = pool.submit(importMenuThread);
            // 获取返回值结果
            result = (Response) future.get();

            // 关闭线程池
            pool.shutdown();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    *//***
     * 读取菜单模板中的数据时启动线程
     * @author kuangbin
     *//*
    public class ImportMenuThread implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportMenuThread(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            String cid = JzbDataType.getString(param.get("cid"));
            Map<String, Object> parentid = new HashMap<>();
            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);

            // 获取当前模板中有多少列
            int size = list.get(0).size();

            // 获取当前时间
            long addTime = System.currentTimeMillis();

            // 获取产品信息
            Map<Integer, String> productMap = list.get(1);
            //Map<String, Object> pid = productService.getProductParam(productMap, size, cid);
            if (!JzbDataType.isEmpty(JzbDataType.getString(""))) {
                // 定义初始开关
                boolean bl = true;

                // 定义存放菜单参数的LIST
                List<Map<String, Object>> listParam = new ArrayList<>();

                // 遍历结果行,菜单数据从第四行开始
                for (int i = 3; i < list.size(); i++) {
                    Map<Integer, String> map = list.get(i);
                    // 遍历列获取每列的数据
                    for (int t = 0; t < size - 3; t++) {
                        String cname = map.get(t);
                        if (!JzbDataType.isEmpty(cname)) {
                            Map<String, Object> menuParam = new HashMap<>();
                            // 获取模板中的备注
                            String summary = map.get(size - 1);
                            menuParam.put("summary", summary);

                            // 获取模板中的排序
                            int idx = JzbDataType.getInteger(map.get(size - 2));
                            menuParam.put("idx", idx);

                            // 获取模板中的链接地址
                            String menupath = map.get(size - 3);
                            menuParam.put("menupath", menupath);

                            // 获取随机菜单ID
                            String mid = JzbRandom.getRandomCharCap(15);
                            menuParam.put("mid", mid);
                            menuParam.put("cname", cname);

                            // 加入产品ID
                            menuParam.put("pid", JzbDataType.getString(""));
                            //menuParam.put("cid", cid);

                            menuParam.put("addtime", addTime);
                            menuParam.put("updtime", addTime);

                            // 获取父级菜单ID
                            menuParam.put("parentid", JzbDataType.getString(parentid.get(JzbDataType.getString(t - 1))));
                            parentid.put(JzbDataType.getString(t), mid);
                            listParam.add(menuParam);
                            break;
                        } else {
                            // 代表已经获取到最后一个菜单名还没有值
                            if (t == size - 4) {
                                result = Response.getResponseError();
                                result.setResponseEntity("菜单名称不能为空");
                                bl = false;
                                break;
                            }
                        }
                    }
                }
                if (bl) {
                    *//*int count = productService.addProduct(pid);
                    if (count == 1) {
                        count = productService.addProductMenu(listParam);
                        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                        result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                    } else {
                        result = Response.getResponseError();
                        result.setResponseEntity("产品创建失败");
                    }*//*
                }
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("产品名称不能为空");
            }
            return result;
        }
    }*/
}
