package com.trybyl.emailproject.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("emails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
	
    @Id
    private String id;
    
    private Date createdDateTime;
    private Date lastModifiedDateTime;
    private Date sentDateTime;
    private Boolean hasAttachments;
	private String internetMessageId;
	private String subject;
	private String bodyPreview;
	private String importance;
	private String parentFolderId;
	private String conversationId;
	private String conversationIndex;
	private Boolean isDeliveryReceiptRequested;
	private Boolean isReadReceiptRequested;
	private String webLink;
	private Body body;
    private Boolean isRead;
    private Boolean isDraft;
    private User sender;
    private User from;
    private List<User> toRecipients;
    private List<User> ccRecipients;
    private List<User> bccRecipients;
}


