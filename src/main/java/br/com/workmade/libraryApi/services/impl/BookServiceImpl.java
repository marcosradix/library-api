package br.com.workmade.libraryApi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.repository.BookRepository;
import br.com.workmade.libraryApi.services.BookService;

@Service
public class BookServiceImpl implements BookService {

	
	@Autowired
	private BookRepository repository;
	
	
	@Override
	public Book save(Book book) {
		return repository.save(book);
	}

}
