package br.com.workmade.libraryApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{
	
	@Query("select case when (count(l.id) > 0) then true else false end from Loan l where l.book =:book and (l.returned is null or l.returned is not true)")
	boolean existsByBookAndNotReturned(@Param("book") Book book);

}
