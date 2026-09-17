package com.christianrodrigo.packetshop.pickup;

public record PickupResponse(
        Long id,
        Integer pickupNumber,
        String customerName,
        Integer totalPacketCount,
        Integer remainingPacketCount,
        PickupStatus status
) {
}
