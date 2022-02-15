package com.ssk.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssk.domain.Customer;
import com.ssk.domain.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImp implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public Flux<Customer> findAll(){
		return customerRepository.findAll();
	}
	
	@Override
	public Mono<Customer> findById(String id){
		return customerRepository.findById(id);
	}
	
	@Override
	public Mono<Customer> save(Customer customer) {
		if(customer.getId() == null) {
		return customerRepository.save(customer);
		}else {
			return null;
		}
	}
	
	@Override
	public Mono<Void> delete(Customer customer) {
		return customerRepository.delete(customer);
	}

	@Override
	public Mono<Customer> update(Customer customer,String id){
		customerRepository.deleteById(id);
		Long updateid = Long.parseLong(id);
		customer.setId(updateid);
		return customerRepository.save(customer);
	}
 
	  public Flux<Customer> getStreamCustomers()  {
	      return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();    
	  }
	
}
