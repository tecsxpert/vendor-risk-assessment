package com.internship.tool.service;

import com.internship.tool.entity.Vendor;
import com.internship.tool.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class VendorRiskReportService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorRiskService riskService;

    // ✅ Generate aggregated risk report
    public Map<String, Object> generateReport() {
        List<Vendor> vendors = vendorRepository.findAll();
        int totalScore = 0;
        int highestScore = 0;
        Vendor highestRiskVendor = null;

        for (Vendor v : vendors) {
            int score = riskService.calculateRiskScore(v);
            totalScore += score;
            if (score > highestScore) {
                highestScore = score;
                highestRiskVendor = v;
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("totalVendors", vendors.size());
        report.put("averageRisk", vendors.isEmpty() ? 0 : totalScore / vendors.size());
        report.put("highestRiskVendor", highestRiskVendor != null ? highestRiskVendor.getName() : "None");
        report.put("highestScore", highestScore);

        return report;
    }
}
