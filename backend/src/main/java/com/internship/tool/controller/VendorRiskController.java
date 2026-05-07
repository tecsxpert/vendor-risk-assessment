@PostMapping("/create")
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
