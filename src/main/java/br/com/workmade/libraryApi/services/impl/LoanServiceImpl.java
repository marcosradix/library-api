package br.com.workmade.libraryApi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.repository.LoanRepository;
import br.com.workmade.libraryApi.services.LoanService;

@Service
public class LoanServiceImpl implements LoanService {
	@Autowired
	private LoanRepository loanRepository;

	
	@Override
	public Loan save(Loan loan) {
		return loanRepository.save(loan);
	}

}
