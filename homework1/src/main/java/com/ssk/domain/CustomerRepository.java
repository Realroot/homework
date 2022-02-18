package com.ssk.domain;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
	
	@Query("SELECT * FROM customer Where first_name = :first_name")
	Flux<Customer> findByFirstName(String first_name);
	
	@Query("SELECT * FROM customer Where last_name = :last_name")
	Flux<Customer> findByLastName(String last_name);
	
	@Query("SELECT * FROM customer Where first_name = :first_name AND last_name =:last_name")
	Flux<Customer> findByName(String first_name,String last_name);
}