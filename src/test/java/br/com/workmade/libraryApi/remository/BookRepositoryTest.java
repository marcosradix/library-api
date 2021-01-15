package br.com.workmade.libraryApi.remository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBook() {
		
		var book = createNewBook();

		Book bookSaved = bookRepository.save(book);
		
		assertNotNull(bookSaved.getId());
		
	}
	
	@Test
	@DisplayName("Deve remover um livro")
	public void deleteBook() {
		
		Book book = createNewBook();
		
		testEntityManager.persist(book);
		
		Book bookFound = testEntityManager.find(Book.class, book.getId());
		assertNotNull(bookFound);
		bookRepository.delete(bookFound);
		
		Book bookDeleted = testEntityManager.find(Book.class, book.getId());
		assertNull(bookDeleted);

	}
	
	private Book createNewBook() {
		return Book.builder().title("Meu Livro").author("Author").isbn("1213212").build();
	}
	
    public static Book createNewBook(String isbn) {
        return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
    }

}
