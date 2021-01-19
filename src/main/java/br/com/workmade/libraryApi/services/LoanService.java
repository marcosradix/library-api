package br.com.workmade.libraryApi.services;

import java.util.Optional;

import br.com.workmade.libraryApi.models.Loan;

public interface LoanService {
	
	Loan save(Loan loan);
	
	Optional<Loan> findById(Long id);

}
