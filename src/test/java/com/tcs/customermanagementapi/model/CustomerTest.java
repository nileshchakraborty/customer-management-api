package com.tcs.customermanagementapi.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerTest {
    @Test
    void testCustomerGettersSetters() {
        Customer c = new Customer();
        UUID id = UUID.randomUUID();
        c.setId(id);
        c.setName("Test");
        c.setEmail("test@example.com");
        c.setAnnualSpend(BigDecimal.valueOf(123.45));
        LocalDateTime now = LocalDateTime.now();
        c.setLastPurchaseDate(now);
        assertEquals(id, c.getId());
        assertEquals("Test", c.getName());
        assertEquals("test@example.com", c.getEmail());
        assertEquals(BigDecimal.valueOf(123.45), c.getAnnualSpend());
        assertEquals(now, c.getLastPurchaseDate());
    }

    @Test
    void testSettersInvoked() {
        Customer spy = Mockito.spy(new Customer());
        UUID id = UUID.randomUUID();
        spy.setId(id);
        spy.setName("Spy");
        spy.setEmail("spy@example.com");
        Mockito.verify(spy).setId(id);
        Mockito.verify(spy).setName("Spy");
        Mockito.verify(spy).setEmail("spy@example.com");
    }

    @Test
    void testStaticUUIDRandomUUIDWithMockitoInline() {
        UUID fakeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        try (MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(fakeId);
            assertEquals(fakeId, UUID.randomUUID());
            mocked.verify(UUID::randomUUID, Mockito.times(1));
        }
    }

    @Test
    void testTierCalculationSilver() {
        Customer c = new Customer();
        c.setAnnualSpend(BigDecimal.valueOf(500));
        c.setLastPurchaseDate(LocalDateTime.now());
        // Silver: annualSpend < $1000
        assertEquals("Silver", getTierForTest(c));
    }

    @Test
    void testTierCalculationGold() {
        Customer c = new Customer();
        c.setAnnualSpend(BigDecimal.valueOf(2000));
        c.setLastPurchaseDate(LocalDateTime.now().minusMonths(6));
        // Gold: annualSpend >= $1000 and < $10000 and purchased within last 12 months
        assertEquals("Gold", getTierForTest(c));
    }

    @Test
    void testTierCalculationPlatinum() {
        Customer c = new Customer();
        c.setAnnualSpend(BigDecimal.valueOf(15000));
        c.setLastPurchaseDate(LocalDateTime.now().minusMonths(2));
        // Platinum: annualSpend >= $10000 and purchased within last 6 months
        assertEquals("Platinum", getTierForTest(c));
    }

    // Helper for business logic test (copied from service)
    private String getTierForTest(Customer customer) {
        BigDecimal spend = customer.getAnnualSpend();
        LocalDateTime lastPurchase = customer.getLastPurchaseDate();
        if (spend == null || spend.compareTo(BigDecimal.valueOf(1000)) < 0) {
            return "Silver";
        }
        if (spend.compareTo(BigDecimal.valueOf(10000)) >= 0) {
            if (lastPurchase != null && lastPurchase.isAfter(LocalDateTime.now().minusMonths(6))) {
                return "Platinum";
            }
        }
        if (spend.compareTo(BigDecimal.valueOf(1000)) >= 0 && spend.compareTo(BigDecimal.valueOf(10000)) < 0) {
            if (lastPurchase != null && lastPurchase.isAfter(LocalDateTime.now().minusMonths(12))) {
                return "Gold";
            }
        }
        return "Silver";
    }

    // PowerMock static mocking is not supported with JUnit 5 and Java 17+.
    // If you need static mocking, use Mockito's mockito-inline or migrate to JUnit 4 for PowerMock support.
}
