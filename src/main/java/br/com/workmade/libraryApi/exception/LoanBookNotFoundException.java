package br.com.workmade.libraryApi.exception;



public class LoanBookNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoanBookNotFoundException(String message) {
		super(message);
	};
	

}
