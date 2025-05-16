package com.tcs.customermanagementapi.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.customermanagementapi.dto.CustomerRequest;
import com.tcs.customermanagementapi.dto.CustomerResponse;
import com.tcs.customermanagementapi.service.CustomerService;

import jakarta.validation.Valid;

/**
 * REST controller for managing customers.
 * Provides CRUD endpoints and business logic for tier calculation.
 */
@RestController
@RequestMapping("/customer")
@Validated
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * Create a new customer.
     * @param request CustomerRequest DTO
     * @return Created CustomerResponse with calculated tier
     */
    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerRequest request) {
        if (request.getName() == null || request.getEmail() == null) {
            return ResponseEntity.badRequest().body("Name and email are required");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a customer by ID.
     * @param id Customer UUID
     * @return CustomerResponse or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable UUID id) {
        Optional<CustomerResponse> response = customerService.getCustomerById(id);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    /**
     * Get a customer by name.
     * @param name Customer name
     * @return CustomerResponse or 404 if not found
     */
    @GetMapping(params = "name")
    public ResponseEntity<?> getCustomerByName(@RequestParam String name) {
        Optional<CustomerResponse> response = customerService.getCustomerByName(name);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    /**
     * Get a customer by email.
     * @param email Customer email
     * @return CustomerResponse or 404 if not found
     */
    @GetMapping(params = "email")
    public ResponseEntity<?> getCustomerByEmail(@RequestParam String email) {
        Optional<CustomerResponse> response = customerService.getCustomerByEmail(email);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    /**
     * Update a customer by ID.
     * @param id Customer UUID
     * @param request CustomerRequest DTO
     * @return Updated CustomerResponse or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable UUID id, @Valid @RequestBody CustomerRequest request) {
        if (request.getName() == null || request.getEmail() == null) {
            return ResponseEntity.badRequest().body("Name and email are required");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        Optional<CustomerResponse> response = customerService.updateCustomer(id, request);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    /**
     * Delete a customer by ID.
     * @param id Customer UUID
     * @return 200 OK if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable UUID id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.ok().body("Customer deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }
}
