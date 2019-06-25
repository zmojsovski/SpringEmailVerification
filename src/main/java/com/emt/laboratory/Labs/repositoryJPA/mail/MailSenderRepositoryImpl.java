package com.emt.laboratory.Labs.repositoryJPA.mail;

import com.emt.laboratory.Labs.models.ConfirmationToken;
import com.emt.laboratory.Labs.models.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MailSenderRepositoryImpl implements MailSenderRepository {

    public static final String MAIL_PATH = "mail/";

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    public String sender;


    public MailSenderRepositoryImpl(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendHtmlMail(String to,
                             String subject,
                             String template,
                             Map<String, String> params) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String htmlBody = composeBody(template, params);


        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);

    }

    private String composeBody(String template,
                               Map<String,
                                       String> params) throws IOException {
        InputStream resource = new ClassPathResource(
                MAIL_PATH + template).getInputStream();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource))) {
            String content = reader.lines()
                    .collect(Collectors.joining("\n"));

            for (Map.Entry<String, String> entry : params.entrySet()) {
                content = content.replaceAll(
                        String.format("\\{\\{%s\\}\\}", entry.getKey()),
                        entry.getValue());
            }
            return content;
        }
    }

}
