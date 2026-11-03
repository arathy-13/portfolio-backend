
package com.arathy.portfolio_backend.service;

import com.arathy.portfolio_backend.entity.ContactMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResendEmailService {

    private static final Logger logger =
            LoggerFactory.getLogger(ResendEmailService.class);

    private final RestClient restClient;
    private final String apiKey;
    private final String notificationEmail;
    private final String fromEmail;

    public ResendEmailService(
            @Value("${RESEND_API_KEY:}") String apiKey,
            @Value("${CONTACT_NOTIFICATION_EMAIL:}") String notificationEmail,
            @Value("${RESEND_FROM_EMAIL:Arathy Portfolio <onboarding@resend.dev>}") String fromEmail
    ) {
        this.apiKey = apiKey;
        this.notificationEmail = notificationEmail;
        this.fromEmail = fromEmail;
        this.restClient = RestClient.create("https://api.resend.com");
    }

    @Async
    public void sendContactNotification(ContactMessage contact) {

        if (apiKey.isBlank() || notificationEmail.isBlank()) {
            logger.warn("Resend email notification skipped. Check environment variables.");
            return;
        }

        String emailText = """
                You have received a new message through your portfolio!

                Name: %s
                Email: %s
                Subject: %s

                Message:
                %s
                """.formatted(
                contact.getName(),
                contact.getEmail(),
                contact.getSubject(),
                contact.getMessage()
        );

        Map<String, Object> emailBody = new HashMap<>();

        emailBody.put("from", fromEmail);
        emailBody.put("to", List.of(notificationEmail));
        emailBody.put("reply_to", List.of(contact.getEmail()));
        emailBody.put("subject", "New Portfolio Contact: " + contact.getSubject());
        emailBody.put("text", emailText);

        try {
            restClient.post()
                    .uri("/emails")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(emailBody)
                    .retrieve()
                    .toBodilessEntity();

            logger.info("Portfolio contact notification accepted by Resend.");

        } catch (RestClientException exception) {
            logger.error("Failed to send portfolio contact notification through Resend.",
                    exception);
        }
    }
}
