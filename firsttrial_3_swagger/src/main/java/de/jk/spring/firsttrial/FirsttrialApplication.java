package de.jk.spring.firsttrial;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.jk.spring.firsttrial.entity.CustomerEntity;
import de.jk.spring.firsttrial.repository.CustomerRepository;

@SpringBootApplication
public class FirsttrialApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
	public static void main(String[] args) {
		SpringApplication.run(FirsttrialApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application successfully started!");	
	}
	
	

}
