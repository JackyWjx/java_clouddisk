package com.jzb.org.api.tender;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-tender",url = "http://pre-api.biaodaa.com")
@RequestMapping(value = "/api/notice")
@Repository
public interface Tender {

    /**
     * 获取招投标列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/zhaobiao/list",method = RequestMethod.POST)
    String tender(@RequestBody Map<String, Object> param);
}
