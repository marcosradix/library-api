package br.com.workmade.libraryApi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.repository.BookRepository;
import br.com.workmade.libraryApi.services.impl.BookServiceImpl;


@ExtendWith(SpringExtension.class)
public class BookServiceTest {
	

	@InjectMocks
	private BookService service = new BookServiceImpl();
	//MockBean
	@Mock
	private BookRepository bookRepository;
	
	
	@BeforeEach
	public void setUp() {
		//service = new BookServiceImpl(bookRepository);
	}
	

	
	@DisplayName("Deve salvar um livro")
	@Test
	public void saveBookTest() {
		
		var book = Book.builder().title("As Aventuras").author("Fulano").isbn("123").build();
	
		when(service.save(book)).thenReturn(Book.builder().id(1L).title("As Aventuras").author("Fulano").isbn("123").build());
		Book bookSaved = service.save(book);
		
		
		assertThat(bookSaved.getId()).isNotNull();
		assertThat(bookSaved.getIsbn()).isEqualTo("123");
		assertThat(bookSaved.getTitle()).isEqualTo("As Aventuras");
		assertThat(bookSaved.getAuthor()).isEqualTo("Fulano");
		
		
		
	}

}
