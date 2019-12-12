package com.jzb.open.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org")
public interface CompanyApi {
	/**
	 * 主页获取单位信息
	 *
	 * @author kuangbin
	 */
	@RequestMapping(value = "/getEnterpriseData", method = RequestMethod.POST)
	@CrossOrigin
	public Response getEnterpriseData(@RequestBody Map<String, Object> param);
} // End interface UserApi
