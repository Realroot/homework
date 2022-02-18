package com.ssk.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssk.domain.Customer;
import com.ssk.domain.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class CustomerServiceImp implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Flux<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public Mono<Customer> findById(String id) {
		return customerRepository.findById(id);
	}

	@Override
	public Mono<Customer> save(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public Mono<Void> delete(String id) {
		return customerRepository.deleteById(id);
	}

	@Override
	public Mono<Customer> update(Customer customer, String id) {
		customerRepository.deleteById(id);
		Long updateid = Long.parseLong(id);
		customer.setId(updateid);
		return customerRepository.save(customer);
	}

	public Flux<Customer> getStreamCustomers() {
		return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
	}

	public Flux<Customer> findByLastName(String lastName){
		return customerRepository.findByLastName(lastName).delayElements(Duration.ofSeconds(1)).log();
	}
	
	public Flux<Customer> findByFirstName(String fristName){
		return customerRepository.findByFirstName(fristName).delayElements(Duration.ofSeconds(1)).log();
	}
	
	public Flux<Customer> findByName(String firstName,String lastName){
		return customerRepository.findByName(firstName, lastName).delayElements(Duration.ofSeconds(1)).log(); 
	}
	
	public Mono<Customer> streamSave(Customer customer,Sinks.Many<Customer> sink){
		return customerRepository.save(customer).doOnNext(c->sink.tryEmitNext(c));
	}
	
	public Mono<Customer> streamUpdate(String id,Customer customer,Sinks.Many<Customer> sink){
		customerRepository.deleteById(id);
		Long updateid = Long.parseLong(id);
		customer.setId(updateid);
		return customerRepository.save(customer).doOnNext(c->sink.tryEmitNext(c));
	}
}
