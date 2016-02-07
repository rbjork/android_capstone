package com.androidcapstone.symptommanagement.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.androidcapstone.symptommanagement.server.auth.OAuth2SecurityConfiguration;
import com.androidcapstone.symptommanagement.server.repository.*;

@EnableAutoConfiguration

@EnableJpaRepositories(basePackageClasses = {CheckinRepository.class, 
											 DoctorsRepository.class, 
											 PatientRepository.class, 
											 MedicationTakenRepository.class}) // How to add more than one?

@EnableWebMvc

@Configuration

//@ComponentScan({"com.androidcapstone.symptomanagement.server","com.fitbit.api.client"})
@ComponentScan
@Import(OAuth2SecurityConfiguration.class)
public class Application extends RepositoryRestMvcConfiguration {
	
		public static void main(String[] args) {
			SpringApplication.run(Application.class, args);
		}
}
