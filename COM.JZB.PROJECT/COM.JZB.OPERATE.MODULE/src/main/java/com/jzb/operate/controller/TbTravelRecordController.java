package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbTravelRecordService;
import com.jzb.operate.service.TbTravelVehicleService;
import com.jzb.operate.util.PageConvert;
import com.jzb.operate.util.VeriafitionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
            if (JzbCheckParam.allNotEmpty(param, new String[]{"uid", "rows", "page"})) {
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
                    Map<String,Object> map=new HashMap<>();

                    // 取出每一行记录的出差工具id
                    map.put("vehicleid",list.get(i).get("vehicle"));
                    // 查出name 后放入
                    list.get(i).put("vehicleName",tbTravelVehicleService.getTravelName(map));
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
            // 获取参数中的list
            List<Map<String, Object>> travelList = (List) param.get("travelList");
            result = tbTravelRecordService.addTravelRecord(travelList) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userInfo")) : Response.getResponseError();
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

            result = tbTravelRecordService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userInfo")) : Response.getResponseError();
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
                result = tbTravelRecordService.setDeleteStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userInfo")) : Response.getResponseError();
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"uid", "page", "rows"})) {
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