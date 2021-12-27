package br.com.sw2u.realmeet.integration;

import br.com.sw2u.realmeet.api.facade.AllocationApi;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import static br.com.sw2u.realmeet.util.DateUtils.now;
import static br.com.sw2u.realmeet.utils.TestDataCreator.*;
import static org.junit.jupiter.api.Assertions.*;

class AllocationApiIntegrationTest extends BaseIntegrationTest {
    
    @Autowired
    private AllocationApi api;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private AllocationRepository allocationRepository;
    
    @Override
    protected void setupEach() throws Exception {
        setLocalhostBasePath(api.getApiClient(), "/v1");
    }
    
    @Test
    void testCreateRoomWithSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDto().roomId(room.getId());
        var allocationDTO = api.createAllocation(createAllocationDTO);
        
        assertEquals(room.getId(), allocationDTO.getRoomId());
        assertNotNull(allocationDTO.getId());
        assertEquals(createAllocationDTO.getSubject(), allocationDTO.getSubject());
        assertEquals(createAllocationDTO.getEmployeeName(), allocationDTO.getEmployeeName());
        assertEquals(createAllocationDTO.getEmployeeEmail(), allocationDTO.getEmployeeEmail());
        assertTrue(createAllocationDTO.getStartAt().isEqual(allocationDTO.getStartAt()));
        assertTrue(createAllocationDTO.getEndAt().isEqual(allocationDTO.getEndAt()));
    }
    
    @Test
    void testCreateAllocationValidationError() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDto().roomId(room.getId()).subject(null);
        
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> api.createAllocation(createAllocationDTO));
    }
    
    @Test
    void testCreateAllocationValidationErrorWhenRoomNotFound() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.createAllocation(newCreateAllocationDto()));
    }
    
    @Test
    void testDeleteAllocationSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(newAllocationBuilder(room).build());
        
        api.deleteAllocation(allocation.getId());
        assertFalse(allocationRepository.findById(allocation.getId()).isPresent());
    }
    
    @Test
    void testDeleteAllocationInThePast() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(
                newAllocationBuilder(room)
                        .startAt(now().minusDays(1L))
                        .endAt(now().minusDays(1L).plusHours(1L))
                        .build());
        
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> api.deleteAllocation(allocation.getId()));
    }
    
    @Test
    void testDeleteAllocationDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.deleteAllocation(1L));
    }
}