package br.com.workmade.libraryApi.remository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // este é um teste de intregração
public class BookRepositoryTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	
	@Autowired
	private BookRepository bookRepository;
	
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com isbn informado")
	public void shouldReturnTrueWhenIsbnExists() {
		
		String isbn = "123";
		
		testEntityManager.persist(Book.builder().title("teste").author("teste").isbn(isbn).build());
		
		boolean existsByIsbn = bookRepository.existsByIsbn(isbn);
		
		assertThat(existsByIsbn).isTrue();
		
		
	}
	
	@Test
	@DisplayName("Deve retornar falso quando não existir um livro na base com isbn informado")
	public void shouldReturnFalseWhenIsbnDoesNotExists() {
	String isbn = "123";
		
		boolean existsByIsbn = bookRepository.existsByIsbn(isbn);
		
		assertThat(existsByIsbn).isFalse();
	}
	
	
	@Test
	@DisplayName("Deve obter um livro por id")
	public void getBookByID() {
		
		Book book = createNewBook();
		
		testEntityManager.persist(book);
		
		Optional<Book> foundBook = bookRepository.findById(book.getId());
		
		assertTrue(foundBook.isPresent());
		
	}
	
	
	private Book createNewBook() {
		return Book.builder().title("Meu Livro").author("Author").isbn("1213212").build();
	}
	

}
