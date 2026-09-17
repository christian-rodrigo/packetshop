package com.christianrodrigo.packetshop.pickup;

public record RegisterPickupRequest(
        String customerName,
        int packetCount
){
}
