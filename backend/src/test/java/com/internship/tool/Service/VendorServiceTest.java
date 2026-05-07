package com.internship.tool.service;

import com.internship.tool.entity.Vendor;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.VendorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorService vendorService;

    private Vendor vendor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Test Vendor");
        vendor.setEmail("test@example.com");
    }

    // ✅ 1. Happy path: createVendor
    @Test
    void testCreateVendor_Success() {
        when(vendorRepository.save(vendor)).thenReturn(vendor);
        Vendor saved = vendorService.createVendor(vendor);
        assertEquals("Test Vendor", saved.getName());
        verify(vendorRepository, times(1)).save(vendor);
    }

    // ✅ 2. Error case: createVendor invalid email
    @Test
    void testCreateVendor_InvalidEmail() {
        vendor.setEmail("invalidEmail");
        assertThrows(ValidationException.class, () -> vendorService.createVendor(vendor));
    }

    // ✅ 3. Happy path: getVendorById
    @Test
    void testGetVendorById_Success() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        Vendor found = vendorService.getVendorById(1L);
        assertEquals("Test Vendor", found.getName());
    }

    // ✅ 4. Error case: getVendorById not found
    @Test
    void testGetVendorById_NotFound() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> vendorService.getVendorById(1L));
    }

    // ✅ 5. Happy path: getAllVendors
    @Test
    void testGetAllVendors_Success() {
        when(vendorRepository.findAll()).thenReturn(Arrays.asList(vendor));
        List<Vendor> vendors = vendorService.getAllVendors();
        assertEquals(1, vendors.size());
    }

    // ✅ 6. Happy path: updateVendor
    @Test
    void testUpdateVendor_Success() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(vendorRepository.save(vendor)).thenReturn(vendor);
        Vendor updated = vendorService.updateVendor(1L, vendor);
        assertEquals("Test Vendor", updated.getName());
    }

    // ✅ 7. Error case: updateVendor not found
    @Test
    void testUpdateVendor_NotFound() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> vendorService.updateVendor(1L, vendor));
    }

    // ✅ 8. Happy path: deleteVendor
    @Test
    void testDeleteVendor_Success() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        vendorService.deleteVendor(1L);
        verify(vendorRepository, times(1)).delete(vendor);
    }

    // ✅ 9. Error case: deleteVendor not found
    @Test
    void testDeleteVendor_NotFound() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> vendorService.deleteVendor(1L));
    }

    // ✅ 10. Error case: createVendor null email
    @Test
    void testCreateVendor_NullEmail() {
        vendor.setEmail(null);
        assertThrows(ValidationException.class, () -> vendorService.createVendor(vendor));
    }
}
