package com.tcs.customermanagementapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing a customer in the system.
 * Uses Lombok for boilerplate code (getters, setters, toString, etc).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    private UUID id; // Unique identifier for the customer

    @Column(nullable = false)
    private String name; // Name of the customer

    @Column(nullable = false, unique = true)
    private String email; // Email address of the customer, must be unique

    private BigDecimal annualSpend; // Total annual spending of the customer

    private LocalDateTime lastPurchaseDate; // Date and time of the customer's last purchase
}
