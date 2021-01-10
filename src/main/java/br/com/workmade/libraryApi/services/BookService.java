package br.com.workmade.libraryApi.services;

import br.com.workmade.libraryApi.models.Book;

public interface BookService {

	Book save(Book book);
	
	Book update(Book book);

	Book findById(Long id);
	
	void deleteById(Long id);
	

}
