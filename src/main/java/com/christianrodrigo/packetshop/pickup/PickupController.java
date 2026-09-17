package com.christianrodrigo.packetshop.pickup;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PickupController {

    private final PickupService pickupService;


    public PickupController(PickupService pickupService) {
        this.pickupService = pickupService;
    }

    @GetMapping("/pickups/open")
    public List<PickupResponse> getOpenPickupGroups(){
        return pickupService.getOpenPickupGroups()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/pickups/register")
    public PickupResponse registerIncomingPackets(@RequestBody RegisterPickupRequest request){
        return toResponse(pickupService.registerIncomingPackets(request.customerName(), request.packetCount()));
    }

    @PostMapping("/pickups/{id}/collect")
    public PickupResponse collectPackets(@PathVariable Long id, @RequestBody PacketCountRequest request){
        return toResponse(pickupService.collectPackets(id, request.packetCount()));
    }

    @PostMapping("/pickups/{id}/send-back")
    public PickupResponse sendBackPackets(@PathVariable Long id, @RequestBody PacketCountRequest request){
        return toResponse(pickupService.sendBackPackets(id, request.packetCount()));
    }


    @GetMapping("/pickup/{id}")
    public PickupResponse getPickupGroupbyId(@PathVariable Long id){
        return toResponse(pickupService.getPickupGroupById(id));
    }





    private PickupResponse toResponse(PickupGroup pickupGroup){
        return new PickupResponse(
                pickupGroup.getId(),
                pickupGroup.getPickupNumber(),
                pickupGroup.getCustomer().getName(),
                pickupGroup.getTotalPacketCount(),
                pickupGroup.getRemainingPacketCount(),
                pickupGroup.getStatus());
    }

}
