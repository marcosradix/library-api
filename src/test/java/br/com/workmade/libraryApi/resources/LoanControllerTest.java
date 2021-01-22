package br.com.workmade.libraryApi.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.workmade.libraryApi.dtos.LoanDTO;
import br.com.workmade.libraryApi.dtos.LoanFilterDTO;
import br.com.workmade.libraryApi.dtos.ReturnedLoanDTO;
import br.com.workmade.libraryApi.exception.AlreadyLoanedBookFoundException;
import br.com.workmade.libraryApi.exception.LoanBookNotFoundException;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.services.BookService;
import br.com.workmade.libraryApi.services.LoanService;
import br.com.workmade.libraryApi.services.impl.LoanServiceTest;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {
	
	static final String LOAN_API = "/api/loans";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BookService bookService;
	
	@MockBean
	private LoanService loanService;
	
	@Test
	@DisplayName("Deve realizar um emprestimo")
	public void createLoanTest() throws Exception{
		LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		given(bookService.findByIsbn("123")).willReturn(Book.builder().id(11L).isbn("123").build());
		
		Loan loan = Loan.builder().id(1L).customer("Fulano").book(createBook()).loanDate(LocalDate.now()).build();
		given(loanService.save(any(Loan.class))).willReturn(loan);
		
		var request = MockMvcRequestBuilders.post(LOAN_API).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json);
		
		mvc.perform(request).andExpect(status().isCreated())
		.andExpect(content().string("1"));
		
	}
	
	@Test
	@DisplayName("Deve lançar BookNotFoundException ao criar loan com livro inexistente")
	public void shouldReturnBookNotFoundExceptionWhenTryCreateLoan() throws Exception{
		String message = "Livro não encontrado";
		LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
		String json = new ObjectMapper().writeValueAsString(dto);
		given(bookService.findByIsbn(Mockito.anyString())).willThrow(new LoanBookNotFoundException(message));
		
		
		var request = MockMvcRequestBuilders.post(LOAN_API).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json);
		
		mvc.perform(request).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("errors", Matchers.hasSize(1))).andExpect(jsonPath("errors[0]").value(message));
		
	}
	
	
	@Test
	@DisplayName("Deve retornar erro ao tentar pegar um livro já emprestado")
	public void loanedBookErrorOnCreateLoan() throws Exception{
		String message = "Livro já emprestado";
		LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
		String json = new ObjectMapper().writeValueAsString(dto);
		
		given(bookService.findByIsbn("123")).willReturn(Book.builder().id(11L).isbn("123").build());
		
		given(loanService.save(Mockito.any(Loan.class))).willThrow(new AlreadyLoanedBookFoundException(message));
		
		var request = MockMvcRequestBuilders.post(LOAN_API).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json);
		
		mvc.perform(request).andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("errors", Matchers.hasSize(1)))
		.andExpect(jsonPath("errors", Matchers.hasSize(1))).andExpect(jsonPath("errors[0]").value(message));
		
	}
	
	@Test
	@DisplayName("Deve retornar  um livro para loan")
	public void returnBookLoanTest() throws Exception {
		ReturnedLoanDTO returnedLoanDTO = ReturnedLoanDTO.builder().returned(true).build();
		
		Loan loan = Loan.builder().id(1L).build();
		
		given(loanService.findById(Mockito.anyLong())).willReturn(Optional.of(loan));
		
		String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);
		var request = MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request).andExpect(status().isOk());
		
		verify(loanService, times(1)).save(loan);
		
	}
	
	
	@Test
	@DisplayName("Deve retornar 404 quando tentar devolver um livro inexistente")
	public void returnInexistentBookLoanTest() throws Exception {
		ReturnedLoanDTO returnedLoanDTO = new ReturnedLoanDTO();
		returnedLoanDTO.setReturned(true);
		String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);
	
		
		given(loanService.findById(Mockito.anyLong())).willReturn(Optional.empty());
		
		var request = MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request).andExpect(status().isNotFound());

		
	}
	

	@Test
	@DisplayName("Deve filtrar empréstimos")
	public void findLoansTest() throws Exception {
		Long id = 1L;
		Book book = createBook();
		Loan loan = LoanServiceTest.createLoan();
		loan.setId(id);
		loan.setBook(book);
		
		given( loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)) )
        .willReturn( new PageImpl<Loan>( Arrays.asList(loan), PageRequest.of(0,100), 1 ) );

		
        String queryString = String.format("?isbn=%s&customer=%s&page=0&size=10",
        		loan.getBook().getIsbn(), loan.getCustomer());

    	var request = MockMvcRequestBuilders
                .get(LOAN_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
            .perform( request )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("content", Matchers.hasSize(1)))
            .andExpect( jsonPath("totalElements").value(1) )
            .andExpect( jsonPath("pageable.pageSize").value(10) )
            .andExpect( jsonPath("pageable.pageNumber").value(0));
		
	}
	
	
	private Book createBook() {
		return Book.builder().id(11L).title("Meu Livro").author("Author").isbn("1213212").build();
	}

}











