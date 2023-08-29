package com.pokemoney.commons.mail;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

/**
 * Mail dto contains the information of the email which will be sent.
 */
@Data
@Builder
public class MailDto {

    /**
     * The email address of the reply to.
     * This field is optional.
     */
    private String replyTo;

    /**
     * The email addresses of the receiver
     * This field is required.
     */
    @NotNull
    private String[] to;

    /**
     * The email addresses of the cc
     * This field is optional.
     */
    private String[] cc;

    /**
     * The email addresses of the bcc
     * This field is optional.
     */
    private String[] bcc;

    /**
     * The date when the email is sent
     * This field is optional.
     */
    private Date sentDate;

    /**
     * The subject of the email
     * This field is required.
     */
    @NotNull
    private String subject;

    /**
     * The text of the email
     * This field is required.
     */
    @NotNull
    private String text;

    /**
     * The filenames of the attachments
     * This field is optional.
     */
    private String[] filenames;
}
