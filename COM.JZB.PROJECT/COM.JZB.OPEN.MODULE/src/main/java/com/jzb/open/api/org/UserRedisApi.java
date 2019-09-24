package com.jzb.open.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/user")
public interface UserRedisApi {

	/**
	 * 根据联系方式获取用户ID
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/getPhoneUid")
	public Response getNameByPhone(@RequestParam String phone);

	/**
	 * 从Redis中读取一个用户ID的用户基本信息
	 * 请求参数，uid
	 * @param param 请求参数
	 */
	@RequestMapping(value = "/getCacheUserInfo", method = RequestMethod.POST)
	public Response getCacheUserInfo(@RequestBody Map<String, Object> param);
} // End interface UserApi
