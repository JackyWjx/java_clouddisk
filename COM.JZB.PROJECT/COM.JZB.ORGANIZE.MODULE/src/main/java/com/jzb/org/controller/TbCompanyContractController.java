package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * <p>
 * 企业合同表
 */
@RestController
@RequestMapping(value = "/org/companyContract")
public class TbCompanyContractController {

    @Autowired
    private TbCompanyContractService tbCompanyContractService;

    /**
     * 生成合同
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanyContract", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addCompanyContract(@RequestBody Map<String, Object> param) {
        Response response;
        try {

            response = tbCompanyContractService.addCompanyContract(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询合同
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyContract", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response quertCompantContract(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "count","pageno","pagesize"})) {
                response = Response.getResponseSuccess();
            } else {
                JzbPageConvert.setPageRows(param);
                // 查询合同
                List<Map<String, Object>> list = tbCompanyContractService.quertCompantContract(param);
                for(int i=0,l=list.size();i<l;i++){
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                // 如果count>0 就返回list 大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? list.size() : 0);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }
}
