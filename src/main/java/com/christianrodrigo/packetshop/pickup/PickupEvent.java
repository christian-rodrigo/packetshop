package com.christianrodrigo.packetshop.pickup;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_events")
public class PickupEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pickup_group_id", nullable = false)
    private PickupGroup pickupGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private PickupEventType eventType;

    @Column(name = "packet_count", nullable = false)
    private Integer packetCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected PickupEvent(){}

    public PickupEvent(PickupGroup pickupGroup, PickupEventType eventType, Integer packetCount) {
        this.pickupGroup = pickupGroup;
        this.eventType = eventType;
        this.packetCount = packetCount;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public PickupGroup getPickupGroup() {
        return pickupGroup;
    }

    public PickupEventType getEventType() {
        return eventType;
    }

    public Integer getPacketCount() {
        return packetCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
