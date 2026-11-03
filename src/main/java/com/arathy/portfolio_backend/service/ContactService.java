
package com.arathy.portfolio_backend.service;

import com.arathy.portfolio_backend.dto.ContactRequest;
import com.arathy.portfolio_backend.entity.ContactMessage;
import com.arathy.portfolio_backend.repository.ContactRepository;

import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final ResendEmailService resendEmailService;

    public ContactService(
            ContactRepository contactRepository,
            ResendEmailService resendEmailService
    ) {
        this.contactRepository = contactRepository;
        this.resendEmailService = resendEmailService;
    }

    public ContactMessage saveContactMessage(ContactRequest request) {

        ContactMessage contactMessage = new ContactMessage();

        contactMessage.setName(request.getName().trim());
        contactMessage.setEmail(request.getEmail().trim());
        contactMessage.setSubject(request.getSubject().trim());
        contactMessage.setMessage(request.getMessage().trim());

        // First, save the message in PostgreSQL
        ContactMessage savedMessage =
                contactRepository.save(contactMessage);

        // Then, send the notification in the background
        resendEmailService.sendContactNotification(savedMessage);

        return savedMessage;
    }
}
