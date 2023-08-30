package com.pokemoney.commons.mail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This is a test class for SmtpEmail.
 */
@SpringBootTest
public class SmtpEmailTest {

    /**
     * The SmtpEmail instance.
     */
    @Autowired
    private SmtpEmail smtpEmail;

    /**
     * The basic mailDto.
     */
    private MailDto mailDto;

    /**
     * setUp method.
     */
    @BeforeEach
    public void setUp() {
        mailDto = MailDto.builder()
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
        smtpEmail.sendMimeMessage(mailDto);
    }

    /**
     * Test sendEmail method without required content.
     * It should throw subject must not be null
     */
    @Test
    public void testSendEmailWithoutRequiredContent() {
        mailDto.setSubject(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> smtpEmail.sendMimeMessage(mailDto));
    }
}
