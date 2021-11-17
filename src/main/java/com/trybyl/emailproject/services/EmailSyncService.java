package com.trybyl.emailproject.services;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trybyl.emailproject.entities.Email;
import com.trybyl.emailproject.entities.EmailAddress;
import com.trybyl.emailproject.entities.Emails;
import com.trybyl.emailproject.entities.User;
import com.trybyl.emailproject.exceptions.CustomException;
import com.trybyl.emailproject.repositories.EmailRepository;
import com.trybyl.emailproject.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailSyncService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired 
	private EmailRepository emailRepository;
	
	@Autowired
	private GraphApiService graphApiService;
	
	private ObjectMapper mapper;
	
	@PostConstruct
    private void postConstruct() throws Exception {
		mapper = new ObjectMapper();	
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	
	public void syncAllEmails() {
		try {
			List<User> users = userRepository.findAll();
			if (users.size() == 0) {
				users = setupDefaultUsers();
			}
			
			Set<String> savedEmailIds = emailRepository.findAll()
					.stream()
					.map(email -> email.getId()).
					collect(Collectors.toSet());
			
			for(User user : users) {
				String userName = user.getEmailAddress().getAddress();
				String rawEmailObject = graphApiService.fetchEmails(userName);
				log.info(rawEmailObject);
				Emails emails = mapper.readValue(rawEmailObject, Emails.class);
				
				for (Email email : emails.getValue()) {
					String emailId = email.getId();
					if (!savedEmailIds.contains(emailId)) {
						emailRepository.save(email);
					}
				}
			}
		} catch (Exception ex) {
			log.error("Error fetching message from Graph API", ex);
			throw new CustomException(ex);
		}
		
	}
	
	public List<Email> getEmailsByDateRage(String emailAddress, Date from, Date to) {
		// could not figure out query string for Mondo db
		
		List<Email> allEmails = emailRepository.findAll();
		
		List<Email> result = filterByEmailAddress(allEmails, emailAddress);
		return result.stream().filter((email)-> email.getSentDateTime().after(from))
			.filter((email)-> email.getSentDateTime().before(to))
			.collect(Collectors.toList());
	}
	
	public List<Email> getEmailsByTopic(String emailAddress, String topic) {
		
		List<Email> allEmails = emailRepository.findAll();
		
		List<Email> result = filterByEmailAddress(allEmails, emailAddress);
		return result.stream().filter((email)-> email.getSubject().equalsIgnoreCase(topic))
			.collect(Collectors.toList());
	}
	
	
	private List<Email> filterByEmailAddress(List<Email> allEmails, String emailAddress) {
		List<Email> result = new LinkedList<>();
		for (Email email : allEmails) {
			if (email.getSender().getEmailAddress().getAddress().equalsIgnoreCase(emailAddress)) {
				result.add(email);
				break;
			}
			
			for (User user : email.getToRecipients()) {
				if (user.getEmailAddress().getAddress().equalsIgnoreCase(emailAddress)) {
					result.add(email);
					break;
				}
			}
			
			for (User user : email.getBccRecipients()) {
				if (user.getEmailAddress().getAddress().equalsIgnoreCase(emailAddress)) {
					result.add(email);
					break;
				}
			}
			
			for (User user : email.getCcRecipients()) {
				if (user.getEmailAddress().getAddress().equalsIgnoreCase(emailAddress)) {
					result.add(email);
					break;
				}
			}
		}
		
		return result;
	}
	
	private List<User> setupDefaultUsers() {
		
		User user1 = User.builder()
				.emailAddress(
						EmailAddress.builder()
						.name("IsaiahL")
						.address("isaiahl@tribyl.onmicrosoft.com")
						.build()
				).build();
		
		User user2 = User.builder()
				.emailAddress(
						EmailAddress.builder()
						.name("AdeleV")
						.address("adelev@tribyl.onmicrosoft.com")
						.build()
				).build();
		
		userRepository.save(user1);
		userRepository.save(user2);
		
		return Arrays.asList(user1, user2);
	}
}
