package br.com.workmade.libraryApi.services;

import java.util.List;

public interface EmailService {

	void sendEmails(List<String> mailsList, String message);
	
	

}
