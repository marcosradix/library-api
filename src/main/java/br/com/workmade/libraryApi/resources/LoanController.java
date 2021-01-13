package br.com.workmade.libraryApi.resources;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.workmade.libraryApi.dtos.LoanDTO;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.services.BookService;
import br.com.workmade.libraryApi.services.LoanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
	

	private final LoanService loanService;

	private final BookService bookService;
	
	@PostMapping
	
	public ResponseEntity<Long> create(@RequestBody LoanDTO loanDTO){
		Book book = bookService.findByIsbn(loanDTO.getIsbn());
		Loan loan = Loan.builder().book(book).customer(loanDTO.getCustomer()).loanDate(LocalDate.now()).returned(false).build();
		
		Loan bookSave = loanService.save(loan);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(bookSave.getId());
		
	}
	
	
}
