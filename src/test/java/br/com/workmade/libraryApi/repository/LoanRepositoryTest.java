package br.com.workmade.libraryApi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest // este é um teste de intregração
public class LoanRepositoryTest {
	
	
	    @Autowired
	    private LoanRepository repository;

	    @Autowired
	    private TestEntityManager entityManager;
	
	
	    @Test
	    @DisplayName("deve verificar se existe empréstimo não devolvido para o livro.")
	    public void existsByBookAndNotReturnedTest(){
	        //cenário
	        Loan loan = createAndPersistLoan(LocalDate.now());
	        Book book = loan.getBook();

	        boolean exists = repository.existsByBookAndNotReturned(book);

	        assertThat(exists).isTrue();
	    }
    
	    @Test
	    @DisplayName("deve buscar empréstimo pelo isbn ou customer do livro")
	    public void findByBookIsbnOrCustomer() {
	    	  Loan loan = createAndPersistLoan(LocalDate.now());

		    Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));
		    assertThat(result.getContent()).contains(loan);
	    	assertThat(result.getContent()).hasSize(1);
	    	assertThat(result.getPageable().getPageSize()).isEqualTo(10);
	    	assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
	    	assertThat(result.getTotalElements()).isEqualTo(1);
	    	
	    }
	    
	    @Test
		@DisplayName("Deve obter empréstimos cuja data de empréstimo for menor ou igual tres dias a tras e não retornados")
		public void findByLoanDateLessThanAndNotReturned() {
	    	Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));
	    	List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
	    	
	    	assertThat(result).hasSize(1).contains(loan);
	    }
	    
	    @Test
		@DisplayName("Deve retornar vazio quando não tiver empréstimos atrasados")
		public void notFindByLoanDateLessThanAndNotReturned() {
	    	
	    	List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
	    	
	    	assertThat(result).isEmpty();
	    }
    
    public Loan createAndPersistLoan(LocalDate loanDate){
        Book book = BookRepositoryTest.createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(loanDate).build();
        entityManager.persist(loan);

        return loan;
    }
    
	

}
