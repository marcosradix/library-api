package br.com.workmade.libraryApi.services.impl;

import org.springframework.stereotype.Service;

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

}





