package br.com.workmade.libraryApi.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties("my-email")
@Data
public class EmailProperties {
	
	/**
	 * email message to send
	 */
	private String message;

}
