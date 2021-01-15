package br.com.workmade.libraryApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{
	
	boolean existsByBookAndReturnedFalse(Book book);

}
