package br.com.workmade.libraryApi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryApiApplicationTests {

	@Test
	@DisplayName("primeiro teste")
	void contextLoads() {
		LibraryApiApplication.main(new String[] {});
	}

}
