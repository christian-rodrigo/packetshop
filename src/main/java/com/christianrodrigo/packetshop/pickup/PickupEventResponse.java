package com.christianrodrigo.packetshop.pickup;

import java.time.LocalDateTime;

public record PickupEventResponse(
        Long id,
        PickupEventType eventType,
        Integer packetCount,
        LocalDateTime createdAt
) {
}
