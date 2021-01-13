package br.com.workmade.libraryApi.exception;

public class AlreadyLoanedBookFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AlreadyLoanedBookFoundException(String message) {
		super(message);
	};
	

}
