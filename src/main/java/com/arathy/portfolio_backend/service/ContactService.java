package com.arathy.portfolio_backend.service;

import com.arathy.portfolio_backend.dto.ContactRequest;
import com.arathy.portfolio_backend.entity.ContactMessage;
import com.arathy.portfolio_backend.repository.ContactRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    // Constructor injection
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // Save contact message
    @Transactional
    public ContactMessage saveContactMessage(ContactRequest request) {

        ContactMessage contactMessage = new ContactMessage();

        contactMessage.setName(request.getName().trim());
        contactMessage.setEmail(request.getEmail().trim());
        contactMessage.setSubject(request.getSubject().trim());
        contactMessage.setMessage(request.getMessage().trim());

        return contactRepository.save(contactMessage);
    }
}
