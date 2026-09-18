package com.christianrodrigo.packetshop.pickup;

import com.christianrodrigo.packetshop.customer.Customer;
import com.christianrodrigo.packetshop.customer.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PickupService {

    private final CustomerService customerService;
    private final PickupGroupRepository pickupGroupRepository;
    private final PickupEventRepository pickupEventRepository;

    public PickupService(CustomerService customerService, PickupGroupRepository pickupGroupRepository, PickupEventRepository pickupEventRepository) {
        this.customerService = customerService;
        this.pickupGroupRepository = pickupGroupRepository;
        this.pickupEventRepository = pickupEventRepository;
    }

    private int findNextAvailablePickupNumber(){

        Set<Integer> usedNumbers = pickupGroupRepository.findByStatus(PickupStatus.OPEN)
                .stream()
                .map(PickupGroup::getPickupNumber)
                .collect(Collectors.toSet());

        int nextNumber = 1;

        while(usedNumbers.contains(nextNumber)){
            nextNumber++;
        }

        return nextNumber;
    }

    public PickupGroup registerIncomingPackets(String customerName, int packetCount){

        if(packetCount <= 0){
            throw new IllegalArgumentException("Packet count must be greater than 0");
        }

        Customer customer = customerService.getOrCreateCustomer(customerName);

        Optional<PickupGroup> existingPickupGroup = pickupGroupRepository.findByCustomerAndStatus(customer, PickupStatus.OPEN);

        PickupGroup pickupGroup;

        if(existingPickupGroup.isPresent()){
            pickupGroup = existingPickupGroup.get();
            pickupGroup.addPackets(packetCount);
        }else{
            pickupGroup = pickupGroupRepository.save(new PickupGroup(findNextAvailablePickupNumber(), customer, packetCount));
        }

        pickupEventRepository.save(new PickupEvent(pickupGroup, PickupEventType.RECEIVED, packetCount));

        return pickupGroup;
    }

    public PickupGroup collectPackets(Long pickupGroupId, int packetCount) {


        if (packetCount <= 0) {
            throw new IllegalArgumentException("Packet count must be greater than 0");
        }

        PickupGroup pickupGroup = pickupGroupRepository.findById(pickupGroupId)
                .orElseThrow(() -> new RuntimeException("Pickup Group not found"));

        pickupGroup.collectPackets(packetCount);

        pickupEventRepository.save(
                new PickupEvent(pickupGroup, PickupEventType.COLLECTED, packetCount)
        );

        return pickupGroup;

    }

    public PickupGroup sendBackPackets(Long pickupId, int packetCount){

        PickupGroup pickupGroup = pickupGroupRepository.findById(pickupId)
                .orElseThrow(()-> new RuntimeException("Pickup Group not found"));

        pickupGroup.sendBackPackets(packetCount);

        pickupEventRepository.save(new PickupEvent(pickupGroup,PickupEventType.SENT_BACK, packetCount));

        return pickupGroup;
    }

    public List<PickupGroup> getOpenPickupGroups(){

        return pickupGroupRepository.findByStatusOrderByPickupNumberAsc(PickupStatus.OPEN);
    }

    public PickupGroup getPickupGroupById(Long id) {
        return pickupGroupRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Pickup Group not found"));
    }

    public List<PickupEvent> getEventsForPickupGroup(Long pickupGroupid){
        PickupGroup pickupGroup = pickupGroupRepository.findById(pickupGroupid)
                .orElseThrow(()-> new RuntimeException("Pickup Group not found"));

        return pickupEventRepository.findByPickupGroupOrderByIdAsc(pickupGroup);
    }
}
