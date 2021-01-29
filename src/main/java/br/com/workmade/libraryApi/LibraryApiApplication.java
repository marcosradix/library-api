package br.com.workmade.libraryApi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}
	
	   @Bean
	    public ModelMapper modelMapper() {
	        ModelMapper modelMapper = new ModelMapper();
	        modelMapper.getConfiguration().setSkipNullEnabled(true);
	        return modelMapper;
	    }
	   

}
