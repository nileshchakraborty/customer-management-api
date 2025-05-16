package com.tcs.customermanagementapi.service;

import com.tcs.customermanagementapi.dto.CustomerRequest;
import com.tcs.customermanagementapi.dto.CustomerResponse;
import com.tcs.customermanagementapi.model.Customer;
import com.tcs.customermanagementapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for customer business logic and persistence.
 */
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Create a new customer and persist to DB.
     */
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());
        Customer saved = customerRepository.save(customer);
        return toResponse(saved);
    }

    /**
     * Retrieve a customer by UUID.
     */
    public Optional<CustomerResponse> getCustomerById(UUID id) {
        return customerRepository.findById(id).map(this::toResponse);
    }

    /**
     * Retrieve a customer by name.
     */
    public Optional<CustomerResponse> getCustomerByName(String name) {
        return customerRepository.findByName(name).map(this::toResponse);
    }

    /**
     * Retrieve a customer by email.
     */
    public Optional<CustomerResponse> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).map(this::toResponse);
    }

    /**
     * Update an existing customer by UUID.
     */
    @Transactional
    public Optional<CustomerResponse> updateCustomer(UUID id, CustomerRequest request) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setAnnualSpend(request.getAnnualSpend());
            customer.setLastPurchaseDate(request.getLastPurchaseDate());
            return toResponse(customer);
        });
    }

    /**
     * Delete a customer by UUID.
     */
    public boolean deleteCustomer(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Convert Customer entity to CustomerResponse DTO, including tier calculation.
     */
    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId() != null ? customer.getId().toString() : null);
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setAnnualSpend(customer.getAnnualSpend());
        response.setLastPurchaseDate(customer.getLastPurchaseDate());
        response.setTier(calculateTier(customer));
        return response;
    }

    /**
     * Calculate customer tier based on annualSpend and lastPurchaseDate.
     * - Silver: annualSpend < 1000
     * - Gold: 1000 <= annualSpend < 10000
     * - Platinum: annualSpend >= 10000
     */
    private String calculateTier(Customer customer) {
        BigDecimal spend = customer.getAnnualSpend();
        LocalDateTime lastPurchase = customer.getLastPurchaseDate();
        if (spend == null || spend.compareTo(BigDecimal.valueOf(1000)) < 0) {
            return "Silver";
        }
        if (spend.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            if (lastPurchase != null && lastPurchase.isAfter(LocalDateTime.now().minus(6, ChronoUnit.MONTHS))) {
                return "Platinum";
            }
        }
        if (spend.compareTo(BigDecimal.valueOf(1000)) >= 0 && spend.compareTo(BigDecimal.valueOf(10000)) < 0) {
            if (lastPurchase != null && lastPurchase.isAfter(LocalDateTime.now().minus(12, ChronoUnit.MONTHS))) {
                return "Gold";
            }
        }
        return "Silver";
    }
}
