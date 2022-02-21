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

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ssk.domain.Customer;
import com.ssk.handler.CustomerHandler;
import com.ssk.handler.CustomerStreamHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class RouterConfig {

	@RouterOperations({
		@RouterOperation(
				path = "/customer/list",
				 produces = { MediaType.APPLICATION_JSON_VALUE }, 
				 method = RequestMethod.GET, 
				 beanClass = CustomerHandler.class, 
				 beanMethod = "listCustomer",
				 operation = @Operation(operationId = "listCustomer", responses = { 
									@ApiResponse(responseCode = "200", description = "get all customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))) 
					       })
				)
	,
		@RouterOperation(
				path = "/customer/{id}",
				produces = { MediaType.APPLICATION_JSON_VALUE },
				method = RequestMethod.GET,
				beanClass = CustomerHandler.class, 
				beanMethod = "getCustomer",
				operation = @Operation(operationId = "getCustomer", responses = { 
									@ApiResponse(responseCode = "200", description = "get customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
									@ApiResponse(responseCode = "400", description = "parameter is not Number.")},
				parameters = {
						@Parameter(in = ParameterIn.PATH, name = "id")
				}
				)
		)
	,
	@RouterOperation(
			path = "/customer/create",
			produces = { MediaType.APPLICATION_JSON_VALUE },
			method = RequestMethod.POST,
			beanClass = CustomerHandler.class, 
			beanMethod = "createCustomer",
			operation = @Operation(operationId = "createCustomer", responses = { 
								@ApiResponse(responseCode = "201", description = "customer create successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
								@ApiResponse(responseCode = "400", description = "input the first_name and last_name")
								},
					requestBody = @RequestBody(
							content = @Content(schema =@Schema(
									implementation = Customer.class
									))
							)
			)
	),
	@RouterOperation(
			path = "/customer/{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE },
			method = RequestMethod.DELETE,
			beanClass = CustomerHandler.class, 
			beanMethod = "deleteCustomer",
			operation = @Operation(operationId = "deleteCustomer", responses = { 
								@ApiResponse(responseCode = "200", description = "delete customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
								@ApiResponse(responseCode = "400", description = "parameter is not Number.") },
			parameters = {
					@Parameter(in = ParameterIn.PATH, name = "id")
			}
			)
	),
	@RouterOperation(
			path = "/customer/{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE },
			method = RequestMethod.PUT,
			beanClass = CustomerHandler.class, 
			beanMethod = "updateCustomer",
			operation = @Operation(operationId = "updateCustomer", responses = { 
								@ApiResponse(responseCode = "200", description = "update customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
								@ApiResponse(responseCode = "400", description = "parameter is not Number.")},
					requestBody = @RequestBody(
							content = @Content(schema =@Schema(
									implementation = Customer.class
									))
							),
			parameters = {
					@Parameter(in = ParameterIn.PATH, name = "id")
			}
			)
	)	
	})
	
		
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
	
	@RouterOperations({
		@RouterOperation(
				path = "/customer/stream/list",
				 produces = { MediaType.APPLICATION_JSON_VALUE }, 
				 method = RequestMethod.GET, 
				 beanClass = CustomerStreamHandler.class, 
				 beanMethod = "streamCustomerList",
				 operation = @Operation(operationId = "streamCustomerList", responses = { 
									@ApiResponse(responseCode = "200", description = "get all customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))) 
					       })
				),
		@RouterOperation(
				path = "/customer/stream/sink",
				 produces = { MediaType.APPLICATION_JSON_VALUE }, 
				 method = RequestMethod.GET, 
				 beanClass = CustomerStreamHandler.class, 
				 beanMethod = "sinkedStreamCustomerList",
				 operation = @Operation(operationId = "sinkedStreamCustomerList", responses = { 
									@ApiResponse(responseCode = "200", description = "get all customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))) 
					       })
				)
		,
		@RouterOperation(
				path = "/customer/stream/create",
				produces = { MediaType.APPLICATION_JSON_VALUE },
				method = RequestMethod.POST,
				beanClass = CustomerStreamHandler.class, 
				beanMethod = "sinkedCreateCustomer",
				operation = @Operation(operationId = "sinkedCreateCustomer", responses = { 
									@ApiResponse(responseCode = "201", description = "customer create successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
									@ApiResponse(responseCode = "400", description = "input the first_name and last_name")
									},
						requestBody = @RequestBody(
								content = @Content(schema =@Schema(
										implementation = Customer.class
										))
								)
				)
		)
		,
		@RouterOperation(
				path = "/customer/stream/{id}",
				produces = { MediaType.APPLICATION_JSON_VALUE },
				method = RequestMethod.PUT,
				beanClass = CustomerStreamHandler.class, 
				beanMethod = "sinkedUpdateCustomer",
				operation = @Operation(operationId = "sinkedUpdateCustomer", responses = { 
									@ApiResponse(responseCode = "200", description = "update customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
									@ApiResponse(responseCode = "400", description = "parameter is not Number or input the firstName or lastName ")},
						requestBody = @RequestBody(
								content = @Content(schema =@Schema(
										implementation = Customer.class
										))
								),
				parameters = {
						@Parameter(in = ParameterIn.PATH, name = "id")
				}
				)
		),
		@RouterOperation(
				path = "/customer/stream/{id}",
				produces = { MediaType.APPLICATION_JSON_VALUE },
				method = RequestMethod.GET,
				beanClass = CustomerStreamHandler.class, 
				beanMethod = "sinkedGetCustomer",
				operation = @Operation(operationId = "sinkedGetCustomer", responses = { 
									@ApiResponse(responseCode = "200", description = "get customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
									@ApiResponse(responseCode = "400", description = "parameter is not Number.")},
				parameters = {
						@Parameter(in = ParameterIn.PATH, name = "id")
				}
				)
		),
		@RouterOperation(
				path = "/customer/stream/list/search",
				produces = { MediaType.APPLICATION_JSON_VALUE },
				method = RequestMethod.GET,
				beanClass = CustomerStreamHandler.class, 
				beanMethod = "streamNameCustomerList",
				operation = @Operation(operationId = "streamNameCustomerList", responses = { 
									@ApiResponse(responseCode = "200", description = "get customer successfully.", content = @Content(schema = @Schema(implementation = Customer.class))),
									@ApiResponse(responseCode = "400", description = "input the firstName or lastName")},
				parameters = {
						@Parameter(in = ParameterIn.QUERY, name = "firstName"),
						@Parameter(in = ParameterIn.QUERY, name = "lastName"),
				}
				)
		)
	})
 
	@Bean
	public RouterFunction<ServerResponse> customerStreamRoutes(CustomerStreamHandler handler) {
		return nest(path("/customer/stream"),
				nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
						route(GET("/list"), handler::streamCustomerList)
						.andRoute(GET("/sink"),handler::sinkedStreamCustomerList)
						.andRoute(POST("/create"), handler::sinkedCreateCustomer)
						.andRoute(PUT("/{id}"),handler::sinkedUpdateCustomer)
						.andRoute(GET("/{id}"), handler::sinkedGetCustomer)
						.andRoute(GET("/list/search"), handler::streamNameCustomerList)
						)
				);	
	}
}
