package br.com.sw2u.realmeet.integration;

import br.com.sw2u.realmeet.api.facade.AllocationApi;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.email.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.HttpClientErrorException;

import static br.com.sw2u.realmeet.util.DateUtils.now;
import static br.com.sw2u.realmeet.utils.TestConstants.*;
import static br.com.sw2u.realmeet.utils.TestDataCreator.*;
import static org.junit.jupiter.api.Assertions.*;

class AllocationApiIntegrationTest extends BaseIntegrationTest {
    
    @Autowired
    private AllocationApi api;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private AllocationRepository allocationRepository;
    
    @MockBean
    private EmailSender emailSender;
    
    @Override
    protected void setupEach() throws Exception {
        setLocalhostBasePath(api.getApiClient(), "/v1");
    }
    
    @Test
    void testCreateAllocationWithSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDto().roomId(room.getId());
        var allocationDTO = api.createAllocation(TEST_CLIENT_API_KEY, createAllocationDTO);
        
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
        
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> api.createAllocation(TEST_CLIENT_API_KEY, createAllocationDTO));
    }
    
    @Test
    void testCreateAllocationValidationErrorWhenRoomNotFound() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.createAllocation(TEST_CLIENT_API_KEY, newCreateAllocationDto()));
    }
    
    @Test
    void testDeleteAllocationSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(newAllocationBuilder(room).build());
        
        api.deleteAllocation(TEST_CLIENT_API_KEY, allocation.getId());
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
        
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> api.deleteAllocation(TEST_CLIENT_API_KEY, allocation.getId()));
    }
    
    @Test
    void testDeleteAllocationDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.deleteAllocation(TEST_CLIENT_API_KEY, 1L));
    }
    
    @Test
    void testUpdateAllocationSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDto().roomId(room.getId());
        var allocationDTO = api.createAllocation(TEST_CLIENT_API_KEY, createAllocationDTO);
        
        var updateAllocationDTO = newUpdateAllocationDto().subject(DEFAULT_ALLOCATION_SUBJECT + "_").startAt(DEFAULT_ALLOCATION_START_AT.plusDays(1))
                .endAt(DEFAULT_ALLOCATION_START_AT.plusDays(1).plusHours(1));
        
        api.updateAllocation(TEST_CLIENT_API_KEY, allocationDTO.getId(), updateAllocationDTO);
        
        var allocation = allocationRepository.findById(allocationDTO.getId()).orElseThrow();
        
        assertEquals(updateAllocationDTO.getSubject(), allocation.getSubject());
        assertTrue(updateAllocationDTO.getStartAt().isEqual(allocation.getStartAt()));
        assertTrue(updateAllocationDTO.getEndAt().isEqual(allocation.getEndAt()));
    }
    
    @Test
    void testUpdateAllocationDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.updateAllocation(TEST_CLIENT_API_KEY, 1L, newUpdateAllocationDto()));
    }
    
    @Test
    void testUpdateAllocationValidationError() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDto().roomId(room.getId());
        var allocationDTO = api.createAllocation(TEST_CLIENT_API_KEY, createAllocationDTO);
        assertThrows(HttpClientErrorException.UnprocessableEntity.class,
                     () -> api.updateAllocation(TEST_CLIENT_API_KEY, allocationDTO.getId(), newUpdateAllocationDto().subject(null)));
    }
}
