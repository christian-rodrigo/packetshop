package com.christianrodrigo.packetshop.customer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerServiceTest {

    @Test
    void cleanDisplayName_removeExtraSpaces() {
        CustomerService customerService = new CustomerService(null);

        String result = customerService.cleanDisplayName("  Maria     Bauer  ");

        assertThat(result).isEqualTo("Maria Bauer");
    }

    @Test
    void normalizeName_cleansNameAndConvertsToLowerCase() {
        CustomerService customerService = new CustomerService(null);

        String result = customerService.normalizeName("   MARIA    Bauer   ");

        assertThat(result).isEqualTo("maria bauer");
    }
}
