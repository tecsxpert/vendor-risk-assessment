package com.internship.tool.controller;

import com.internship.tool.entity.Vendor;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.VendorRepository;
import com.internship.tool.service.NotificationService;
import com.internship.tool.service.VendorRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/vendors")
@Tag(name = "Vendor Risk Management", description = "APIs for vendor risk assessment and management")
public class VendorRiskController {

    @Autowired
    private VendorRiskService riskService;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private NotificationService notificationService;

    // ✅ Risk score endpoint
    @GetMapping("/{id}/risk")
    @Operation(summary = "Get vendor risk score", description = "Calculates and returns the risk score for a specific vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Risk score calculated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(type = "integer", example = "75"))),
        @ApiResponse(responseCode = "404", description = "Vendor not found",
                content = @Content)
    })
    public ResponseEntity<Integer> getVendorRisk(@Parameter(description = "Vendor ID", example = "1") @PathVariable Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + id));
        int score = riskService.calculateRiskScore(vendor);
        return ResponseEntity.ok(score);
    }

    // ✅ Vendor creation endpoint with validation + email notification
    @PostMapping("/create")
    @Operation(summary = "Create a new vendor", description = "Creates a new vendor with validation and sends notification email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Vendor.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vendor data or email format",
                content = @Content)
    })
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

