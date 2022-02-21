package com.ssk.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table
public class Customer {

	@Id
	private Long id;
	private String first_name;
	private String last_name;

	public Customer(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name = last_name;
	}

}