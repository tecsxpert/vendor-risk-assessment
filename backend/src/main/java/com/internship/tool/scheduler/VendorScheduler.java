package com.internship.tool.scheduler;

import com.internship.tool.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class VendorScheduler {
    private static final Logger logger = LoggerFactory.getLogger(VendorScheduler.class);

    @Autowired
    private VendorService vendorService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendOverdueReminders() {
        logger.info("Running overdue reminder job");
        vendorService.processOverdueVendors();
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void sendUpcomingDeadlineAlerts() {
        logger.info("Running 7-day alert job");
        vendorService.processUpcomingDeadlines();
    }

    @Scheduled(cron = "0 0 8 * * MON")
    public void sendWeeklySummary() {
        logger.info("Running weekly summary job");
        vendorService.generateWeeklySummary();
    }
}