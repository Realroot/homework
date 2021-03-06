package com.ssk.config;

import java.time.Duration;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import com.ssk.domain.Customer;
import com.ssk.domain.CustomerRepository;

import io.r2dbc.spi.ConnectionFactory;

@EnableR2dbcRepositories
@Configuration
public class H2R2dbcConfig  {

	private static final Logger log = LoggerFactory.getLogger(H2R2dbcConfig.class);

	@Bean
	ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

		return initializer;
	}

	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {

		return (args) -> {
			// save a few customers
			repository.saveAll(Arrays.asList(new Customer("Jack", "Bauer"), new Customer("Chloe", "O'Brian"),
					new Customer("Kim", "Bauer"), new Customer("David", "Palmer"), new Customer("Michelle", "Dessler"),
					new Customer("Lee", "a"), new Customer("Lee", "b"), new Customer("Lee", "c"),
					new Customer("Kim", "c"), new Customer("Park", "c"), new Customer("Jung", "c")))
					.blockLast(Duration.ofSeconds(10));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			repository.findAll().doOnNext(customer -> {
				log.info(customer.toString());
			}).blockLast(Duration.ofSeconds(10));

			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByFirstName("Lee").doOnNext(Lee -> {
				log.info(Lee.toString());
			}).blockLast(Duration.ofSeconds(10));
			;
			log.info("");
		};
	}
}
