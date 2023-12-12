package com.pokemoney.commons.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

/**
 * Send email using SMTP with SSL.
 */
@Validated
@Component
public class SmtpEmailService {
    /**
     * The email address of the sender
     */
    @Value("${spring.mail.username}")
    private String from;

    /**
     * The java mail sender
     */
    private final JavaMailSender javaMailSender;

    /**
     * The constructor of SmtpEmailService
     *
     * @param javaMailSender The java mail sender
     */
    public SmtpEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Send email using SMTP with SSL.
     *
     * @param mailProperty The mail dto contains the information of the email which will be sent.
     */
    public void sendMimeMessage(MailProperty mailProperty) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(mailProperty.getTo());
            messageHelper.setSubject(mailProperty.getSubject());

            if (!ObjectUtils.isEmpty(mailProperty.getReplyTo())) {
                messageHelper.setReplyTo(mailProperty.getReplyTo());
            }
            if (!ObjectUtils.isEmpty(mailProperty.getSentDate())) {
                messageHelper.setSentDate(mailProperty.getSentDate());
            }
            if (!ObjectUtils.isEmpty(mailProperty.getCc())) {
                messageHelper.setCc(mailProperty.getCc());
            }
            if(!ObjectUtils.isEmpty(mailProperty.getBcc())) {
                messageHelper.setBcc(mailProperty.getBcc());
            }
            mimeMessage = messageHelper.getMimeMessage();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailProperty.getText(), "text/html;charset=UTF-8");

            if (!ObjectUtils.isEmpty(mailProperty.getFilenames())) {
                // Describe the relationship between the body and the attachment
                MimeMultipart mm = new MimeMultipart();
                mm.setSubType("related");
                mm.addBodyPart(mimeBodyPart);

                // Add attachments
                for (String filename : mailProperty.getFilenames()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    try {
                        attachPart.attachFile(filename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mm.addBodyPart(attachPart);
                }
                mimeMessage.setContent(mm);
            } else {
                mimeMessage.setContent(mailProperty.getText(), "text/html;charset=UTF-8");
            }
            mimeMessage.saveChanges();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mimeMessage);
    }
}
