package br.com.workmade.libraryApi.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.workmade.libraryApi.dtos.LoanDTO;
import br.com.workmade.libraryApi.dtos.LoanFilterDTO;
import br.com.workmade.libraryApi.exception.AlreadyLoanedBookFoundException;
import br.com.workmade.libraryApi.models.Book;
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

	@Override
	public Optional<Loan> findById(Long id) {
		return loanRepository.findById(id);
	}

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return loanRepository.findByBookIsbnOrCustomer( filterDTO.getIsbn(), filterDTO.getCustomer(), pageable );
    }
	
    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loanDays = 4;
        LocalDate threeDaysAgo = LocalDate.now().minusDays(loanDays);
        return loanRepository.findByLoanDateLessThanAndNotReturned(threeDaysAgo);
    }

	@Override
	public Page<LoanDTO> getLoansByBook(Book book, Pageable pageable) {
		return loanRepository.findByBook(book, pageable);
	}

}
