package br.com.workmade.libraryApi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.workmade.libraryApi.core.EmailProperties;
import br.com.workmade.libraryApi.models.Loan;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	
	private static final String CRON_LATE_LOANS = "0 0 0 /1 * ?";
	

	private final LoanService loanService;
	
	private EmailService emailService;
	
	private EmailProperties emailProperties;
	
	
	@Scheduled(cron = CRON_LATE_LOANS)
	public void sendEmailToLateLoans() {
		List<Loan> allLateLoans = loanService.getAllLateLoans();
		List<String> mailsList = allLateLoans.stream().map(loan -> loan.getCustomer()).collect(Collectors.toList());
		emailService.sendEmails(mailsList, emailProperties.getMessage());
		
	}

}
