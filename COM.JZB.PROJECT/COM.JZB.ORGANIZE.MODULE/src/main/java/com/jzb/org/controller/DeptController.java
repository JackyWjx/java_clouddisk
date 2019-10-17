package com.jzb.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.service.DeptService;
import com.jzb.org.service.OrgToken;
import com.jzb.org.service.ProductService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/13 19:27
 */
@RestController
@RequestMapping("/org/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private OrgConfigProperties config;

    @Autowired
    private OrgToken orgToken;

    @Autowired
    private ProductService productService;
    /**
     * 根据企业id获取部门信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getDeptList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                param.put("pagesize", 2147483647);
                param.put("start", 0);
                List<Map<String, Object>> list = deptService.getDeptListByCid(param);
                PageInfo pageInfo = new PageInfo();
                result = Response.getResponseSuccess(userInfo);
                pageInfo.setList(list);
                result.setPageInfo(pageInfo);
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
     * 根据企业id,产品线id和产品名称获取相似名称的有效产品（包括产品包信息）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("plid", JzbDataType.getInteger(param.get("plid")));
                List<Map<String, Object>> list = deptService.getProductListByName(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(list);
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
     * 获取企业所有产品线
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/6 10:22
     */
    @RequestMapping(value = "/getProductLineList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductLineList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                List<Map<String, Object>> list = deptService.searchProLine(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(list);
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
     * 获取产品的菜单权限
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductMenu(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"pid", "ptype"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("ptype", JzbDataType.getInteger(param.get("ptype")));
                JSONArray map = productService.getProductMenu(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(map);
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
     * 获取CRM的菜单权限
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/25 10:16
     */
    @RequestMapping(value = "/getCRMMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCRMMenu(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("ptype", 1);
            String pid = config.getCrmId();
            param.put("pid", pid);
            JSONArray map = productService.getProductMenu(param);
            result = Response.getResponseSuccess(userInfo);
            Map<String, Object> reMap = new HashMap<>(2);
            reMap.put("pid", pid);
            reMap.put("list", map);
            result.setResponseEntity(reMap);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 根据用户姓名或用户id获取用户部门信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUserDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    param.put("pagesize", rows);
                    param.put("start", rows * (page - 1));
                    List<Map<String, Object>> list = deptService.queryDeptByCname(param);
                    PageInfo pageInfo = new PageInfo();
                    result = Response.getResponseSuccess(userInfo);
                    pageInfo.setList(list);
                    int count = JzbDataType.getInteger(param.get("count"));
                    if (count == 0) {
                        int size = deptService.queryDeptByCnameCount(param);
                        pageInfo.setTotal(size > 0 ? size : list.size());
                    }
                    result.setPageInfo(pageInfo);
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
     * 新建部门（组织架构）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanyDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "cname"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                if (JzbTools.isEmpty(param.get("pcdid"))) {
                    param.put("pcdid", "00000000000");
                }
                Map<String, Object> map = deptService.insertCompanyDept(param);
                result = JzbDataType.getInteger(map.get("add")) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                result.setResponseEntity(map);
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
     * 修改部门信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/modifyCompanyDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "cdid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int update = deptService.updateCompanyDept(param);
                result = update > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 删除部门信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/removeCompanyDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeCompanyDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "cdid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                deptService.deleteCompanyDept(param);
                result = Response.getResponseSuccess();
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
     * 根据部门表中的用户姓名或手机号获取用户部门信息和在职状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptUser(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    param.put("pagesize", rows);
                    param.put("start", rows * (page - 1));
                    List<Map<String, Object>> duList = deptService.queryDeptUser(param);
                    result = Response.getResponseSuccess(userInfo);
                    PageInfo pageInfo = new PageInfo();
                    pageInfo.setList(duList);
                    int count = JzbDataType.getInteger(param.get("count"));
                    if (count == 0) {
                        int size = deptService.queryDeptUserCount(param);
                        pageInfo.setTotal(size > 0 ? size : duList.size());
                    }
                    result.setPageInfo(pageInfo);
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
     * 部门添加用户
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response addDeptUser(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "cname", "uid", "phone"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("status", "1");
                if (JzbTools.isEmpty(param.get("cdid"))) {
                    param.put("cdid", JzbDataType.getString(param.get("cid")) + "0000");
                }
                int add = deptService.addDeptUser(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 修改部门用户表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/3 16:28
     */
    @RequestMapping(value = "/modifyDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyDeptUser(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cdid", "cid", "uid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                int add = deptService.updateDeptUser(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 部门用户表移除用户
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/3 16:31
     */
    @RequestMapping(value = "/removeDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeDeptUser(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cdid", "cid", "uid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("status", "2");
                deptService.updateDeptUser(param);
                result = Response.getResponseSuccess(userInfo);
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
     * 导出用户模板
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportDeptExcel", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response exportDeptExcel(@RequestBody Map<String, Object> param, HttpServletResponse response) {
        Response result;
        try {
            String url;
            int type = JzbDataType.getInteger(param.get("type"));
            if (type == 1) {
                url = "static/excel/importuser.xlsx";
            } else {
                url = "static/excel/importuserdept.xlsx";
            }
            String srcFilePath = Thread.currentThread().getContextClassLoader().getResource(url).getPath();
            FileInputStream in = new FileInputStream(srcFilePath);
            //读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            //响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=importuserdept.xlsx");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //将excel写入到输出流中
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
        return null;
    }

    /**
     * 批量导入用户到部门
     *
     * @param file
     * @param
     * @return
     */
    @RequestMapping(value = "/importUserInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response importUserInfo(@RequestBody MultipartFile file,
                                   @RequestHeader(value = "token") String token,
                                   @RequestParam(value = "cid") String cId,
                                   @RequestParam(value = "cdid", required = false) String cdId) {
        Response result;
        try {
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            if (userInfo.size() > 0) {
                //生成批次ID
                String batchId = JzbRandom.getRandomChar(11);
                //获取上传文件名称
                String fileName = file.getOriginalFilename();
                //获取后缀名
                String suffix = fileName.substring(fileName.lastIndexOf("."));
                String filepath = config.getImportPath() + "/" + batchId + suffix;
                Map<String, Object> param = new HashMap<>(6);
                param.put("batchid", batchId);
                param.put("address", filepath);
                param.put("cname", fileName);
                param.put("cid", cId);
                param.put("cdid", cdId);
                param.put("uid", userInfo.get("uid"));
                param.put("status", "2");
                try {
                    //保存文件到本地
                    File intoFile = new File(filepath);
                    intoFile.getParentFile().mkdirs();
                    String address = intoFile.getCanonicalPath();
                    file.transferTo(new File(address));
                } catch (Exception e) {
                    e.printStackTrace();
                    JzbTools.logError(e);
                    //保存失败信息到批次表
                    param.put("status", "4");
                    param.put("summary", "保存文件到本地失败");
                }
                //添加批次信息到用户导入批次表
                deptService.addExportBatch(param);
                if (JzbDataType.getInteger(param.get("status")) == 2) {
                    ExportUser exportUser = new ExportUser(param);
                    exportUser.start();
                }
                result = Response.getResponseSuccess(userInfo);
                Map<String, Object> map = new HashMap(4);
                map.put("batchid", batchId);
                map.put("cid", cId);
                map.put("status", param.get("status"));
                result.setResponseEntity(map);
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
     * 所有用户导入
     *
     * @param file
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/10/11 20:59
     */
    @RequestMapping(value = "/importUserAll", method = RequestMethod.POST)
    @CrossOrigin
    public Response importUserAll(@RequestBody MultipartFile file,
                                  @RequestHeader(value = "token") String token) {
        Response result;
        try {
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            if (userInfo.size() > 0) {
                //生成批次ID
                String batchId = JzbRandom.getRandomChar(11);
                Map<String, Object> param = new HashMap<>(6);
                //获取上传文件名称
                String fileName = file.getOriginalFilename();
                //获取后缀名
                String suffix = fileName.substring(fileName.lastIndexOf("."));
                String filepath = config.getImportPath() + "/" + batchId + suffix;
                param.put("cname", fileName);
                param.put("uid", userInfo.get("uid"));
                param.put("status", "2");
                param.put("batchid", batchId);
                param.put("address", filepath);

                try {
                    //保存文件到本地
                    File intoFile = new File(filepath);
                    intoFile.getParentFile().mkdirs();
                    String address = intoFile.getCanonicalPath();
                    file.transferTo(new File(address));
                } catch (Exception e) {
                    //保存失败信息到批次表
                    param.put("status", "4");
                    param.put("summary", "保存文件到本地失败");
                    e.printStackTrace();
                    JzbTools.logError(e);
                }
                //添加批次信息到用户导入批次表
                deptService.addExportBatch(param);
                if (JzbDataType.getInteger(param.get("status")) == 2) {
                    param.put("type", "1");
                    ExportUser exportUser = new ExportUser(param);
                    exportUser.start();
                }
                result = Response.getResponseSuccess(userInfo);
                Map<String, Object> map = new HashMap(4);
                map.put("batchid", batchId);
                map.put("status", param.get("status"));
                result.setResponseEntity(map);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    class ExportUser extends Thread {
        private Map<String, Object> param;

        ExportUser(Map<String, Object> maps) {
            param = maps;
        }

        @Override
        public void run() {
            //导入数据
            int type = JzbDataType.getInteger(param.get("type"));
            int export;
            if (type == 1) {
                export = deptService.addExportUserAll(param);
            } else {
                export = deptService.addExportUserInfo(param);
            }
            //导入完成后修改状态
            if (export == 1) {
                param.put("status", "8");
            } else if (export != 1) {
                param.put("status", "4");
            }
            deptService.updateExportBatch(param);
        }

    }


    /**
     * 获取企业下属用户数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getDeptUserList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptUserList(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo pageInfo;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    param.put("pagesize", rows);
                    param.put("start", rows * (page - 1));
                    List<Map<String, Object>> deptList = deptService.queryDeptUserList(param);
                    result = Response.getResponseSuccess(userInfo);
                    pageInfo = new PageInfo();
                    pageInfo.setList(deptList);
                    int count = JzbDataType.getInteger(param.get("count"));
                    if (count == 0) {
                        int size = deptService.queryDeptUserListCount(param);
                        pageInfo.setTotal(size > 0 ? size : deptList.size());
                    }
                    result.setPageInfo(pageInfo);
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
     * 获取部门下所有子级的用户包括自身
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/4 14:36
     */
    @RequestMapping(value = "/getDeptUserChildList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptUserChildList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                if (JzbTools.isEmpty(param.get("pcdid"))) {
                    param.put("pcdid", "00000000000");
                }
                List<Map<String, Object>> map = deptService.queryDeptUserChildList(param);
                result = map.size() > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                result.setResponseEntity(map);
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
     * 调整部门
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/4 15:25
     */
    @RequestMapping(value = "/modifyDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cdid", "cid", "uid", "newcdid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                int up = deptService.updateDept(param);
                result = up > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
