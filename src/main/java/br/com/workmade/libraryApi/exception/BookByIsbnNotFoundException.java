package br.com.workmade.libraryApi.exception;



public class BookByIsbnNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BookByIsbnNotFoundException(String message) {
		super(message);
	};
	

}
