package com.christianrodrigo.packetshop.pickup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PickupEventRepository extends JpaRepository<PickupEvent, Long> {
    List<PickupEvent> findByPickupGroup(PickupGroup pickupGroup);
    List<PickupEvent> findByPickupGroupOrderByIdAsc(PickupGroup pickupGroup);
}
