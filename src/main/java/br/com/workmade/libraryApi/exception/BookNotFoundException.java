package br.com.workmade.libraryApi.exception;



public class BookNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BookNotFoundException(String message) {
		super(message);
	};
	

}