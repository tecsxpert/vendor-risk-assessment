package com.internship.tool.controller;

import com.internship.tool.service.VendorRiskReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@RestController
@RequestMapping("/api/vendors/risk")
@Tag(name = "Vendor Risk Reports", description = "APIs for generating vendor risk reports")
public class VendorRiskReportController {

    @Autowired
    private VendorRiskReportService reportService;

    @GetMapping("/report")
    @Operation(summary = "Generate risk report", description = "Generates a comprehensive risk report for all vendors")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Risk report generated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(type = "object", example = "{\"totalVendors\": 10, \"highRiskCount\": 2, \"reportDate\": \"2023-10-01\"}")))
    })
    public ResponseEntity<Map<String, Object>> getRiskReport() {
        return ResponseEntity.ok(reportService.generateReport());
    }
}
