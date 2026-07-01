package com.christianrodrigo.packetshop.pickup;

import com.christianrodrigo.packetshop.customer.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_groups")
public class PickupGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pickup_number", nullable = false)
    private Integer pickupNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "total_packet_count", nullable = false)
    private Integer totalPacketCount;

    @Column(name = "remaining_packet_count", nullable = false)
    private Integer remainingPacketCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PickupStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    protected PickupGroup() {
        // Required by JPA
    }

    public PickupGroup(Integer pickupNumber, Customer customer, Integer packetCount) {
        this.pickupNumber = pickupNumber;
        this.customer = customer;
        this.totalPacketCount = packetCount;
        this.remainingPacketCount = packetCount;
        this.status = PickupStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    public void addPackets(int packetCount){
        if(packetCount <= 0){
            throw new IllegalArgumentException("Packet count must be greater than 0");
        }

        if(status != PickupStatus.OPEN){
            throw new IllegalStateException("Cannot add packets to a closed pickup group");
        }

        this.totalPacketCount += packetCount;
        this.remainingPacketCount += packetCount;
    }

    public void collectPackets(int packetCount){
        if(packetCount <= 0){
            throw new IllegalStateException("packetCount must be greater than 0");
        }

        if(status != PickupStatus.OPEN){
            throw new IllegalStateException("Cannot collect packets from a closed pickup group");
        }

        if(packetCount > remainingPacketCount){
            throw new IllegalStateException("Cannot collect more packets than remaining");
        }

        this.remainingPacketCount -= packetCount;

        if(remainingPacketCount == 0){
            this.status = PickupStatus.CLOSED;
            this.closedAt = LocalDateTime.now();
        }
    }

    public void sendBackPackets(int packetCount) {
        if (packetCount <= 0) {
            throw new IllegalArgumentException("Packet count must be greater than 0");
        }

        if (status != PickupStatus.OPEN) {
            throw new IllegalStateException("Cannot send back packets from a closed pickup group");
        }

        if (packetCount > remainingPacketCount) {
            throw new IllegalArgumentException("Cannot send back more packets than remaining");
        }

        this.remainingPacketCount -= packetCount;

        if (this.remainingPacketCount == 0) {
            this.status = PickupStatus.CLOSED;
            this.closedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Integer getPickupNumber() {
        return pickupNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Integer getTotalPacketCount() {
        return totalPacketCount;
    }

    public Integer getRemainingPacketCount() {
        return remainingPacketCount;
    }

    public PickupStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }
}