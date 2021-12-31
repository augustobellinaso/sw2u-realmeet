package br.com.sw2u.realmeet.integration;

import br.com.sw2u.realmeet.api.facade.AllocationApi;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import static br.com.sw2u.realmeet.util.DateUtils.now;
import static br.com.sw2u.realmeet.utils.TestConstants.*;
import static br.com.sw2u.realmeet.utils.TestDataCreator.*;
import static org.junit.jupiter.api.Assertions.*;

class AllocationApiFilterIntegrationTest extends BaseIntegrationTest {
    
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
    void testFilterAllAllocations() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation1 = allocationRepository.saveAndFlush(newAllocationBuilder(room).subject(DEFAULT_ALLOCATION_SUBJECT + "1").build());
        var allocation2 = allocationRepository.saveAndFlush(newAllocationBuilder(room).subject(DEFAULT_ALLOCATION_SUBJECT + "2").build());
        var allocation3 = allocationRepository.saveAndFlush(newAllocationBuilder(room).subject(DEFAULT_ALLOCATION_SUBJECT + "3").build());
        
        var allocationsDTOList = api.listAllocations(null, null, null, null, null, null, null);
        
        assertEquals(3, allocationsDTOList.size());
        assertEquals(allocation1.getSubject(), allocationsDTOList.get(0).getSubject());
        assertEquals(allocation2.getSubject(), allocationsDTOList.get(1).getSubject());
        assertEquals(allocation3.getSubject(), allocationsDTOList.get(2).getSubject());
    }
    
    @Test
    void testFilterAllocationByRoomId() {
        var roomA = roomRepository.saveAndFlush(newRoomBuilder().name(DEFAULT_ROOM_NAME + "1").build());
        var roomB = roomRepository.saveAndFlush(newRoomBuilder().name(DEFAULT_ROOM_NAME + "2").build());
    
        var allocation1 = allocationRepository.saveAndFlush(newAllocationBuilder(roomA).build());
        var allocation2 = allocationRepository.saveAndFlush(newAllocationBuilder(roomA).build());
        allocationRepository.saveAndFlush(newAllocationBuilder(roomB).build());
    
        var allocationsDTOList = api.listAllocations(null, roomA.getId(), null, null, null, null, null);
    
        assertEquals(2, allocationsDTOList.size());
        assertEquals(allocation1.getId(), allocationsDTOList.get(0).getId());
        assertEquals(allocation2.getId(), allocationsDTOList.get(1).getId());
    }
    
    @Test
    void testFilterAllocationByEmployeeEmail() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        
        var employee1 = newEmployeeBuilder().email(DEFAULT_EMPLOYEE_EMAIL + "1").build();
        var employee2 = newEmployeeBuilder().email(DEFAULT_EMPLOYEE_EMAIL + "2").build();
        
        var allocation1 = allocationRepository.saveAndFlush(newAllocationBuilder(room).employee(employee1).build());
        var allocation2 = allocationRepository.saveAndFlush(newAllocationBuilder(room).employee(employee1).build());
        allocationRepository.saveAndFlush(newAllocationBuilder(room).employee(employee2).build());
        
        var allocationsDTOList = api.listAllocations(employee1.getEmail(), null, null, null, null, null, null);
        
        assertEquals(2, allocationsDTOList.size());
        assertEquals(allocation1.getEmployee().getEmail(), allocationsDTOList.get(0).getEmployeeEmail());
        assertEquals(allocation2.getEmployee().getEmail(), allocationsDTOList.get(1).getEmployeeEmail());
    }
    
    @Test
    void testFilterAllocationByDateRange() {
        var baseStartAt = now().plusDays(2).withHour(14).withMinute(0);
        var baseEndAt = now().plusDays(4).withHour(20).withMinute(0);
        
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        
        var allocation1 = allocationRepository.saveAndFlush(newAllocationBuilder(room).startAt(baseStartAt.plusHours(1)).endAt(baseStartAt.plusHours(2)).build());
        var allocation2 = allocationRepository.saveAndFlush(newAllocationBuilder(room).startAt(baseStartAt.plusHours(4)).endAt(baseStartAt.plusHours(5)).build());
        var allocation3 = allocationRepository.saveAndFlush(newAllocationBuilder(room).startAt(baseEndAt.plusHours(5)).endAt(baseEndAt.plusHours(6)).build());
        
        var allocationsDTOList = api.listAllocations(null, null, baseStartAt.toLocalDate(), baseEndAt.toLocalDate(), null, null, null);
        
        assertEquals(2, allocationsDTOList.size());
        assertEquals(allocation1.getId(), allocationsDTOList.get(0).getId());
        assertEquals(allocation2.getId(), allocationsDTOList.get(1).getId());
    }
    
}
