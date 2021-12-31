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
import br.com.sw2u.realmeet.util.PageUtils;
import br.com.sw2u.realmeet.validator.AllocationValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.com.sw2u.realmeet.domain.entity.Allocation.SORTABLE_FIELDS;
import static br.com.sw2u.realmeet.util.Constants.ALLOCATION_MAX_FILTER_LIMIT;
import static br.com.sw2u.realmeet.util.DateUtils.DEFAULT_TIME_ZONE;
import static br.com.sw2u.realmeet.util.DateUtils.now;
import static java.util.Objects.isNull;

@Service
public class AllocationService {
    
    private final AllocationMapper allocationMapper;
    private final RoomRepository roomRepository;
    private final AllocationRepository allocationRepository;
    private final AllocationValidator allocationValidator;
    private final int maxLimit;
    
    public AllocationService(
            AllocationMapper allocationMapper,
            RoomRepository roomRepository,
            AllocationRepository allocationRepository,
            AllocationValidator allocationValidator,
            @Value(ALLOCATION_MAX_FILTER_LIMIT) int maxLimit) {
        this.allocationMapper = allocationMapper;
        this.roomRepository = roomRepository;
        this.allocationRepository = allocationRepository;
        this.allocationValidator = allocationValidator;
        this.maxLimit = maxLimit;
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
    public void updateAllocation(
            Long allocationId,
            UpdateAllocationDTO updateAllocationDTO) {
        var allocation = getAllocationOrThrow(allocationId);
        
        allocationValidator.validate(allocationId, updateAllocationDTO);
        
        if (isAllocationInThePast(allocation)) {
            throw new AllocationCannotBeUpdatedException();
        }
        
        allocationRepository.updateAllocation(allocationId, updateAllocationDTO.getSubject(), updateAllocationDTO.getStartAt(),
                                              updateAllocationDTO.getEndAt());
    }
    
    public List<AllocationDTO> listAllocations(
            String employeeEmail, Long roomId, LocalDate startAt, LocalDate endAt, String orderBy, Integer limit, Integer page) {
        
        Pageable pageable = PageUtils.newPageable(page, limit, maxLimit, orderBy, SORTABLE_FIELDS);
        
        var allocations = allocationRepository.findAllAllocationsWithFilters(
                employeeEmail,
                roomId,
                isNull(startAt) ? null : startAt.atTime(LocalTime.MIN).atOffset(DEFAULT_TIME_ZONE),
                isNull(endAt) ? null : endAt.atTime(LocalTime.MAX).atOffset(DEFAULT_TIME_ZONE),
                pageable);
        
        return allocations.stream().map(allocationMapper::fromEntityToAllocationDTO).collect(Collectors.toList());
    }
    
    private Allocation getAllocationOrThrow(Long allocationId) {
        return allocationRepository.findById(allocationId)
                .orElseThrow(() -> new AllocationNotFoundException("Allocation #" + allocationId + " not found"));
    }
    
    private boolean isAllocationInThePast(Allocation allocation) {
        return allocation.getEndAt().isBefore(now());
    }
}
