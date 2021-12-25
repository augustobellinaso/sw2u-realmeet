package br.com.sw2u.realmeet.service;

import br.com.sw2u.realmeet.api.model.AllocationDTO;
import br.com.sw2u.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.mapper.AllocationMapper;
import org.springframework.stereotype.Service;

@Service
public class AllocationService {

    private final AllocationMapper allocationMapper;
    private final RoomRepository roomRepository;
    private final AllocationRepository allocationRepository;
    
    public AllocationService(AllocationMapper allocationMapper, RoomRepository roomRepository,
            AllocationRepository allocationRepository) {
        this.allocationMapper = allocationMapper;
        this.roomRepository = roomRepository;
        this.allocationRepository = allocationRepository;
    }
    
    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {
        return null;
    }
}
