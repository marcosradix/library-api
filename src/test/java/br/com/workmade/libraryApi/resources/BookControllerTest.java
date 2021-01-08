package br.com.workmade.libraryApi.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
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

import br.com.workmade.libraryApi.dtos.BookDTO;
import br.com.workmade.libraryApi.exception.BookNotFoundException;
import br.com.workmade.libraryApi.exception.BusinessException;
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
		var bookDTO = createNewBook();
		
		var book = Book.builder().id(101L).title("Meu Livro").author("Author").isbn("1213212").build();
		
		given(bookService.save(Mockito.any(Book.class))).willReturn(book);
		
		
		String json = new ObjectMapper().writeValueAsString(bookDTO);
		var request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
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
	public void createInvalidBookTest() throws Exception {

		String json = new ObjectMapper().writeValueAsString(new BookDTO());
		var request = MockMvcRequestBuilders.post(BOOK_API).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json);
		
		mvc.perform(request)
		.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("errors", Matchers.hasSize(3)));
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já em uso")
	public void createBookWithDuplicatedIsbn() throws Exception {
		String messageErro = "Isbn já cadastrado";
		String json = new ObjectMapper().writeValueAsString(createNewBook());
		
		given(bookService.save(any(Book.class))).willThrow(new BusinessException(messageErro));
		
		var request = MockMvcRequestBuilders.post(BOOK_API).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json);
		
		mvc.perform(request)
		.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("errors", Matchers.hasSize(1)))
		.andExpect(jsonPath("errors[0]").value(messageErro));
		
	}
	
	private BookDTO createNewBook() {
		return BookDTO.builder().title("Meu Livro").author("Author").isbn("1213212").build();
	}
	
	@Test
	@DisplayName("Deve obter informações de um livro")
	public void getBookDetails() throws Exception {
		Long id = 11L;
		Book book = new Book();
		BeanUtils.copyProperties(createNewBook(), book);
		book.setId(id);
		
		given(bookService.findById(id)).willReturn(book);
		
		var request = MockMvcRequestBuilders.get(BOOK_API.concat("/"+id))
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
		.andExpect(status().isOk())
		.andExpect(jsonPath("id").value(11L))
		.andExpect(jsonPath("title").value(book.getTitle()))
		.andExpect(jsonPath("author").value(book.getAuthor()))
		.andExpect(jsonPath("isbn").value(book.getIsbn()));
		
	}
	
	@Test
	@DisplayName("Deve retornar uma exception de livro não existente quando não encontrar o livro")
	public void shouldReturnBookNotFoundException() throws Exception {
		Long id = Mockito.anyLong();
		String messageErro = "Livro não encontrado";
		given(bookService.findById(id)).willThrow(new BookNotFoundException("Livro não encontrado"));
		
		var request = MockMvcRequestBuilders.get(BOOK_API.concat("/"+id))
				.contentType(MediaType.APPLICATION_JSON);
		

		mvc.perform(request)
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()))
		.andExpect(jsonPath("errors", Matchers.hasSize(1)))
		.andExpect(jsonPath("errors[0]").value(messageErro));
		
	}
	
	
	@Test
	@DisplayName("Deve deletar um livro e retornar status NoContent")
	public void shouldDeleteBookAndReturnStatusNoContent() throws Exception {
		Long id = Mockito.anyLong();

		
		given(bookService.findById(id)).willReturn(Book.builder().id(id).build());
		
		var request = MockMvcRequestBuilders.delete(BOOK_API.concat("/"+id+"/remover"))
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request) .andExpect(status().isNoContent());
		
	}
	
	
	@Test
	@DisplayName("Deve retornar resource not found ao deletar livro inexistente")
	public void shouldThrowBookNotFoundExceptionWhenDeleteBookThatNotExists() throws Exception {
		Long id = Mockito.anyLong();
		
		given(bookService.findById(id)).willThrow(new BookNotFoundException("Livro não encontrado para deletar"));
		
		var request = MockMvcRequestBuilders.delete(BOOK_API.concat("/"+id+"/remover"))
				.contentType(MediaType.APPLICATION_JSON);
		
		String  messageErro = "Livro não encontrado para deletar";
		mvc.perform(request)
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()))
		.andExpect(jsonPath("errors", Matchers.hasSize(1)))
		.andExpect(jsonPath("errors[0]").value(messageErro ));
		
	}
	
}


















