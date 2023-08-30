package com.pokemoney.commons.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is a spring main class which is used for test SmtpEmail.
 */
@SpringBootApplication
public class MailTestApplication {
    /**
     * The main method.
     * @param args The arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(MailTestApplication.class, args);
    }
}
