package com.jzb.open.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth")
public interface OpenAuthApi {
	/**
	 * 根据联系方式获取用户id,name
	 */
	@RequestMapping(value = "/getUserIdNameByPhone")
	public Response getUserIdNameByPhone(Map<String, Object> param);
} // End interface UserApi
