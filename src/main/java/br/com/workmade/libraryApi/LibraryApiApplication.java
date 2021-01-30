package br.com.workmade.libraryApi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.workmade.libraryApi.core.EmailProperties;



@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {
//	@Autowired
//	private  EmailService emailService;
	
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

	@Bean
	public EmailProperties emailProperties() {
		return new EmailProperties();
	}
	
	   @Bean
	    public ModelMapper modelMapper() {
	        ModelMapper modelMapper = new ModelMapper();
	        modelMapper.getConfiguration().setSkipNullEnabled(true);
	        return modelMapper;
	    }
	   
//	   @Bean
//	   public CommandLineRunner runner() {
//		   return args  -> {
//			   emailService.sendEmails(Arrays.asList("library-api-0cd213@inbox.mailtrap.io"), "Testando servicço de email."); 
//		   };
//		   
//	   }

}
