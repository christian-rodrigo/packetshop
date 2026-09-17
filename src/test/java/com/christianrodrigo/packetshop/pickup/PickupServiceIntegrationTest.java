package com.christianrodrigo.packetshop.pickup;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PickupServiceIntegrationTest {

    @Autowired
    private PickupService pickupService;

    @Autowired
    private PickupEventRepository pickupEventRepository;

    @Test
    void registerIncomingPackets_addsPacketsToExistingOpenPickupGroupAndCreatesSecondEvent() {
        PickupGroup firstpickupGroup = pickupService.registerIncomingPackets("Maria Bauer", 2);

        PickupGroup secondPickupGroup = pickupService.registerIncomingPackets("   Maria   Bauer   ", 1);

        assertThat(firstpickupGroup.getId()).isEqualTo(secondPickupGroup.getId());
        assertThat(firstpickupGroup.getPickupNumber()).isEqualTo(secondPickupGroup.getPickupNumber());
        assertThat(secondPickupGroup.getRemainingPacketCount()).isEqualTo(3);
        assertThat(secondPickupGroup.getTotalPacketCount()).isEqualTo(3);
        assertThat(secondPickupGroup.getStatus()).isEqualTo(PickupStatus.OPEN);

        List<PickupEvent> events =
                pickupEventRepository.findByPickupGroup(secondPickupGroup);

        assertThat(events).hasSize(2);
        assertThat(events)
                .extracting(PickupEvent::getEventType)
                .containsOnly(PickupEventType.RECEIVED);

        assertThat(events)
                .extracting(PickupEvent::getPacketCount)
                .containsExactly(2, 1);
    }

    @Test
    void registerIncomingPackets_createsNewPickupGroupAndReceivedEvent() {
        PickupGroup pickupGroup = pickupService.registerIncomingPackets("Maria Bauer", 2);

        assertThat(pickupGroup.getId()).isNotNull();
        assertThat(pickupGroup.getCustomer().getName()).isEqualTo("Maria Bauer");
        assertThat(pickupGroup.getRemainingPacketCount()).isEqualTo(2);
        assertThat(pickupGroup.getTotalPacketCount()).isEqualTo(2);
        assertThat(pickupGroup.getStatus()).isEqualTo(PickupStatus.OPEN);

        List<PickupEvent> events = pickupEventRepository.findByPickupGroup(pickupGroup);

        assertThat(events).hasSize(1);

        PickupEvent event = events.get(0);

        assertThat(event.getEventType()).isEqualTo(PickupEventType.RECEIVED);
        assertThat(event.getPacketCount()).isEqualTo(2);
    }

    @Test
    void collectPackets_decreasesRemainingPacketCountAndCreatesCollectedEvent() {

        PickupGroup pickupGroup = pickupService.registerIncomingPackets("Maria Bauer", 3);

        Long pickupId = pickupGroup.getId();

        PickupGroup updatedPickupGroup = pickupService.collectPackets(pickupId, 1);

        assertThat(updatedPickupGroup.getId()).isEqualTo(pickupId);
        assertThat(updatedPickupGroup.getTotalPacketCount()).isEqualTo(3);
        assertThat(updatedPickupGroup.getRemainingPacketCount()).isEqualTo(2);
        assertThat(updatedPickupGroup.getStatus()).isEqualTo(PickupStatus.OPEN);
        assertThat(updatedPickupGroup.getClosedAt()).isNull();

        List<PickupEvent> events = pickupEventRepository.findByPickupGroupOrderByIdAsc(updatedPickupGroup);

        assertThat(events).hasSize(2);

        assertThat(events)
                .extracting(PickupEvent::getEventType)
                .containsExactly(
                        PickupEventType.RECEIVED,
                        PickupEventType.COLLECTED
                );

        assertThat(events)
                .extracting(PickupEvent::getPacketCount)
                .containsExactly(3, 1);

    }

    @Test
    void collectPackets_closesGroupWhenRemainingIsZero() {

        PickupGroup pickupGroup = pickupService.registerIncomingPackets("Christian Rodrigo", 5);

        Long pickupId = pickupGroup.getId();

        PickupGroup updatedPickupGroup = pickupService.collectPackets(pickupId, 5);

        assertThat(updatedPickupGroup.getId()).isEqualTo(pickupId);
        assertThat(updatedPickupGroup.getRemainingPacketCount()).isEqualTo(0);
        assertThat(updatedPickupGroup.getTotalPacketCount()).isEqualTo(5);
        assertThat(updatedPickupGroup.getStatus()).isEqualTo(PickupStatus.CLOSED);
        assertThat(updatedPickupGroup.getClosedAt()).isNotNull();

        List<PickupEvent> events = pickupEventRepository.findByPickupGroupOrderByIdAsc(updatedPickupGroup);

        assertThat(events).hasSize(2);

        assertThat(events).extracting(PickupEvent::getEventType)
                .containsExactly(PickupEventType.RECEIVED, PickupEventType.COLLECTED);

        assertThat(events).extracting(PickupEvent::getPacketCount)
                .containsExactly(5, 5);

    }

    @Test
    void sendBackPackets_decreasesRemainingPacketCountAndCreatesSentBackEvent(){

        PickupGroup pickupGroup = pickupService.registerIncomingPackets("Christian Rodrigo", 5);

        PickupGroup updatedPickupGroup = pickupService.sendBackPackets(pickupGroup.getId(), 1);

        assertThat(updatedPickupGroup.getId()).isEqualTo(pickupGroup.getId());
        assertThat(updatedPickupGroup.getTotalPacketCount()).isEqualTo(5);
        assertThat(updatedPickupGroup.getRemainingPacketCount()).isEqualTo(4);
        assertThat(updatedPickupGroup.getStatus()).isEqualTo(PickupStatus.OPEN);
        assertThat(updatedPickupGroup.getClosedAt()).isNull();

        List<PickupEvent> events = pickupEventRepository.findByPickupGroupOrderByIdAsc(updatedPickupGroup);

        assertThat(events).hasSize(2);

        assertThat(events)
                .extracting(PickupEvent::getPacketCount)
                .containsExactly(5,1);

        assertThat(events)
                .extracting(PickupEvent::getEventType)
                .containsExactly(PickupEventType.RECEIVED, PickupEventType.SENT_BACK);


    }
}
