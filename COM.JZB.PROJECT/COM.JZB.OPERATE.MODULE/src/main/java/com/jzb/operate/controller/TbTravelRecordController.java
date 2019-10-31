package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbTravelAimService;
import com.jzb.operate.service.TbTravelRecordService;
import com.jzb.operate.service.TbTravelVehicleService;
import com.jzb.operate.service.TbUserTravelService;
import com.jzb.operate.util.PageConvert;
import com.jzb.operate.util.VeriafitionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 出差记录表
 */
@RestController
@RequestMapping(value = "/operate/travelRecord")
public class TbTravelRecordController {

    @Autowired
    private TbTravelRecordService tbTravelRecordService;

    @Autowired
    private TbTravelVehicleService tbTravelVehicleService;

    @Autowired
    private TbTravelAimService tbTravelAimService;

    @Autowired
    private TbUserTravelService tbUserTravelService;

    /**
     * 根据uid 获取出差申请记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTravelRecordList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTravekRecordList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.allNotEmpty(param, new String[]{"uid", "pagesize", "pageno"})) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 设置分页
                PageConvert.setPageRows(param);
                // 如果起始时间参数不为空则转为时间戳
                if (!JzbTools.isEmpty(param.get("beginTime"))) {
                    Date beginTime = sdf.parse(JzbDataType.getString(param.get("beginTime")));
                    param.put("beginTime", beginTime.getTime());
                }
                if (!JzbTools.isEmpty(param.get("endTime"))) {
                    Date beginTime = sdf.parse(JzbDataType.getString(param.get("endTime")));
                    param.put("endTime", beginTime.getTime());
                }
                // 得到结果集
                List<Map<String, Object>> list = tbTravelRecordService.getTravelRecordListByUid(param);

                for (int i = 0, l = list.size(); i < l; i++) {
                    Map<String, Object> map = new HashMap<>();

                    // 取出每一行记录的出差工具id
                    map.put("vehicleid", list.get(i).get("vehicle"));
                    // 查出name 后放入
                    list.get(i).put("vehicleName", tbTravelVehicleService.getTravelName(map));

                    map.put("travelid", list.get(i).get("travelid"));
                    list.get(i).put("travelAim", tbTravelAimService.queryTravelAim(map));

                    list.get(i).put("userList", tbUserTravelService.queryUserTravel(map));
                }

                // 得到总数
                int count = tbTravelRecordService.getTravelRecordCountByUid(param);

                // 定义分页  pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(count);

                // 设置userinfo
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));

                result.setPageInfo(pi);

            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            // 返回错误
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据uid 获取出差申请记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTravelRecordListByCid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTravelRecordListByCid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.allNotEmpty(param, new String[]{"count", "curcid", "pagesize", "pageno"})) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 设置分页
                PageConvert.setPageRows(param);
                // 如果起始时间参数不为空则转为时间戳
                // 得到结果集
                List<Map<String, Object>> list = tbTravelRecordService.queryTravelRecordByCid(param);

                for (int i = 0, l = list.size(); i < l; i++) {
                    Map<String, Object> map = new HashMap<>();

                    // 取出每一行记录的出差工具id
                    map.put("vehicleid", list.get(i).get("vehicle"));
                    // 查出name 后放入
                    list.get(i).put("vehicleName", tbTravelVehicleService.getTravelName(map));

                    map.put("travelid", list.get(i).get("travelid"));
                    list.get(i).put("travelAim", tbTravelAimService.queryTravelAim(map));

                    list.get(i).put("userList", tbUserTravelService.queryUserTravel(map));
                }

                // 得到总数
                int count = tbTravelRecordService.getTravelRecordCountByUid(param);

                // 定义分页  pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                pi.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? list.size() : 0);

                // 设置userinfo
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));

                result.setPageInfo(pi);

            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            // 返回错误
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加出差记录 (添加用户出差记录，出差目标)
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addTravelRecord", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addTravelRecord(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取参数中的出差list
            List<Map<String, Object>> travelList = (List) param.get("list");

            List<Map<String, Object>> userTravelList;

            List<Map<String, Object>> travelAim;

            // 循环生成出差id
            for (int i = 0, l = travelList.size(); i < l; i++) {
                String travelid;
                if (travelList.get(i).get("travelid") != null) {
                    travelid = travelList.get(i).get("travelid").toString();
                } else {
                    travelid = JzbRandom.getRandomCharLow(19);
                }
                // 生成出差id

                // 放入出差id
                travelList.get(i).put("travelid", travelid);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                travelList.get(i).put("starttime", JzbDateUtil.getDate(sdf.format(JzbDataType.getDateTime(travelList.get(i).get("starttime"))), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                travelList.get(i).put("finishtime", JzbDateUtil.getDate(sdf.format(JzbDataType.getDateTime(travelList.get(i).get("finishtime"))), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                travelList.get(i).put("nexttime", JzbDateUtil.getDate(sdf.format(JzbDataType.getDateTime(travelList.get(i).get("nexttime"))), JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());

                // 获取参数中的用户出差list
                userTravelList = (List<Map<String, Object>>) travelList.get(i).get("userList");

                // 给用户出差记录添加出差id
                for (int k = 0, j = userTravelList.size(); k < j; k++) {
                    userTravelList.get(k).put("travelid", travelid);
                }

                // 获取参数中目标list
                travelAim = (List<Map<String, Object>>) travelList.get(i).get("travelAim");

                // 遍历赋值出差id
                for (int q = 0, w = travelAim.size(); q < w; q++) {
                    travelAim.get(q).put("travelid", travelid);
                }
                // 添加用户出差记录
                tbUserTravelService.addUserTravel(userTravelList);
                travelList.get(i).remove("userList");
                // 添加出差目标
                tbTravelAimService.addTravelAim(travelAim);
                travelList.get(i).remove("travelAim");
            }
            // 获取参数中的出差目标list
            result = tbTravelRecordService.addTravelRecord(travelList) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.isEmpty(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改出差记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTravelRecord", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateTravelRecord(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            result = tbTravelRecordService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 撤回
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/setBackStatus", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response setBackStatus(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                result = Response.getResponseError();
            } else {
                param.put("status", 4);
                result = tbTravelRecordService.updateTravelRecordStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/setDeleteStatus", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response setDeleteStatus(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                result = Response.getResponseError();
            } else {
                result = tbTravelRecordService.setDeleteStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取归我审批的记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMyVerifyTravel", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMyVerifyTravel(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            // 验证指定参数为空返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"uid", "pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                // 获取结果集
                List<Map<String, Object>> list = tbTravelRecordService.getMyVerifyTravel(param);

                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());

                // 获取网关传过来的userinfo
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pi);
            }

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    //抄送

    /**
     * 点击报销申请，根据id查询多条出差记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTravelRecordByTravelid")
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTravelRecordByTravelid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 接收api 传过来的数组  用list接收
            List<Map<String, Object>> travelArray = (List<Map<String, Object>>) param.get("travelArray");

            // 获取结果集
            List<Map<String, Object>> list = tbTravelRecordService.getTravelRecordByTravelid(travelArray);

            PageInfo pi = new PageInfo();
            pi.setList(list);
            pi.setTotal(list.size());

            // 获取网关传过来的userinfo
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            result.setPageInfo(pi);

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}