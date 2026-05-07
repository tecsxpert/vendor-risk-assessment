package com.internship.tool.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    public Vendor createVendor(@RequestBody Vendor vendor) throws MessagingException {
        Vendor saved = vendorService.save(vendor);
        notificationService.sendVendorCreatedEmail("recipient@example.com", saved.getName());
        return saved;
    }
}
