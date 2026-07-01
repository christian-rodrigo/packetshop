package com.christianrodrigo.packetshop.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void getOrCreateCustomer_createsCustomerWhenNotExists(){
        Customer customer = customerService.getOrCreateCustomer(" Maria    Bauer   ");

        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getName()).isEqualTo("Maria Bauer");
        assertThat(customer.getNormalizedName()).isEqualTo("maria bauer");

        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    void getOrCreateCustomer_returnsExistingCustomerWhenNameAlreadyExists(){
        Customer first = customerService.getOrCreateCustomer("Maria Bauer");
        Customer second = customerService.getOrCreateCustomer("   MARIA    BaUer   ");

        assertThat(second.getId()).isEqualTo(first.getId());
        assertThat(customerRepository.findAll()).hasSize(1);
    }
}
