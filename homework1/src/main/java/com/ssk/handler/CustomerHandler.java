package com.ssk.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ssk.domain.Customer;
import com.ssk.exception.BadRequestException;
import com.ssk.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class CustomerHandler {

	@Autowired
	private final CustomerService service;

	public CustomerHandler(CustomerService service) {
		super();
		this.service = service;
	}

	public boolean isNumeric (String id) {
		return id.matches("[+-]?\\d*(\\.\\d+)?");
	}
	
	public Mono<ServerResponse> listCustomer(ServerRequest request) {
		Flux<Customer> customers = service.findAll();
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customers, Customer.class);
	}

	public Mono<ServerResponse> getCustomer(ServerRequest request) {
		String id = request.pathVariable("id");
		if(!isNumeric(id)) throw new BadRequestException();
		Mono<Customer> customer = service.findById(id);	
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
     
	}
	
	public Mono<ServerResponse> createCustomer(ServerRequest request) {
		Mono<Customer> customer = request.bodyToMono(Customer.class).flatMap(c -> service.save(c))
				.onErrorResume(e -> Mono.error(new BadRequestException(e)));  
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}

	public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
		String id = request.pathVariable("id");
		if(!isNumeric(id)) throw new BadRequestException();
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(service.delete(id), Customer.class);
	}

	public Mono<ServerResponse> updateCustomer(ServerRequest request) {
		String id = request.pathVariable("id");
		if(!isNumeric(id)) throw new BadRequestException();
		Mono<Customer> customer = request.bodyToMono(Customer.class).flatMap(c -> service.update(c,id))
				.onErrorResume(e -> Mono.error(new BadRequestException(e)));  
	    return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}

}
