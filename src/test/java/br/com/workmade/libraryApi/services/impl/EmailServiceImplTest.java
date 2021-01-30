package br.com.workmade.libraryApi.services.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.core.EmailProperties;
import br.com.workmade.libraryApi.services.EmailService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class EmailServiceImplTest {

	
   @InjectMocks
    private  EmailService emailService = new EmailServiceImpl();
	
	@Mock
	private JavaMailSender mailSender;
	
	@Mock
	private EmailProperties emailProperties;
	
	@DisplayName("Deve testar o envio de email")
	@Test
	void testSendEmails() {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

		simpleMailMessage.setFrom(emailProperties.getRemetent());
		List<String> mailsList = Arrays.asList("library-api-0cd213@inbox.mailtrap.io");
		
		String[] emails = mailsList.toArray( new String[mailsList.size()]);
		
		simpleMailMessage.setTo(emails);
		simpleMailMessage.setSubject("Livro com empréstimo em atraso.");
		simpleMailMessage.setText("Testando servicço de email.");
		
		
		
		emailService.sendEmails(Arrays.asList("library-api-0cd213@inbox.mailtrap.io"), "Testando servicço de email.");
		
		verify(mailSender, times(1)).send(simpleMailMessage);

	}

}
