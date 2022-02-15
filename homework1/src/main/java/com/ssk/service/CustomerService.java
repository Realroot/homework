package com.ssk.service;



import com.ssk.domain.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

	public Flux<Customer> findAll();
	public Mono<Customer> findById(String id);
	public Mono<Customer> save(Customer customer);
	public Mono<Void> delete(Customer customer);
	public Mono<Customer> update(Customer customer,String id);
	public Flux<Customer> getStreamCustomers();
	
}
