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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.workmade.libraryApi.dtos.BookDTO;
import br.com.workmade.libraryApi.exception.BookNotFoundException;
import br.com.workmade.libraryApi.exception.BusinessException;
import br.com.workmade.libraryApi.exception.handler.ExceptionHandlerApi;
import br.com.workmade.libraryApi.models.Book;
import br.com.workmade.libraryApi.services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BookService bookService;
	
	
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
    
    
    
    
    
    @DeleteMapping("/{id}/remover")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    	
    	Book bookFound = bookService.findById(id);
    	
    	bookService.deleteById(bookFound.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    
   
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionHandlerApi handleValidationExceptions(MethodArgumentNotValidException e) {
    	BindingResult bindResult = e.getBindingResult();
    	return new ExceptionHandlerApi(bindResult);
    }
    
    
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionHandlerApi handleValidationExceptions(BookNotFoundException e) {
    	return new ExceptionHandlerApi(e);
    }
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionHandlerApi handleBookController(BusinessException e) {
    	return new ExceptionHandlerApi(e);
    }

}









