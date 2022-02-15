package com.ssk.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ssk.domain.Customer;
import com.ssk.service.CustomerService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@ApiResponses({
	   @ApiResponse(responseCode = "200", description = "OK !!"),
       @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
       @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
       @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
})
@Component
public class CustomerHandler {

	@Autowired
	private CustomerService service;
	
	public Mono<ServerResponse> listCustomer(ServerRequest request){
		Flux<Customer> customers = service.findAll();
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customers, Customer.class);
	}
	
	public Mono<ServerResponse> getCustomer(ServerRequest request){
		String id = request.pathVariable("id");
		Mono<Customer> customer = service.findById(id);
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}
	
	public Mono<ServerResponse> createCustomer(ServerRequest request){
		 Mono<Customer> customer = request.bodyToMono(Customer.class).flatMap(c -> service.save(c));
		 return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}
	
	public Mono<ServerResponse> deleteCustomer(ServerRequest request){
		String id = request.pathVariable("id");
		Mono<Customer> customer = service.findById(id);
		return customer.flatMap(p -> service.delete(p).then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> updateCustomer(ServerRequest request){
		String id = request.pathVariable("id");
		Mono<Customer> update = request.bodyToMono(Customer.class);
		return update.flatMap(c->
				ServerResponse.status(HttpStatus.CREATED).body(service.update(c,id),Customer.class));
	}
	
}
