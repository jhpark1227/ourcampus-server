package com.example.school.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

//@Configuration
@PropertySource("classpath:application.yml")
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean startlls_required;
    @Value("${spring.mail.username}")
    private String id;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }
    private Properties getMailProperties()
    {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);
        properties.put("mail.smtp.starttls.required", startlls_required);
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return properties;
    }
}
