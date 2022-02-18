package com.ssk.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ssk.handler.CustomerHandler;
import com.ssk.handler.CustomerStreamHandler;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> customerRoutes(CustomerHandler handler) {
		return nest(path("/customer"),
				nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
						route(GET("/list"), handler::listCustomer)
						.andRoute(GET("/{id}"), handler::getCustomer)
						.andRoute(POST("/create"), handler::createCustomer)
						.andRoute(DELETE("/{id}"), handler::deleteCustomer)
						.andRoute(PUT("/{id}"), handler::updateCustomer)
						)
				);

	}
    
	
	@Bean
	public RouterFunction<ServerResponse> customerStreamRoutes(CustomerStreamHandler handler) {
		return nest(path("/customer/stream"),
				nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
						route(GET("/list"), handler::streamCustomerList)
						.andRoute(GET("/list/search"), handler::streamFirstNameCustomerList)
						.andRoute(GET("/sink"),handler::sinkedStreamCustomerList)
						.andRoute(POST("/create"), handler::sinkedCreateCustomer)
						.andRoute(PUT("/{id}"),handler::sinkedUpdateCustomer)
						)
				);	
	}
}
