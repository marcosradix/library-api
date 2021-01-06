package br.com.workmade.libraryApi.resources;


import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.workmade.libraryApi.ExceptionHandlerApi;
import br.com.workmade.libraryApi.dtos.BookDTO;
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
    public ResponseEntity<BookDTO> salvar(@RequestBody @Valid BookDTO bookDTO) {
    	
    	Book newBook = modelMapper.map(bookDTO, Book.class);
    	
    	Book bookSaved = bookService.save(newBook);
    	
    	BookDTO newBookDTO = modelMapper.map(bookSaved, BookDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBookDTO);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionHandlerApi handleValidationExceptions(MethodArgumentNotValidException e) {
    	BindingResult bindResult = e.getBindingResult();
    	return new ExceptionHandlerApi(bindResult);
    }

}
