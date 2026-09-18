package com.christianrodrigo.packetshop.pickup;

import com.christianrodrigo.packetshop.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PickupGroupRepository extends JpaRepository<PickupGroup, Long> {

    Optional<PickupGroup> findByCustomerAndStatus(Customer customer, PickupStatus status);
    List<PickupGroup> findByStatus(PickupStatus status);
    List<PickupGroup> findByStatusOrderByPickupNumberAsc(PickupStatus status);
}
