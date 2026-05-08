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
import javax.mail.MessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.internship.tool.entity.Vendor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Vendor Management", description = "APIs for managing vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/create")
    @Operation(summary = "Create a new vendor", description = "Creates a new vendor and sends a notification email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Vendor.class))),
        @ApiResponse(responseCode = "400", description = "Invalid vendor data",
                content = @Content)
    })
    public Vendor createVendor(@RequestBody Vendor vendor) throws MessagingException {
        Vendor saved = vendorService.save(vendor);
        notificationService.sendVendorCreatedEmail("recipient@example.com", saved.getName());
        return saved;
    }
}
