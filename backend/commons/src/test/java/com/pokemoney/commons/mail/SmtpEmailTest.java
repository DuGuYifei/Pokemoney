package com.pokemoney.commons.mail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is a test class for SmtpEmailService.
 */
@SpringBootTest
public class SmtpEmailTest {

    /**
     * The SmtpEmailService instance.
     */
    @Autowired
    private SmtpEmailService smtpEmailService;

    /**
     * The basic mailProperty.
     */
    private MailProperty mailProperty;

    /**
     * setUp method.
     */
    @BeforeEach
    public void setUp() {
        mailProperty = MailProperty.builder()
                .to(new String[]{"s188026@student.pg.edu.pl"})
                .subject("Test")
                .text("Test")
                .build();
    }

    /**
     * Test sendEmail method with basic contents.
     */
    @Test
    public void testSendSimpleEmail() {
        Assertions.assertDoesNotThrow(() -> smtpEmailService.sendMimeMessage(mailProperty));
    }

    /**
     * Test sendEmail method without required content.
     * It should throw subject must not be null
     */
    @Test
    public void testSendEmailWithoutRequiredContent() {
        mailProperty.setSubject(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> smtpEmailService.sendMimeMessage(mailProperty));
    }
}
