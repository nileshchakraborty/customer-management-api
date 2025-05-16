package com.tcs.customermanagementapi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.tcs.customermanagementapi.dto.CustomerRequest;
import com.tcs.customermanagementapi.dto.CustomerResponse;
import com.tcs.customermanagementapi.model.Customer;
import com.tcs.customermanagementapi.repository.CustomerRepository;

class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTierCalculation_Silver() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Silver User");
        customer.setEmail("silver@example.com");
        customer.setAnnualSpend(BigDecimal.valueOf(500));
        customer.setLastPurchaseDate(LocalDateTime.now().minusMonths(2));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerRequest req = new CustomerRequest();
        req.setName("Silver User");
        req.setEmail("silver@example.com");
        req.setAnnualSpend(BigDecimal.valueOf(500));
        req.setLastPurchaseDate(LocalDateTime.now().minusMonths(2));
        CustomerResponse resp = customerService.createCustomer(req);
        assertEquals("Silver", resp.getTier());
    }

    @Test
    void testTierCalculation_Gold() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Gold User");
        customer.setEmail("gold@example.com");
        customer.setAnnualSpend(BigDecimal.valueOf(2000));
        customer.setLastPurchaseDate(LocalDateTime.now().minusMonths(6));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerRequest req = new CustomerRequest();
        req.setName("Gold User");
        req.setEmail("gold@example.com");
        req.setAnnualSpend(BigDecimal.valueOf(2000));
        req.setLastPurchaseDate(LocalDateTime.now().minusMonths(6));
        CustomerResponse resp = customerService.createCustomer(req);
        assertEquals("Gold", resp.getTier());
    }

    @Test
    void testTierCalculation_Platinum() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Platinum User");
        customer.setEmail("platinum@example.com");
        customer.setAnnualSpend(BigDecimal.valueOf(15000));
        customer.setLastPurchaseDate(LocalDateTime.now().minusMonths(2));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerRequest req = new CustomerRequest();
        req.setName("Platinum User");
        req.setEmail("platinum@example.com");
        req.setAnnualSpend(BigDecimal.valueOf(15000));
        req.setLastPurchaseDate(LocalDateTime.now().minusMonths(2));
        CustomerResponse resp = customerService.createCustomer(req);
        assertEquals("Platinum", resp.getTier());
    }
}
