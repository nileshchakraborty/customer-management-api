package com.tcs.customermanagementapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO for customer responses, including calculated tier.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerResponse extends CustomerRequest {
    private String id;
    private String tier;

    // Lombok will generate getters, setters, toString, equals, and hashCode
}
