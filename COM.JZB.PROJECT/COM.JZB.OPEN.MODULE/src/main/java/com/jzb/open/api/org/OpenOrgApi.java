package com.jzb.open.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org")
public interface OpenOrgApi {
	/**
	 * 入驻开放平台
	 * @param param
	 */
	@RequestMapping(value = "/addOpenPlatform")
    public int addOpenPlatform(Map<String, Object> param);

    /**
	 * 获取企业信息
	 * @return
	 */
	@RequestMapping(value = "/getOrgInfo")
	public Response getOrgInfo(Map<String, Object> param);


} // End interface UserApi
