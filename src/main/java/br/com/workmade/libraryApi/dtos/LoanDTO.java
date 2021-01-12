package br.com.workmade.libraryApi.dtos;

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
	private String isbn;
	private String customer;
	

}
