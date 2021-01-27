package br.com.workmade.libraryApi.dtos;

import javax.validation.constraints.NotEmpty;

import br.com.workmade.libraryApi.models.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
	
	private Long id;
	@NotEmpty
	private String isbn;
	@NotEmpty
	private String customerEmail;
	@NotEmpty
	private String customer;
	private Book book;
	

}
