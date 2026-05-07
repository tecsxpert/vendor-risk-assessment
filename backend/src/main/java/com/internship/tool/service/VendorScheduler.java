package com.internship.tool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import java.util.List;

import com.internship.tool.entity.Vendor;

@Component
public class VendorScheduler {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * ?") // every day at 9 AM
    public void notifyOverdueVendors() throws MessagingException {
        List<Vendor> overdue = vendorService.findOverdueVendors();
        for (Vendor v : overdue) {
            notificationService.sendOverdueVendorEmail("recipient@example.com", v.getName());
        }
    }
}
