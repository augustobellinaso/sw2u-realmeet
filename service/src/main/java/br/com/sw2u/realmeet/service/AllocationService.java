package br.com.sw2u.realmeet.service;

import br.com.sw2u.realmeet.api.model.AllocationDTO;
import br.com.sw2u.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.AllocationCannotBeDeletedException;
import br.com.sw2u.realmeet.exception.AllocationNotFoundException;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import br.com.sw2u.realmeet.mapper.AllocationMapper;
import br.com.sw2u.realmeet.util.DateUtils;
import br.com.sw2u.realmeet.validator.AllocationValidator;
import org.springframework.stereotype.Service;

import static br.com.sw2u.realmeet.util.DateUtils.*;

@Service
public class AllocationService {
    
    private final AllocationMapper allocationMapper;
    private final RoomRepository roomRepository;
    private final AllocationRepository allocationRepository;
    private final AllocationValidator allocationValidator;
    
    public AllocationService(AllocationMapper allocationMapper, RoomRepository roomRepository,
            AllocationRepository allocationRepository, AllocationValidator allocationValidator) {
        this.allocationMapper = allocationMapper;
        this.roomRepository = roomRepository;
        this.allocationRepository = allocationRepository;
        this.allocationValidator = allocationValidator;
    }
    
    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {
        var room = roomRepository.findById(createAllocationDTO.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Room #" + createAllocationDTO.getRoomId() + " not found"));
        allocationValidator.validate(createAllocationDTO);
        
        var allocation = allocationMapper.fromCreateAllocationDTOToEntity(createAllocationDTO, room);
        allocationRepository.save(allocation);
        return allocationMapper.fromEntityToAllocationDTO(allocation);
    }
    
    public void deleteAllocation(Long allocationId) {
        var allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new AllocationNotFoundException("Allocation #" + allocationId + " not found"));
        
        if (allocation.getEndAt().isBefore(now())) {
            throw new AllocationCannotBeDeletedException();
        }
        allocationRepository.delete(allocation);
    }
}
