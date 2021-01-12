package br.com.workmade.libraryApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.workmade.libraryApi.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

	boolean existsByIsbn(String isbn);
	
	Optional<Book> findByIsbnLike(String isbn);
	

}
