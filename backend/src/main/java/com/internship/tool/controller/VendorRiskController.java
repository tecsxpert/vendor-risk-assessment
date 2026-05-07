package com.internship.tool.controller;

import com.internship.tool.entity.Vendor;
import com.internship.tool.repository.VendorRepository;
import com.internship.tool.service.NotificationService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import jakarta.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping("/vendor")
public class VendorRiskController {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private NotificationService notificationService;

    // ✅ Create Vendor endpoint
    @PostMapping("/create")
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) throws MessagingException {
        // Step 1: Save vendor to DB
        Vendor savedVendor = vendorRepository.save(vendor);

        // Step 2: Trigger email notification
        notificationService.sendVendorCreatedEmail(
            savedVendor.getEmail(),
            savedVendor.getName()
        );

        // Step 3: Return saved vendor in response
        return ResponseEntity.ok(savedVendor);
    }

    // ✅ Get Vendor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        Optional<Vendor> vendor = vendorRepository.findById(id);
        return vendor.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Get all Vendors
    @GetMapping("/all")
    public ResponseEntity<Iterable<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorRepository.findAll());
    }
}

