package com.emt.laboratory.Labs.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Profile("!gmail")
@Configuration
public class DefaultEmailConfig {

    @Value("${app.mail.username}")
    private String username;



    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(2500);

        mailSender.setUsername(username);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.debug", "true");

        return mailSender;
    }

}
