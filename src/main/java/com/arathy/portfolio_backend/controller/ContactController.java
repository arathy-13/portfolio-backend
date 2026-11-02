package com.arathy.portfolio_backend.controller;

import com.arathy.portfolio_backend.dto.ContactRequest;
import com.arathy.portfolio_backend.entity.ContactMessage;
import com.arathy.portfolio_backend.service.ContactService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> submitContact(
            @Valid @RequestBody ContactRequest request) {

        ContactMessage savedMessage =
                contactService.saveContactMessage(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "status", "success",
                        "message", "Your message has been submitted successfully.",
                        "id", savedMessage.getId().toString()
                ));
    }
}
