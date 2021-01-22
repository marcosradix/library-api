package br.com.workmade.libraryApi.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.dtos.LoanFilterDTO;
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
		
		when(loanRepository.existsByBookAndNotReturned(createBook())).thenReturn(true);
		
		Throwable exception = catchThrowable(() -> 	 loanService.save(savingLoan));
		
		assertThat(exception).isInstanceOf(AlreadyLoanedBookFoundException.class).hasMessage("Livro já emprestado");
		
		assertThrows(AlreadyLoanedBookFoundException.class, () -> {
			  
			 loanService.save(savingLoan);
		  } );
		  
		  verify(loanRepository, never()).save(savingLoan);
	
		
	}
	
	
    @Test
    @DisplayName("Deve obter informações de um empréstimo por ID")
    public void getLoanDetailsTest(){
    	Long id = 1L;
    	Loan loan = createLoan();
    	loan.setId(id);
    	
    	 when(loanRepository.findById(id)).thenReturn(Optional.of(loan));
    	 
    	 Optional<Loan> loanFound = loanService.findById(id);
    	 
    	 assertThat(loanFound.isPresent()).isTrue();
    	 assertThat(loanFound.get().getId()).isEqualTo(id);
    	 assertThat(loanFound.get().getCustomer()).isEqualTo(loan.getCustomer());
    	 assertThat(loanFound.get().getBook()).isEqualTo(loan.getBook());
    	 assertThat(loanFound.get().getLoanDate()).isEqualTo(loan.getLoanDate());
    	 
    	 verify(loanRepository).findById(id);
    	
    }
	
    
    @Test
    @DisplayName("Deve atualizar um empréstimo")
    public void updateLoanTest(){
       	Long id = 1L;
    	
		Loan loan = createLoan();
		loan.setReturned(true);
		loan.setId(id);
				
		when(loanRepository.save(loan)).thenReturn(loan);
		
		Loan saved = loanService.save(loan);
		
		assertNotNull(saved);
		verify(loanRepository).save(loan);
		
    	
    }
    
    @Test
    @DisplayName("Deve filtrar empréstimos pelas propriedades")
    public void findLoanTest(){
        //cenario
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Fulano").isbn("321").build();

        Loan loan = createLoan();
        loan.setId(1l);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> lista = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size());
        when( loanRepository.findByBookIsbnOrCustomer(
                    Mockito.anyString(),
                    Mockito.anyString(),
                    Mockito.any(PageRequest.class))
        )
                .thenReturn(page);

        //execucao
        Page<Loan> result = loanService.find( loanFilterDTO, pageRequest );


        //verificacoes
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
    
    public static Loan createLoan(){
		Book book = Book.builder().title("Aventuras").author("Fulano").isbn("123").build();
    	Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        return loan;
    }
	
	private Book createBook() {
		return Book.builder().id(11L).title("Meu Livro").author("Author").isbn("1213212").build();
	}

	
}
