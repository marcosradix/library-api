package br.com.workmade.libraryApi.services.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.workmade.libraryApi.exception.BookByIsbnNotFoundException;
import br.com.workmade.libraryApi.exception.BookNotFoundException;
import br.com.workmade.libraryApi.exception.BusinessException;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.repository.BookRepository;
import br.com.workmade.libraryApi.services.BookService;

@Service
public class BookServiceImpl implements BookService {

	

	private BookRepository repository;
	
	
	public BookServiceImpl(BookRepository bookRepository) {
		this.repository = bookRepository;
	}


	public BookServiceImpl() {

	}


	@Override
	public Book save(Book book) {
		if(repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn já cadastrado");
			
		}
		return repository.save(book);
	}


	@Override
	public Book findById(Long id) {
		return repository.findById(id).orElseThrow(() ->  new BookNotFoundException("Livro não encontrado"));
	}


	@Override
	public void deleteById(Long id) {
		findById(id);
		repository.deleteById(id);
		
	}


	@Override
	public Book update(Book book) {
		findById(book.getId());
		return repository.save(book);
	}


	@Override
    public Page<Book> find( Book filter, Pageable pageRequest ) {
        Example<Book> example = Example.of(filter,
                    ExampleMatcher
                            .matching()
                            .withIgnoreCase()
                            .withIgnoreNullValues()
                            .withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING )
        );
        return repository.findAll(example, pageRequest);
    }


	@Override
	public Book findByIsbn(String isbn) {
		return repository.findByIsbnContainingIgnoreCase(isbn).orElseThrow(() -> new BookByIsbnNotFoundException(isbn));
	}

}





