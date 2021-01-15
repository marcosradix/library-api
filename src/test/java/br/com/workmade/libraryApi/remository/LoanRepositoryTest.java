package br.com.workmade.libraryApi.remository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.repository.LoanRepository;

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

	        boolean exists = repository.existsByBookAndReturnedFalse(book);

	        assertThat(!exists).isTrue();
	    }
    
    
    public Loan createAndPersistLoan(LocalDate loanDate){
        Book book = BookRepositoryTest.createNewBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(loanDate).build();
        entityManager.persist(loan);

        return loan;
    }
    
	

}
