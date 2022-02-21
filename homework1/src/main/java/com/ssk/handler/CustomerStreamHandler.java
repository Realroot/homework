package com.ssk.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ssk.domain.Customer;
import com.ssk.exception.BadRequestException;
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
	
	public boolean isNumeric (String id) {
		return id.matches("[+-]?\\d*(\\.\\d+)?");
	}

	public Mono<ServerResponse> streamCustomerList(ServerRequest request) {
		Flux<Customer> customer = service.getStreamCustomers();
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
	}
	
	public Mono<ServerResponse> sinkedStreamCustomerList(ServerRequest request){
		Flux<ServerSentEvent<Customer>> findcustomer = sink.asFlux().map(c-> ServerSentEvent.builder(c).build()).doOnCancel(()->{
			sink.asFlux().blockLast();
		});
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(findcustomer, Customer.class);
	}
	
	public Mono<ServerResponse> sinkedCreateCustomer(ServerRequest request){
		Mono<Customer> customer = request.bodyToMono(Customer.class).flatMap(c -> service.streamSave(c,sink))
				.onErrorResume(e -> Mono.error(new BadRequestException()));
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}
	
	public Mono<ServerResponse> sinkedUpdateCustomer(ServerRequest request) {
		String id = request.pathVariable("id");
		if(!isNumeric(id)) throw new BadRequestException();
		Mono<Customer> customer = request.bodyToMono(Customer.class).flatMap(c-> service.streamUpdate(id, c, sink))
				.onErrorResume(e -> Mono.error(new BadRequestException()));
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}
	
	public Mono<ServerResponse> sinkedGetCustomer(ServerRequest request) {
		String id = request.pathVariable("id");
		if(!isNumeric(id)) throw new BadRequestException();
		Mono<Customer> customer = service.findById(id).flatMap(c-> service.streamUpdate(id, c, sink))
				.onErrorResume(e -> Mono.error(new BadRequestException()));
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(customer, Customer.class);
	}
	
	
	public Mono<ServerResponse> streamNameCustomerList(ServerRequest request) {
		Optional<String> firstName = request.queryParam("firstName");
		Optional<String> lastName = request.queryParam("lastName");

		if (firstName.isPresent() && lastName.isEmpty()) {
			Flux<Customer> customer = service.findByFirstName(firstName.get());
			return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
		} else if(firstName.isEmpty() && lastName.isPresent()){
			Flux<Customer> customer = service.findByLastName(lastName.get());
			return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
		}else if(firstName.isPresent() && lastName.isPresent()){
			Flux<Customer> customer = service.findByName(firstName.get(),lastName.get());
			return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class).log();
		}else {
			throw new BadRequestException();
		}
	}
	
}
