package br.com.workmade.libraryApi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.workmade.libraryApi.dtos.LoanFilterDTO;
import br.com.workmade.libraryApi.models.Loan;

public interface LoanService {
	
	Loan save(Loan loan);
	
	Optional<Loan> findById(Long id);
	
	Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable);
	
    List<Loan> getAllLateLoans();

}
