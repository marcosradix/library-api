package br.com.workmade.libraryApi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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
	   
	  //segundo minuto hora dia mês ano
	   @Scheduled(cron = "0 29 7 1/1 * ?")
	   private void tarefasAgendadas() { 
		   System.out.println("[TAREFAS] AGENDAMENTO FUNCIONANDO COM SUCESSO");
		   
	   }

}
