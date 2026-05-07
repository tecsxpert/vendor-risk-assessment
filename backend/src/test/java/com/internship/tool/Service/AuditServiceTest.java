package com.internship.tool.service;

import com.internship.tool.model.AuditLog;
import com.internship.tool.repository.AuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Log event should save AuditLog")
    void testLogEvent() {
        AuditLog log = new AuditLog("LOGIN", "user1");

        when(auditRepository.save(log)).thenReturn(log);

        AuditLog saved = auditService.logEvent(log);

        verify(auditRepository, times(1)).save(log);
        assertThat(saved.getAction()).isEqualTo("LOGIN");
        assertThat(saved.getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("Log event should handle repository exception")
    void testLogEventErrorHandling() {
        AuditLog log = new AuditLog("LOGIN", "user1");

        when(auditRepository.save(log)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> auditService.logEvent(log));

        assertThat(ex.getMessage()).isEqualTo("DB error");
        verify(auditRepository, times(1)).save(log);
    }
}
