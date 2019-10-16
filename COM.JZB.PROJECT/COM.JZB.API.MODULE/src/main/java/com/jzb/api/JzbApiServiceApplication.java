package com.jzb.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;


/**
 * API服务类入口
 * @author Chad
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class JzbApiServiceApplication {
	/**
	 * 主入口
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(JzbApiServiceApplication.class, args);
	} // End main

	/**
	 * 解决跨域问题
	 * @return
	 */
	@Bean
	public CorsWebFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		// cookie跨域
		config.setAllowCredentials(Boolean.TRUE);
		config.addAllowedMethod("*");
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		// 配置前端js允许访问的自定义响应头
		config.addExposedHeader("setToken");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
		source.registerCorsConfiguration("/**", config);
		return new CorsWebFilter(source);
	} // End corsFilter

	/**
	 *
	 * @param builder
	 * @return
	 */
//	@Bean
//	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//		JzbTools.logInfo("============================>>", "routeLocator");
//		RouteLocatorBuilder.Builder routeLocator = builder.routes();
//		try {
//			List<RouteConfig> configs = RouteConfig.getRouteConfigs();
//			if (routeLocator != null) {
//				configs.forEach(route -> routeLocator.route(route.getRouteId(),
//						r -> r.path(route.getPath()).and().weight(route.getGroup(),
//								route.getWeight()).uri(route.getUri())));
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return routeLocator.build();
//	} // End routeLocator
} // End class JzbApiServiceApplication
