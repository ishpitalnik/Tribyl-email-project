package com.trybyl.emailproject.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.trybyl.emailproject.entities.Email;

public interface EmailRepository extends MongoRepository<Email, String> {

	@Query("{ 'toRecipientsn.emailAddress.address': ?0 } } }")
	List<Email> findAllByRecipientsnEmailAddress(String emailAddress);
	
	@Query(value = "{ 'sender.emailAddress.address' : ?0, 'subject' : ?1 }") 
	List<Email> findAllBySenderAndSubject(String emailAddress, String subject);
}


//@Query("{name:'?0'}")
//GroceryItem findItemByName(String name);
//
//@Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//List<GroceryItem> findAll(String category);