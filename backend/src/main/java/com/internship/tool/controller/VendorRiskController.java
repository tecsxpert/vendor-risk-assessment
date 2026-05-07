package com.internship.tool.controller;

import com.internship.tool.entity.Vendor;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.VendorRepository;
import com.internship.tool.service.NotificationService;
import com.internship.tool.service.VendorRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
public class VendorRiskController {

    @Autowired
    private VendorRiskService riskService;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private NotificationService notificationService;

    // ✅ Risk score endpoint
    @GetMapping("/{id}/risk")
    public ResponseEntity<Integer> getVendorRisk(@PathVariable Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + id));
        int score = riskService.calculateRiskScore(vendor);
        return ResponseEntity.ok(score);
    }

    // ✅ Vendor creation endpoint with validation + email notification
    @PostMapping("/create")
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        // Step 0: Validate vendor email
        if (vendor.getEmail() == null || !vendor.getEmail().contains("@")) {
            throw new ValidationException("Invalid email format");
        }

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
}

