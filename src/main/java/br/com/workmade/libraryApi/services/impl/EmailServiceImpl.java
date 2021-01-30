package br.com.workmade.libraryApi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.workmade.libraryApi.core.EmailProperties;
import br.com.workmade.libraryApi.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties emailProperties;

	@Override
	public void sendEmails(List<String> mailsList, String message) {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

				simpleMailMessage.setFrom(emailProperties.getRemetent());
				String[] emails = mailsList.toArray( new String[mailsList.size()]);
				
				simpleMailMessage.setTo(emails);
				simpleMailMessage.setSubject("Livro com empréstimo em atraso.");
				simpleMailMessage.setText(message);
				mailSender.send(simpleMailMessage);

	}

}
