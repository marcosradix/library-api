package br.com.workmade.libraryApi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.workmade.libraryApi.models.Book;

public interface BookService {

	Book save(Book book);
	
	Book update(Book book);

	Book findById(Long id);
	
	Book findByIsbn(String isbn);
	
	Page<Book> find(Book book, Pageable pageable);
	
	void deleteById(Long id);
	

}
