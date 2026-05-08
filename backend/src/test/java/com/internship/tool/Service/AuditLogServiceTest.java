package com.internship.tool.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuditLogServiceTest {

    @Autowired
    private AuditLogService auditLogService;

    @Test
    @DisplayName("Save audit log does not throw exception")
    void testSaveAudit() {
        // This is a stub method, so just ensure it doesn't throw
        auditLogService.saveAudit("CREATE", "Vendor", "old", "new");
        // No assertion needed as it's a void method with print
    }
}