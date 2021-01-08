package br.com.workmade.libraryApi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.exception.BusinessException;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.repository.BookRepository;
import br.com.workmade.libraryApi.services.impl.BookServiceImpl;


@ExtendWith(SpringExtension.class)
public class BookServiceTest {
	

	@InjectMocks
	private BookService service = new BookServiceImpl();
	
	//private BookService service;//usar mockBean no repository
	
	
	//@MockBean
	@Mock
	private BookRepository bookRepository;
	
	
	@BeforeEach
	public void setUp() {
		//service = new BookServiceImpl(bookRepository);// caso queira passar via construtor trocar para mockBean no repository
	}
	

	
	@DisplayName("Deve salvar um livro")
	@Test
	public void saveBookTest() {
		
		var book = createValidBook();
	
		when(service.save(book)).thenReturn(Book.builder().id(1L).title("As Aventuras").author("Fulano").isbn("123").build());
		Book bookSaved = service.save(book);
		
		assertThat(bookSaved.getId()).isNotNull();
		assertThat(bookSaved.getIsbn()).isEqualTo("123");
		assertThat(bookSaved.getTitle()).isEqualTo("As Aventuras");
		assertThat(bookSaved.getAuthor()).isEqualTo("Fulano");
		
	}

	@DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn existente")
	@Test
	public void shouldNotSaveBookWithDuplicatedIsbn() {
		Book book = createValidBook();
		
		when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);
		
		Throwable exception = Assertions.catchThrowable(() ->  service.save(book));
		String messageErro = "Isbn já cadastrado";
		assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(messageErro);
		
		verify(bookRepository, never()).save(book);
		
	}
	
	
	private Book createValidBook() {
		return Book.builder().title("As Aventuras").author("Fulano").isbn("123").build();
	}

}















