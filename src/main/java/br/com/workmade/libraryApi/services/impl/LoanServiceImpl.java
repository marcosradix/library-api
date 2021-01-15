package br.com.workmade.libraryApi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.workmade.libraryApi.exception.AlreadyLoanedBookFoundException;
import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.repository.LoanRepository;
import br.com.workmade.libraryApi.services.BookService;
import br.com.workmade.libraryApi.services.LoanService;

@Service
public class LoanServiceImpl implements LoanService {
	
	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private BookService bookService;
	
	@Override
	public Loan save(Loan loan) {
		bookService.findById(loan.getId());
		if(loanRepository.existsByBookAndNotReturned(loan.getBook())){
			throw new AlreadyLoanedBookFoundException("Livro já emprestado");
		}
		return loanRepository.save(loan);
	}

}
