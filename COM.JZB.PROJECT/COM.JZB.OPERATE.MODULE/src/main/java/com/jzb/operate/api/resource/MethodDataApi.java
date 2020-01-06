package com.jzb.operate.api.resource;
/**
 * 方法论
 *
 * @author lifei
 * @date
 */

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "jzb-resource")
@RequestMapping(value = "/methodData")
@Repository
public  interface MethodDataApi {
    /**
     * 查询方法论资料 所有typeids （ 父级 和子级）
     *lifei
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMethodDataTypeIdsAll", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMethodDataTypeIdsAll(@RequestBody Map<String, Object> param);

    /**
     * 查询方法论资料 根据typeid
     *lifei
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMethodDataByTypeid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMethodDataByTypeid(@RequestBody Map<String, Object> param);




}
