package com.jzb.resource.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import com.jzb.resource.service.TbProductPriceService;
import com.jzb.resource.service.TbProductResListService;

import com.jzb.resource.util.PageConvert;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/ProductResList")
public class TbProductResListController {

    //分页参数的设置
    @Autowired
    private AdvertService advertService;

    @Autowired
    private TbProductResListService tbProductResListService;

    @Autowired
    private TbProductPriceService tbProductPriceService;

    @Value("${ftpConfig.path}")
    private String path;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbProductResListController.class);



    /**
     * 删除合同模板
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteProductRes", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response deleteProductRes(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/contractTemplate/deleteProductRes";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if(JzbCheckParam.haveEmpty(param,new String[]{"pid"})){
                result=Response.getResponseError();
            }else {
                String[] pids = JzbDataType.getString(param.get("pid")).split(",");
                List<String> list = new ArrayList<>();
                for (int i = 0; i < pids.length; i++) {
                    list.add(pids[i]);
                }
                int count = list.size() > 0 ? tbProductResListService.updateTbProductResList(list) : 0;
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "deleteProductRes Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }




    /**
     * 根据产品线的id查询产品表
     * 根据产品id查询出产品参数
     *
     * @param param
     * @return
     */

    @RequestMapping(value = "/getProductResList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductResList(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            //前端传过来的总条数
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            //把分页参数在设置好
            PageConvert.setPageRows(param);
            //返回所有合同配置中的产品资源
            List<Map<String, Object>> productResList = tbProductResListService.getProductResList(param);
            if (count == 0) {
                // 查询单位总数
                count = tbProductResListService.getCount(param);
            }
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(productResList);
            //设置分页总数
            pageInfo.setTotal(count > 0 ? count : productResList.size());
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改时查询产品参数表中的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductItem", method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response getProductItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //前端传过来的分页总条数
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取产品报价总数
            count = count < 0 ? 0 : count;
            //把分页参数在设置好
            param = advertService.setPageSize(param);
            //返回所有合同配置中的产品功能
            List<Map<String, Object>> ProductPriceList = tbProductResListService.getProductItem(param);
            if (count == 0) {
                // 查询产品报价的总数
                count = ProductPriceList.size();
            }
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(ProductPriceList);
            //设置分页总数
            pageInfo.setTotal(count > 0 ? count : ProductPriceList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 添加资源产品表的数据13
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbProductResList", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbProductResList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pid", "plid", "cname"})) {
                //添加资源产品表中的数据
                result = Response.getResponseError();
            } else {
                int count = tbProductResListService.saveTbProductResList(param);
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 添加新建产品参数
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbProductParameteItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbProductParameteItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //获取Map参数中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("addtime", time);
                paramList.get(i).put("updtime", time);
                paramList.get(i).put("paraid", JzbRandom.getRandomCharCap(13));
            }
            //添加一条产品参数
            int count = tbProductResListService.saveTbProductParameteItem(paramList);
            //判断返回的参数来确定是否添加成功
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                //如果添加失败返回错误信息
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 修改合同配置中的产品参数列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTbProductParameteItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateTbProductParameteItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //获取Map参数中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            for (Map<String, Object> stringObjectMap : paramList) {
                long time = System.currentTimeMillis();
                stringObjectMap.put("updtime", time);
            }
            int count = tbProductResListService.updateTbProductParameteItem(paramList);
            //如果返回值大于0,代表修改成功 ， 否则就是修改失败
            if (count > 0) {
                //获取用户信息返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 点击修改的时候进行查询返回给页面
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbProductParameteItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbProductParameteItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> itemList = tbProductResListService.getTbProductParameteItem(param);
            //获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(itemList);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 文件上传
     */
    @RequestMapping(value = "/saveFile", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveFile(@RequestBody MultipartFile file,HttpServletRequest request) throws UnsupportedEncodingException {
        Response result;
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");//你的编码格式
        }
        try {
            //获取文件名称
            String filename = file.getOriginalFilename();
            //文件后缀
            String fileSuffix = filename.substring(filename.lastIndexOf("."), filename.length());
            long time = System.currentTimeMillis();
            //UUID uuid = UUID.randomUUID();
            String filePath = path + File.separator + time + filename;
            //保存在本地
            File desFile = new File(filePath);
            desFile.getParentFile().mkdirs();
            //保存多媒体文件
            file.transferTo(desFile);
            filePath = filePath.replace("\\","/");
            //返回成功的结果
            result = Response.getResponseSuccess();
            Map<String, String> stringObjectMap = new HashedMap();
            stringObjectMap.put("accept","//"+time+filename);
            stringObjectMap.put("acceptname", filename);
            result.setResponseEntity(stringObjectMap);
        }
        catch (Exception e){
            JzbTools.logError(e);
            result = Response.getResponseError();
        }

        return result;

    }









    /**
     * 文件下载
     *
     * @param response
     * @param
     * @return
     */
    @RequestMapping(value = "/creatFile", method = RequestMethod.GET)
    @CrossOrigin
    public void creatFile(HttpServletResponse response,@RequestParam("pid")String pid,HttpServletRequest request) {

        File file;
        FileInputStream fis = null;
        ServletOutputStream out = null;
        try {
            //获取url地址
            Map<String , Object>  map  = new HashedMap();
            map.put("pid",pid);
            List<Map<String,Object>> list = tbProductResListService.getURL(map);
            String url ="D:\\01-project\\java\\COM.JZB.PROJECT\\COM.JZB.RESOURCE.MOUDLE\\src\\main\\resources\\file"+list.get(0).get("accept").toString();
            //设置编码集
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(list.get(0).get("acceptname").toString(),"UTF-8"));
            request.setCharacterEncoding("utf-8");
            file = new File(url);
            //读取文件的路径
            fis = new FileInputStream(file);
            //写出文件
            out = response.getOutputStream();
            //读取文件
            byte[] buffer=new byte[2097152];
            int len = 1024;
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JzbTools.logError(e);
        }finally {
            try {
                JzbTools.logInfo("资源 ==============>> 释放");
                fis.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 应用模板的查询
     * @param param
     * @return
     */
    @RequestMapping(value = "/getResList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getResList(@RequestBody Map<String, Object> param) {
        Response result;
        //判断参数为空返回404
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"paraid"})) {
                result = Response.getResponseError();
            } else {
               //查询结果
                List<Map<String,Object>> list  = tbProductResListService.getResList(param);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
            }

        } catch (Exception e) {
            //打印异常信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}



