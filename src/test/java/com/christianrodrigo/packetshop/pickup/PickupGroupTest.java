package com.christianrodrigo.packetshop.pickup;

import com.christianrodrigo.packetshop.customer.Customer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PickupGroupTest {

    @Test
    void addPackets_increasesTotalAndRemainingPacketCount(){
        Customer customer = new Customer("Maria Bauer", "maria bauer");
        PickupGroup pickupGroup = new PickupGroup(1, customer, 2);

        pickupGroup.addPackets(1);

        assertThat(pickupGroup.getTotalPacketCount()).isEqualTo(3);
        assertThat(pickupGroup.getRemainingPacketCount()).isEqualTo(3);
    }


    @Test
    void collectPackets_decreasesRemainingPacketCount(){
        Customer customer = new Customer("Maria Bauer", "maria bauer");
        PickupGroup pickupGroup = new PickupGroup(1, customer, 3);

        pickupGroup.collectPackets(1);

        assertThat(pickupGroup.getTotalPacketCount()).isEqualTo(3);
        assertThat(pickupGroup.getRemainingPacketCount()).isEqualTo(2);
        assertThat(pickupGroup.getStatus()).isEqualTo(PickupStatus.OPEN);
        assertThat(pickupGroup.getClosedAt()).isNull();
    }

    @Test
    void collectPackets_closesPickupGroupWhenNoPacketsRemain(){
        Customer customer = new Customer("Maria Bauer", "maria bauer");
        PickupGroup pickupGroup = new PickupGroup(1, customer, 3);

        pickupGroup.collectPackets(3);

        assertThat(pickupGroup.getTotalPacketCount()).isEqualTo(3);
        assertThat(pickupGroup.getStatus()).isEqualTo(PickupStatus.CLOSED);
        assertThat(pickupGroup.getClosedAt()).isNotNull();
        assertThat(pickupGroup.getRemainingPacketCount()).isZero();
    }

    @Test
    void sendBackPackets_decreasesRemainingPacketCount() {
        Customer customer = new Customer("Maria Bauer", "maria bauer");
        PickupGroup pickupGroup = new PickupGroup(1, customer, 3);

        pickupGroup.sendBackPackets(1);

        assertThat(pickupGroup.getTotalPacketCount()).isEqualTo(3);
        assertThat(pickupGroup.getRemainingPacketCount()).isEqualTo(2);
        assertThat(pickupGroup.getStatus()).isEqualTo(PickupStatus.OPEN);
        assertThat(pickupGroup.getClosedAt()).isNull();
    }

    @Test
    void sendBackPackets_closesPickupGroupWhenNoPacketsRemain() {
        Customer customer = new Customer("Maria Bauer", "maria bauer");
        PickupGroup pickupGroup = new PickupGroup(1, customer, 3);

        pickupGroup.sendBackPackets(3);

        assertThat(pickupGroup.getTotalPacketCount()).isEqualTo(3);
        assertThat(pickupGroup.getRemainingPacketCount()).isZero();
        assertThat(pickupGroup.getStatus()).isEqualTo(PickupStatus.CLOSED);
        assertThat(pickupGroup.getClosedAt()).isNotNull();
    }
}
