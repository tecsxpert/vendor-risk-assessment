package com.internship.tool.service;

import com.internship.tool.entity.Vendor;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.VendorRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    // ✅ Get Vendor by ID with ResourceNotFoundException
    @Cacheable(value = "vendors", key = "#id", unless = "#result == null")
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id: " + id));
    }

    // ✅ Get all vendors (paginated)
    @Cacheable(value = "vendorsAll", key = "#pageable.pageNumber", unless = "#result == null")
    public Page<Vendor> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable);
    }

    // ✅ Create Vendor with ValidationException
    @CacheEvict(value = {"vendors", "vendorsAll"}, allEntries = true)
    public Vendor createVendor(Vendor vendor) {
        if (vendor.getEmail() == null || !vendor.getEmail().contains("@")) {
            throw new ValidationException("Invalid email format");
        }
        return vendorRepository.save(vendor);
    }

    // ✅ Update Vendor
    @CacheEvict(value = {"vendors", "vendorsAll"}, allEntries = true)
    public Vendor updateVendor(Long id, Vendor vendorDetails) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found

