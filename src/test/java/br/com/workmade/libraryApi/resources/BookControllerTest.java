package br.com.workmade.libraryApi.resources;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.workmade.libraryApi.dtos.BookDTO;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.services.BookService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {
	

	private static final String BOOK_API = "/api/books";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BookService bookService;
	
	@Test
	@DisplayName("Deve criar um livro com sucesso")
	public void createBookTest() throws Exception{
		var bookDTO = BookDTO.builder().title("Meu Livro").author("Author").isbn("1213212").build();
		
		var book = Book.builder().id(101L).title("Meu Livro").author("Author").isbn("1213212").build();
		
		BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(book);
		
		
		String json = new ObjectMapper().writeValueAsString(bookDTO);
		var request = MockMvcRequestBuilders.post(BOOK_API).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).content(json);
		
		mvc.perform(request)
		.andExpect(status().isCreated())
		.andExpect(jsonPath("id").value(101L))
		.andExpect(jsonPath("title").value(bookDTO.getTitle()))
		.andExpect(jsonPath("author").value(bookDTO.getAuthor()))
		.andExpect(jsonPath("isbn").value(bookDTO.getIsbn()));
		
	}
	
	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados sufucuente para criação de um livro")
	public void createInvalidBookTest() {
		
		
		
	}
	
}
