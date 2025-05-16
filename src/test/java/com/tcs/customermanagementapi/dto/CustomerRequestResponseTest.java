package com.tcs.customermanagementapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CustomerRequestResponseTest {
    @Test
    void testCustomerRequestGettersSetters() {
        CustomerRequest req = new CustomerRequest();
        req.setName("Test");
        req.setEmail("test@example.com");
        req.setAnnualSpend(BigDecimal.valueOf(100));
        LocalDateTime now = LocalDateTime.now();
        req.setLastPurchaseDate(now);
        assertEquals("Test", req.getName());
        assertEquals("test@example.com", req.getEmail());
        assertEquals(BigDecimal.valueOf(100), req.getAnnualSpend());
        assertEquals(now, req.getLastPurchaseDate());
    }

    @Test
    void testCustomerResponseGettersSetters() {
        CustomerResponse resp = new CustomerResponse();
        UUID id = UUID.randomUUID();
        resp.setId(id.toString());
        resp.setName("Test");
        resp.setEmail("test@example.com");
        resp.setAnnualSpend(BigDecimal.valueOf(200));
        LocalDateTime now = LocalDateTime.now();
        resp.setLastPurchaseDate(now);
        resp.setTier("Gold");
        assertEquals(id.toString(), resp.getId());
        assertEquals("Test", resp.getName());
        assertEquals("test@example.com", resp.getEmail());
        assertEquals(BigDecimal.valueOf(200), resp.getAnnualSpend());
        assertEquals(now, resp.getLastPurchaseDate());
        assertEquals("Gold", resp.getTier());
    }
}
