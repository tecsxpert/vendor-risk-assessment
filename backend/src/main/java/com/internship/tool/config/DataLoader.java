package com.internship.tool.config;

import com.internship.tool.entity.Vendor;
import com.internship.tool.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VendorRepository vendorRepository;

    private static final String[] COMPANY_NAMES = {
            "TechCore Solutions", "CloudSync Industries", "DataFlow Systems",
            "SecureNet Technologies", "GlobalTrade Partners", "ProLogistics Inc",
            "DigitalWave Corp", "SustainSource Ltd", "FinanceHub International",
            "InnovateTech Ventures", "SmartFactory Solutions", "EcoPartners Global",
            "VendorLink Technologies", "RapidSupply Systems", "PrecisionManufacturing Co",
            "NetworkGuard Security", "CloudBridge Solutions", "DataSecure Partners",
            "IntegrationPlus Corp", "VendorSync Services", "Enterprise Solutions Ltd",
            "GreenTech Industries", "QuantumLeap Innovations", "StreamlineSupply Co",
            "OptimizeCore Systems", "VendorHub International", "NextGen Manufacturing",
            "ProServices Global", "ConnectTrade Industries", "Vendor Excellence Ltd"
    };

    private static final String[] CONTACT_PERSONS = {
            "John Smith", "Maria Garcia", "Ahmed Hassan", "Sarah Johnson",
            "Robert Chen", "Emma Wilson", "Michael Brown", "Lisa Anderson",
            "David Martinez", "Jennifer Lee", "James Taylor", "Patricia Davis",
            "Christopher Rodriguez", "Mary Thompson", "Daniel White", "Barbara Harris",
            "Matthew Martin", "Susan Clark", "Anthony Lewis", "Karen Robinson",
            "Donald Walker", "Nancy Young", "Steven King", "Margaret Wright",
            "Paul Hill", "Diane Scott", "Andrew Green", "Sandra Adams",
            "Joshua Nelson", "Kimberly Carter"
    };

    private static final String[] DESCRIPTIONS = {
            "Excellent compliance record, no issues detected",
            "Good vendor with consistent delivery performance",
            "Multiple delivery delays reported in Q2",
            "Under investigation for quality issues",
            "New vendor, awaiting assessment",
            "Reliable partner, no compliance concerns",
            "Quality issues resolved, monitoring status",
            "Strong financial position, low risk",
            "Moderate compliance concerns, improving",
            "High quality standards maintained",
            "Recent audit passed successfully",
            "Pending compliance certification",
            "Trusted long-term partner",
            "Initial assessment phase ongoing",
            "Performance metrics within acceptable range",
            "Some documentation gaps identified",
            "Excellent communication and responsiveness",
            "Quality metrics show improvement trend",
            "Financial stability verified",
            "On-time delivery rate: 98.5%",
            "Certified ISO 9001 compliant",
            "Awaiting supplier certification",
            "Regular audits show consistent results",
            "Minor process improvements needed",
            "Highly recommended by procurement team",
            "Integration with ERP system pending",
            "Third-party verification completed",
            "Scalability assessment in progress",
            "Meets all regulatory requirements",
            "Strategic partnership potential identified"
    };

    @Override
    public void run(String... args) throws Exception {
        if (vendorRepository.count() > 0) {
            return; // Skip if data already exists
        }

        // Create 30 realistic vendor records
        for (int i = 0; i < 30; i++) {
            Vendor vendor = new Vendor();

            // Set basic info
            vendor.setName(COMPANY_NAMES[i]);
            vendor.setContactPerson(CONTACT_PERSONS[i]);
            vendor.setEmail(generateEmail(COMPANY_NAMES[i], i));
            vendor.setPhone(generatePhone(i));

            // Set risk score with distribution
            int riskScore = calculateRiskScore(i);
            vendor.setRiskScore(riskScore);

            // Set status based on risk score
            vendor.setStatus(getStatus(riskScore));

            // Set description
            vendor.setDescription(DESCRIPTIONS[i]);

            // Set review date (30-90 days in future)
            LocalDate reviewDate = LocalDate.now().plusDays(30 + (i % 60));
            vendor.setReviewDate(reviewDate);

            // Set timestamps
            vendor.setCreatedAt(LocalDateTime.now().minusDays(i % 60));
            vendor.setUpdatedAt(LocalDateTime.now().minusDays(i % 30));

            vendorRepository.save(vendor);
        }
    }

    /**
     * Generate email from company name
     */
    private String generateEmail(String companyName, int index) {
        String domain = companyName.toLowerCase()
                .replaceAll(" ", "")
                .replaceAll("[^a-z0-9]", "");
        return "contact@" + domain + index + ".com";
    }

    /**
     * Generate phone number
     */
    private String generatePhone(int index) {
        int areaCode = 200 + (index % 800);
        int exchange = 100 + ((index * 7) % 900);
        int number = 1000 + ((index * 13) % 9000);
        return String.format("+1-%d-%d-%d", areaCode, exchange, number);
    }

    /**
     * Calculate risk score with realistic distribution
     * Distribution: 7 LOW (0-30), 8 MEDIUM-LOW (31-50), 8 MEDIUM-HIGH (51-70), 7 HIGH (71-100)
     */
    private int calculateRiskScore(int index) {
        if (index < 7) {
            // LOW: 0-30
            return (index * 4) + 5;
        } else if (index < 15) {
            // MEDIUM-LOW: 31-50
            return 31 + ((index - 7) * 2) + 3;
        } else if (index < 23) {
            // MEDIUM-HIGH: 51-70
            return 51 + ((index - 15) * 2) + 3;
        } else {
            // HIGH: 71-100
            return 71 + ((index - 23) * 4) + 2;
        }
    }

    /**
     * Get status based on risk score
     */
    private String getStatus(int riskScore) {
        if (riskScore <= 30) {
            return "LOW";
        } else if (riskScore <= 50) {
            return "MEDIUM";
        } else if (riskScore <= 75) {
            return "HIGH";
        } else {
            return "PENDING"; // Awaiting full assessment
        }
    }
}
