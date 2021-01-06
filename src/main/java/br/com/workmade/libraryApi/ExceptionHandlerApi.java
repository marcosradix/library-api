package br.com.workmade.libraryApi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;

public class ExceptionHandlerApi {

	private List<String> errors;
	
	public ExceptionHandlerApi(BindingResult bindResult) {
		this.errors = new ArrayList<>();
		bindResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));

	}

	public List<String> getErrors() {
		return errors;
	}
	
	

}
