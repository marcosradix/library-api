package br.com.workmade.libraryApi.resources;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.workmade.libraryApi.dtos.BookDTO;
import br.com.workmade.libraryApi.dtos.LoanDTO;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.services.BookService;
import br.com.workmade.libraryApi.services.LoanService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BookService bookService;
	
	private LoanService loanService;
	
	
    @PostMapping
    public ResponseEntity<BookDTO> save(@RequestBody @Valid BookDTO bookDTO) {
    	
    	Book newBook = modelMapper.map(bookDTO, Book.class);
    	
    	Book bookSaved = bookService.save(newBook);
    	
    	BookDTO newBookDTO = modelMapper.map(bookSaved, BookDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookDTO);
    }
    
    
	
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@RequestBody @Valid BookDTO bookDTO, @PathVariable Long id) {
    	
    	Book bookFound = bookService.findById(id);
    	
    	
    	BeanUtils.copyProperties(bookDTO, bookFound, "id");
    	
    	Book bookSaved = bookService.update(bookFound);
    	
    	BookDTO newBookDTO = modelMapper.map(bookSaved, BookDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(newBookDTO);
    }
    
    @GetMapping
    public Page<BookDTO> find( BookDTO dto, Pageable pageRequest ){
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = bookService.find(filter, pageRequest);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>( list, pageRequest, result.getTotalElements() );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
    	
    	Book bookFound = bookService.findById(id);
    	
    	BookDTO bookFoundDTO = modelMapper.map(bookFound, BookDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(bookFoundDTO);
    }
    
    @GetMapping("{id}/loans")
    public ResponseEntity<Page<LoanDTO>> loanByBook(@PathVariable Long id, Pageable pageable){
    	Book book = bookService.findById(id);
    	Page<LoanDTO> result = loanService.getLoansByBook(book,pageable);
    	List<LoanDTO> listLoanDTO = result.getContent().stream().map(loan ->{
    		Book loanBook = loan.getBook();
    		
    		LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
    		loanDTO.setBook(loanBook);
    		return loanDTO;
    	}).collect(Collectors.toList());
    	PageImpl<LoanDTO> pageImpl = new PageImpl<LoanDTO>(listLoanDTO, pageable, result.getTotalElements());
    	return ResponseEntity.ok(pageImpl);
    	
    }
    
    
    @DeleteMapping("/{id}/remover")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    	
    	Book bookFound = bookService.findById(id);
    	
    	bookService.deleteById(bookFound.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    

}









