package br.com.workmade.libraryApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.workmade.libraryApi.models.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

}
