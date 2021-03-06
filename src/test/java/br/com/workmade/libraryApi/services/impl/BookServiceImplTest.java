package br.com.workmade.libraryApi.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.exception.BookByIsbnNotFoundException;
import br.com.workmade.libraryApi.exception.BookNotFoundException;
import br.com.workmade.libraryApi.exception.BusinessException;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.repository.BookRepository;
import br.com.workmade.libraryApi.services.BookService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceImplTest {

	@InjectMocks
	private BookService service = new BookServiceImpl();

	// private BookService service;//usar mockBean no repository

	// @MockBean
	@Mock
	private BookRepository bookRepository;

	@BeforeEach
	public void setUp() {
		// service = new BookServiceImpl(bookRepository);// caso queira passar via
		// construtor trocar para mockBean no repository
	}

	@DisplayName("Deve salvar um livro")
	@Test
	public void saveBookTest() {

		var book = createValidBook();

		when(service.save(book))
				.thenReturn(Book.builder().id(1L).title("As Aventuras").author("Fulano").isbn("123").build());
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

		Throwable exception = Assertions.catchThrowable(() -> service.save(book));
		String messageErro = "Isbn já cadastrado";
		assertThat(exception).isInstanceOf(BusinessException.class).hasMessage(messageErro);

		verify(bookRepository, never()).save(book);

	}

	@DisplayName("Deve obter um livro por id")
	@Test
	public void getBookById() {
		Long id = 1L;
		Book validBook = createValidBook();
		validBook.setId(id);
		when(bookRepository.findById(id)).thenReturn(Optional.of(validBook));

		Book foundBook = service.findById(id);

		assertNotNull(foundBook);
		assertThat(foundBook.getId()).isEqualTo(id);
		assertThat(foundBook.getTitle()).isEqualTo(validBook.getTitle());
		assertThat(foundBook.getAuthor()).isEqualTo(validBook.getAuthor());
		assertThat(foundBook.getIsbn()).isEqualTo(validBook.getIsbn());

	}

	@DisplayName("Deve retornar booknotfoundexception para livro não encontrado")
	@Test
	public void bookNotFoundByIdException() {
		Long id = Mockito.anyLong();
		String message = "Livro não encontrado";

		when(bookRepository.findById(id)).thenThrow(new BookNotFoundException(message));

		assertThrows(BookNotFoundException.class, () -> {
			service.findById(id);
		});

	}

	@DisplayName("Deve retornar o livro atualizado")
	@Test
	public void updateBook() {

		var bookToUpdate = Book.builder().id(1L).title("As Aventuras").author("Fulano").isbn("123").build();

		var bookUpdated = Book.builder().id(1L).title("As Aventuras 2").author("Fulano 2").isbn("1232").build();
		when(bookRepository.findById(bookToUpdate.getId())).thenReturn(Optional.ofNullable(bookToUpdate));
		when(service.update(bookToUpdate)).thenReturn(bookUpdated);
		Book bookSaved = service.update(bookToUpdate);

		assertThat(bookSaved.getId()).isEqualTo(1L);
		assertThat(bookSaved.getIsbn()).isEqualTo("1232");
		assertThat(bookSaved.getTitle()).isEqualTo("As Aventuras 2");
		assertThat(bookSaved.getAuthor()).isEqualTo("Fulano 2");

		verify(bookRepository, times(1)).save(bookToUpdate);

	}

	@DisplayName("Deve lançar BookNotFoundException ao tentar atualizar livro inexistente")
	@Test
	public void bookNotFoundExceptionWhenTryToUpdate() {
		var book = Book.builder().build();

		assertThrows(BookNotFoundException.class, () -> {

			service.update(book);
		});

		verify(bookRepository, never()).save(book);

	}

	@DisplayName("Deve remover um livro pelo id")
	@Test
	public void deleteBookById() {
		var book = Book.builder().id(1L).title("As Aventuras").author("Fulano").isbn("123").build();

		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		when(service.save(book))
				.thenReturn(Book.builder().id(1L).title("As Aventuras").author("Fulano").isbn("123").build());

		Book bookSaved = service.save(book);

		assertDoesNotThrow(() -> service.deleteById(bookSaved.getId()));

		verify(bookRepository, times(1)).deleteById(book.getId());

	}

	@DisplayName("Deve lançar BookNotFoundException ao remover um livro pelo id que não existe")
	@Test
	public void bookNotFoundExceptionWhenDeleteById() {
		var book = Book.builder().id(1L).title("As Aventuras").author("Fulano").isbn("123").build();

		assertThrows(BookNotFoundException.class, () -> {

			service.deleteById(book.getId());
		});

		verify(bookRepository, never()).deleteById(book.getId());

	}

	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Deve filtrar livros pelas propriedades")
	public void findBookTest() {
		// cenario
		Book book = createValidBook();

		PageRequest pageRequest = PageRequest.of(0, 10);

		List<Book> lista = Arrays.asList(book);
		Page<Book> page = new PageImpl<Book>(lista, pageRequest, 1);

		when(bookRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

		// execucao
		Page<Book> result = service.find(book, pageRequest);

		// verificacoes
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(lista);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
	}

	private Book createValidBook() {
		return Book.builder().title("As Aventuras").author("Fulano").isbn("123").build();
	}

	@Test
	@DisplayName("Deve retornar um livro pelo isbn")
	public void shouldReturnBookByIsbn() {
		String isbn = "1230";

		when(bookRepository.findByIsbnContainingIgnoreCase(isbn))
				.thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));

		Book bookFound = service.findByIsbn(isbn);

		assertNotNull(bookFound);
		assertThat(bookFound.getId()).isEqualTo(1L);
		assertThat(bookFound.getIsbn()).isEqualTo(isbn);

		verify(bookRepository, times(1)).findByIsbnContainingIgnoreCase(isbn);
	}

	@Test
	@DisplayName("Deve retornar BookByIsbnNotFoundException pelo isbn não encontrado")
	public void shouldReturnBookByIsbnNotFoundExceptionByIsbn() {
		String message = "123";

		when(bookRepository.findByIsbnContainingIgnoreCase(Mockito.anyString()))
				.thenThrow(new BookByIsbnNotFoundException(message));

		assertThrows(BookByIsbnNotFoundException.class, () -> {
			service.findByIsbn(Mockito.anyString());
		});

		verify(bookRepository, times(1)).findByIsbnContainingIgnoreCase(Mockito.anyString());

	}

}
