package br.com.sw2u.realmeet.service;

import br.com.sw2u.realmeet.api.model.AllocationDTO;
import br.com.sw2u.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2u.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2u.realmeet.domain.entity.Allocation;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.AllocationCannotBeDeletedException;
import br.com.sw2u.realmeet.exception.AllocationCannotBeUpdatedException;
import br.com.sw2u.realmeet.exception.AllocationNotFoundException;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import br.com.sw2u.realmeet.mapper.AllocationMapper;
import br.com.sw2u.realmeet.validator.AllocationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.sw2u.realmeet.util.DateUtils.now;

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
        var allocation = getAllocationOrThrow(allocationId);
        
        if (isAllocationInThePast(allocation)) {
            throw new AllocationCannotBeDeletedException();
        }
        allocationRepository.delete(allocation);
    }
    
    @Transactional
    public void updateAllocation(Long allocationId, UpdateAllocationDTO updateAllocationDTO) {
        var allocation = getAllocationOrThrow(allocationId);
        
        allocationValidator.validate(allocationId, updateAllocationDTO);
        
        if (isAllocationInThePast(allocation)) {
            throw new AllocationCannotBeUpdatedException();
        }
        
        allocationRepository.updateAllocation(allocationId, updateAllocationDTO.getSubject(), updateAllocationDTO.getStartAt(),
                                              updateAllocationDTO.getEndAt());
    }
    
    private Allocation getAllocationOrThrow(Long allocationId) {
        return allocationRepository.findById(allocationId)
                .orElseThrow(() -> new AllocationNotFoundException("Allocation #" + allocationId + " not found"));
    }
    
    private boolean isAllocationInThePast(Allocation allocation) {
        return allocation.getEndAt().isBefore(now());
    }
}
