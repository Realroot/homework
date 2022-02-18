package com.ssk.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ssk.domain.Customer;
import com.ssk.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class CustomerStreamHandler {

	
	private final CustomerService service;
	private final Sinks.Many<Customer> sink;
	
	public CustomerStreamHandler(CustomerService service) {
		this.service = service;
		sink = Sinks.many().multicast().onBackpressureBuffer();
	}


	// 하나씩 날림
	public Mono<ServerResponse> streamCustomerList(ServerRequest request) {
		Flux<Customer> customer = service.getStreamCustomers();
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
	}

	//query문
	public Mono<ServerResponse> streamFirstNameCustomerList(ServerRequest request) {
		Optional<String> firstName = request.queryParam("firstName");
		Optional<String> lastName = request.queryParam("lastName");

		if (firstName.isPresent() && lastName.isEmpty()) {
			Flux<Customer> customer = service.findByFirstName(firstName.get());
			return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
		} else if(firstName.isEmpty() && lastName.isPresent()){
			Flux<Customer> customer = service.findByLastName(lastName.get());
			return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
		}else {
			Flux<Customer> customer = service.findByName(firstName.get(),lastName.get());
			return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
		}
	}
	
	public Mono<ServerResponse> sinkedStreamCustomerList(ServerRequest request){
		Flux<ServerSentEvent<Customer>> findcustomer = sink.asFlux().map(c-> ServerSentEvent.builder(c).build()).doOnCancel(()->{
			sink.asFlux().blockLast();
		});
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(findcustomer, Customer.class).log();
	}
	
	public Mono<ServerResponse> sinkedCreateCustomer(ServerRequest request){
		Mono<Customer> customer = request.bodyToMono(Customer.class).flatMap(c -> service.streamSave(c,sink));
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}
	
	public Mono<ServerResponse> sinkedUpdateCustomer(ServerRequest request) {
		String id = request.pathVariable("id");
		Mono<Customer> customer = request.bodyToMono(Customer.class);
		return customer
				.flatMap(c -> ServerResponse.status(HttpStatus.CREATED).body(service.streamUpdate(id,c,sink), Customer.class));
	}
	
}
