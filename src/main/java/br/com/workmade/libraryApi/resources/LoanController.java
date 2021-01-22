package br.com.workmade.libraryApi.resources;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.workmade.libraryApi.dtos.LoanDTO;
import br.com.workmade.libraryApi.dtos.LoanFilterDTO;
import br.com.workmade.libraryApi.dtos.ReturnedLoanDTO;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.models.Loan;
import br.com.workmade.libraryApi.services.BookService;
import br.com.workmade.libraryApi.services.LoanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
	
	@Autowired
	private  LoanService loanService;
	
	@Autowired
	private  BookService bookService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping
	public ResponseEntity<Long> create(@RequestBody LoanDTO loanDTO){
		Book book = bookService.findByIsbn(loanDTO.getIsbn());
		Loan loan = Loan.builder().book(book).customer(loanDTO.getCustomer()).loanDate(LocalDate.now()).returned(false).build();
		
		Loan bookSave = loanService.save(loan);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(bookSave.getId());
		
	}
	
	@PatchMapping("/{id}")
	public void returnedLoanUpdate(@RequestBody ReturnedLoanDTO returnedLoanDTO,@PathVariable Long id){
	 Loan loanFound = loanService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	 loanFound.setReturned(returnedLoanDTO.getReturned());
	  	loanService.save(loanFound);
		
	}
	
    @GetMapping
    public Page<LoanDTO> find( LoanFilterDTO dto, Pageable pageRequest ){
    	LoanFilterDTO filter = modelMapper.map(dto, LoanFilterDTO.class);
        
        Page<Loan> result = loanService.find(filter, pageRequest);
        List<LoanDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, LoanDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<LoanDTO>( list, pageRequest, result.getTotalElements() );
    }
	
	
}
