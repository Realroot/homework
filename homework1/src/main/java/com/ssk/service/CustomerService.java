package com.ssk.service;

import com.ssk.domain.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public interface CustomerService {

	public Flux<Customer> findAll();

	public Mono<Customer> findById(String id);

	public Mono<Customer> save(Customer customer);

	public Mono<Void> delete(String id);

	public Mono<Customer> update(Customer customer, String id);

	public Flux<Customer> getStreamCustomers();
	
	public Flux<Customer> findByLastName(String lastName);
	
	public Flux<Customer> findByFirstName(String lastName);
	
	public Flux<Customer> findByName(String firstName,String lastName);
	
	public Mono<Customer> streamSave(Customer customer,Sinks.Many<Customer> sink);
	
	public Mono<Customer> streamUpdate(String id,Customer customer,Sinks.Many<Customer> sink);
}
