package com.internship.tool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;   // ✅ injected here

    public void sendVendorCreatedEmail(String to, String vendorName) throws MessagingException {
        Context context = new Context();
        context.setVariable("vendorName", vendorName);

        // ✅ This line loads vendor-created.html
        String htmlContent = templateEngine.process("vendor-created", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Vendor Created Notification");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendOverdueVendorEmail(String to, String vendorName) throws MessagingException {
        Context context = new Context();
        context.setVariable("vendorName", vendorName);

        // ✅ This line loads vendor-overdue.html
        String htmlContent = templateEngine.process("vendor-overdue", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Vendor Overdue Notification");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
