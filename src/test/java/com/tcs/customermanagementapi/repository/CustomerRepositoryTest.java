package com.tcs.customermanagementapi.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.tcs.customermanagementapi.model.Customer;

@DataJpaTest
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindByNameAndEmail() {
        Customer c = new Customer();
        c.setName("RepoTest");
        c.setEmail("repo@example.com");
        customerRepository.save(c);
        Optional<Customer> byName = customerRepository.findByName("RepoTest");
        Optional<Customer> byEmail = customerRepository.findByEmail("repo@example.com");
        assertTrue(byName.isPresent());
        assertTrue(byEmail.isPresent());
        assertEquals("RepoTest", byName.get().getName());
        assertEquals("repo@example.com", byEmail.get().getEmail());
    }
}
