package com.internship.tool.repository;

import com.internship.tool.model.Vendor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VendorRepositoryTest {

    @Autowired
    private VendorRepository vendorRepository;

    @Test
    @DisplayName("Save and find by ID")
    void testSaveAndFindById() {
        Vendor vendor = new Vendor();
        vendor.setName("Test Vendor");
        vendor.setEmail("test@vendor.com");

        Vendor saved = vendorRepository.save(vendor);
        Optional<Vendor> found = vendorRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@vendor.com");
    }

    @Test
    @DisplayName("Find all vendors")
    void testFindAll() {
        Vendor v1 = new Vendor();
        v1.setName("Vendor One");
        v1.setEmail("one@vendor.com");

        Vendor v2 = new Vendor();
        v2.setName("Vendor Two");
        v2.setEmail("two@vendor.com");

        vendorRepository.save(v1);
        vendorRepository.save(v2);

        List<Vendor> vendors = vendorRepository.findAll();
        assertThat(vendors).hasSize(2);
    }

    @Test
    @DisplayName("Find by name")
    void testFindByName() {
        Vendor vendor = new Vendor();
        vendor.setName("Unique Vendor");
        vendor.setEmail("unique@vendor.com");

        vendorRepository.save(vendor);

        Optional<Vendor> found = vendorRepository.findByName("Unique Vendor");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Unique Vendor");
    }

    @Test
    @DisplayName("Find by name not found")
    void testFindByNameNotFound() {
        Optional<Vendor> found = vendorRepository.findByName("Nonexistent");

        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Custom query: find by email")
    void testFindByEmail() {
        Vendor vendor = new Vendor();
        vendor.setName("Email Vendor");
        vendor.setEmail("email@vendor.com");
        vendorRepository.save(vendor);

        Optional<Vendor> found = vendorRepository.findByEmail("email@vendor.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Email Vendor");
    }
}
