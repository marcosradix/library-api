package br.com.workmade.libraryApi.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.exception.AlreadyLoanedBookFoundException;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.repository.LoanRepository;
import br.com.workmade.libraryApi.services.BookService;
import br.com.workmade.libraryApi.services.LoanService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {
	
	@InjectMocks
	private LoanService loanService = new LoanServiceImpl();
	
	@Mock
	private LoanRepository loanRepository;
	
	@Mock
	private BookService bookService;
	
	@Test
	@DisplayName("Deve salvar um empréstimo")
	public void saveLoan() throws Exception{
		Loan savingLoan = Loan.builder()
				.book(createBook()).customer("Fulano").loanDate(LocalDate.now()).build();
		
		Loan savedLoan = Loan.builder()
				.book(createBook()).id(11L).customer("Fulano").loanDate(LocalDate.now()).build();
		when(loanRepository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = loanService.save(savingLoan);
		
		assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
		
	}
	
	
	
	@Test
	@DisplayName("Deve lançar erro ao fazer um empréstimo de livro já emprestado")
	public void shoulAlreadyLoanedBookFoundException() throws Exception{
		Loan savingLoan = Loan.builder()
				.book(createBook()).customer("Fulano").loanDate(LocalDate.now()).build();
		
		when(loanRepository.existsByBookAndReturnedFalse(createBook())).thenReturn(true);
		
		Throwable exception = catchThrowable(() -> 	 loanService.save(savingLoan));
		
		assertThat(exception).isInstanceOf(AlreadyLoanedBookFoundException.class).hasMessage("Livro já emprestado");
		
		assertThrows(AlreadyLoanedBookFoundException.class, () -> {
			  
			 loanService.save(savingLoan);
		  } );
		  
		  verify(loanRepository, never()).save(savingLoan);
	
		
	}
	
	
	
	
	
	private Book createBook() {
		return Book.builder().id(11L).title("Meu Livro").author("Author").isbn("1213212").build();
	}

	
}
