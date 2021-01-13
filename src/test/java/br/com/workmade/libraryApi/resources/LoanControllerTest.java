package br.com.workmade.libraryApi.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.workmade.libraryApi.dtos.LoanDTO;
import br.com.workmade.libraryApi.exception.LoanBookNotFoundException;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.services.BookService;
import br.com.workmade.libraryApi.services.LoanService;

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
	
	private Book createBook() {
		return Book.builder().id(11L).title("Meu Livro").author("Author").isbn("1213212").build();
	}

}











