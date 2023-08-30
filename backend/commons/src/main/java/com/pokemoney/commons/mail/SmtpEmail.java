package com.pokemoney.commons.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

/**
 * Send email using SMTP with SSL.
 */
@Component
public class SmtpEmail {

    /**
     * The java mail sender
     */
    private final JavaMailSender javaMailSender;

    /**
     * The constructor of SmtpEmail
     * @param javaMailSender The java mail sender
     */
    public SmtpEmail(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Send email using SMTP with SSL.
     * @param mailDto The mail dto contains the information of the email which will be sent.
     */
    public void sendMimeMessage(MailDto mailDto) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setTo(mailDto.getTo());
            messageHelper.setSubject(mailDto.getSubject());

            if (!ObjectUtils.isEmpty(mailDto.getReplyTo())) {
                messageHelper.setReplyTo(mailDto.getReplyTo());
            }
            if (!ObjectUtils.isEmpty(mailDto.getSentDate())) {
                messageHelper.setSentDate(mailDto.getSentDate());
            }
            if (!ObjectUtils.isEmpty(mailDto.getCc())) {
                messageHelper.setCc(mailDto.getCc());
            }
            if(!ObjectUtils.isEmpty(mailDto.getBcc())) {
                messageHelper.setBcc(mailDto.getBcc());
            }
            mimeMessage = messageHelper.getMimeMessage();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailDto.getText(), "text/html;charset=UTF-8");

            if (!ObjectUtils.isEmpty(mailDto.getFilenames())) {
                // Describe the relationship between the body and the attachment
                MimeMultipart mm = new MimeMultipart();
                mm.setSubType("related");
                mm.addBodyPart(mimeBodyPart);

                // Add attachments
                for (String filename : mailDto.getFilenames()) {
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
                mimeMessage.setContent(mailDto.getText(), "text/html;charset=UTF-8");
            }
            mimeMessage.saveChanges();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mimeMessage);
    }
}
