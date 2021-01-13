package br.com.workmade.libraryApi.exception.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.BindingResult;

import br.com.workmade.libraryApi.exception.AlreadyLoanedBookFoundException;
import br.com.workmade.libraryApi.exception.BookNotFoundException;
import br.com.workmade.libraryApi.exception.BusinessException;
import br.com.workmade.libraryApi.exception.LoanBookNotFoundException;

public class ExceptionHandlerApi {

	private List<String> errors;
	
	public ExceptionHandlerApi(BindingResult bindResult) {
		this.errors = new ArrayList<>();
		bindResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));

	}

	public ExceptionHandlerApi(BusinessException e) {
		this.errors = Arrays.asList(e.getMessage());
	}

	public ExceptionHandlerApi(BookNotFoundException e) {
		this.errors = Arrays.asList(e.getMessage());
	}

	public ExceptionHandlerApi(LoanBookNotFoundException e) {
		this.errors = Arrays.asList(e.getMessage());
	}

	public ExceptionHandlerApi(AlreadyLoanedBookFoundException e) {
		this.errors = Arrays.asList(e.getMessage());
	}

	public List<String> getErrors() {
		return errors;
	}
	
	

}
