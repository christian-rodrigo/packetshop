package com.christianrodrigo.packetshop.customer;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String cleanDisplayName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }

    public String normalizeName(String name) {
        return cleanDisplayName(name).toLowerCase(Locale.ROOT);
    }

    public Customer getOrCreateCustomer(String inputName) {
        String displayName = cleanDisplayName(inputName);
        String normalizedName = normalizeName(inputName);

        return customerRepository.findByNormalizedName(normalizedName)
                .orElseGet(() -> customerRepository.save(new Customer(displayName, normalizedName)));
    }
}
