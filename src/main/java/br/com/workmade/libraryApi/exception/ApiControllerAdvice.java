package br.com.workmade.libraryApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.workmade.libraryApi.exception.handler.ExceptionHandlerApi;

@RestControllerAdvice
public class ApiControllerAdvice {
	
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionHandlerApi handleValidationExceptions(MethodArgumentNotValidException e) {
    	BindingResult bindResult = e.getBindingResult();
    	return new ExceptionHandlerApi(bindResult);
    }
    
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionHandlerApi handleValidationExceptions(BookNotFoundException e) {
    	return new ExceptionHandlerApi(e);
    }
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionHandlerApi handleBookController(BusinessException e) {
    	return new ExceptionHandlerApi(e);
    }
    @ExceptionHandler(LoanBookNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionHandlerApi handleValidationExceptions(LoanBookNotFoundException e) {
    	return new ExceptionHandlerApi(e);
    }
}
