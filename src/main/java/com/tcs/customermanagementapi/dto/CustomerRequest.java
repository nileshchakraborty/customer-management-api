package com.tcs.customermanagementapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * DTO for customer creation and update requests.
 */
@Data
public class CustomerRequest {
    private String name;
    private String email;
    private BigDecimal annualSpend;
    private LocalDateTime lastPurchaseDate;
    // Lombok will generate getters, setters, toString, equals, and hashCode
}
