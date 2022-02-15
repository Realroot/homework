package com.ssk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ssk.handler.CustomerHandler;
import com.ssk.handler.CustomerStreamHandler;


@Configuration
public class routerConfig {

	@Autowired
	private CustomerHandler handler;
	
	@Autowired
	private CustomerStreamHandler streamHandler;
	
	@Bean
	public RouterFunction<ServerResponse> routes() {
		 return RouterFunctions.route()
	                .GET("/customer/list",handler::listCustomer)
	                .GET("/customer/{id}",handler::getCustomer)
	                .POST("/customer/save",handler::createCustomer)
	                .DELETE("/customer/{id}/",handler::deleteCustomer)
	                .PUT("/customer/{id}",handler::updateCustomer)
	                .GET("/stream/customers",streamHandler::listStreamCustomers)
	                .build();	
	}
}
