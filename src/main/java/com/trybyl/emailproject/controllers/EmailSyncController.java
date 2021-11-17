package com.trybyl.emailproject.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trybyl.emailproject.entities.Email;
import com.trybyl.emailproject.services.EmailSyncService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/emails")
@CrossOrigin
@RequiredArgsConstructor
public class EmailSyncController {
    private final EmailSyncService emailSyncService;
    
    @PostMapping("sync")
    //@PreAuthorize("hasRole('ROLE_SYNC_EMAILS')")
	public ResponseEntity<Boolean> syncAllEmails() {
	    emailSyncService.syncAllEmails();
	    return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
    
    
    @GetMapping("date")
    //@PreAuthorize("hasRole('ROLE_READ_EMAILS')")
	public ResponseEntity<List<Email>> getEmailsByDateRage(
			@RequestParam String emailAddress, 
			@RequestParam Date from, 
			@RequestParam Date to) {
	    List<Email> emails = emailSyncService.getEmailsByDateRage(emailAddress, from, to);
	    return new ResponseEntity<>(emails, HttpStatus.OK);
	}
    
    @GetMapping("topic")
    //@PreAuthorize("hasRole('ROLE_READ_EMAILS')")
	public ResponseEntity<List<Email>> getEmailsByTopic(
			@RequestParam String emailAddress,
			@RequestParam String topic) {
	    List<Email> emails = emailSyncService.getEmailsByTopic(emailAddress, topic);
	    return new ResponseEntity<>(emails, HttpStatus.OK);
	}
}