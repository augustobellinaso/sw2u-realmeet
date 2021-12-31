package br.com.sw2u.realmeet.integration;

import br.com.sw2u.realmeet.api.facade.AllocationApi;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.entity.Allocation;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.service.AllocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private AllocationService allocationService;
    
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
        
        var allocation1 = allocationRepository.saveAndFlush(
                newAllocationBuilder(room).startAt(baseStartAt.plusHours(1)).endAt(baseStartAt.plusHours(2)).build());
        var allocation2 = allocationRepository.saveAndFlush(
                newAllocationBuilder(room).startAt(baseStartAt.plusHours(4)).endAt(baseStartAt.plusHours(5)).build());
        var allocation3 =
                allocationRepository.saveAndFlush(newAllocationBuilder(room).startAt(baseEndAt.plusHours(5)).endAt(baseEndAt.plusHours(6)).build());
        
        var allocationsDTOList = api.listAllocations(null, null, baseStartAt.toLocalDate(), baseEndAt.toLocalDate(), null, null, null);
        
        assertEquals(2, allocationsDTOList.size());
        assertEquals(allocation1.getId(), allocationsDTOList.get(0).getId());
        assertEquals(allocation2.getId(), allocationsDTOList.get(1).getId());
    }
    
    @Test
    void testFilterAllocationUsingPagination() {
        persistAllocations(15);
        ReflectionTestUtils.setField(allocationService, "maxLimit", 10);
        
        var allocationDTOListPage1 = api.listAllocations(null, null, null, null, null, null, 0);
        assertEquals(10, allocationDTOListPage1.size());
    
        var allocationDTOListPage2 = api.listAllocations(null, null, null, null, null, null, 1);
        assertEquals(5, allocationDTOListPage2.size());
    }
    
    @Test
    void testFilterAllocationUsingPaginationAndLimit() {
        persistAllocations(25);
        ReflectionTestUtils.setField(allocationService, "maxLimit", 50);
        
        var allocationDTOListPage1 = api.listAllocations(null, null, null, null, null, 10, 0);
        assertEquals(10, allocationDTOListPage1.size());
        
        var allocationDTOListPage2 = api.listAllocations(null, null, null, null, null, 10, 1);
        assertEquals(10, allocationDTOListPage2.size());
    
        var allocationDTOListPage3 = api.listAllocations(null, null, null, null, null, 10, 2);
        assertEquals(5, allocationDTOListPage3.size());
    }
    
    @Test
    void testFilterAllocationOrderByStartAtDesc() {
        var allocationList = persistAllocations(3);
        
        var allocationDTOList = api.listAllocations(null, null, null, null, "-startAt", null, null);
        assertEquals(3, allocationDTOList.size());
        assertEquals(allocationList.get(2).getId(), allocationDTOList.get(0).getId());
        assertEquals(allocationList.get(1).getId(), allocationDTOList.get(1).getId());
        assertEquals(allocationList.get(0).getId(), allocationDTOList.get(2).getId());
    }
    
    @Test
    void testFilterAllocationOrderByEndAtAsc() {
        var allocationList = persistAllocations(3);
        
        var allocationDTOList = api.listAllocations(null, null, null, null, "endAt", null, null);
        assertEquals(3, allocationDTOList.size());
        assertEquals(allocationList.get(0).getId(), allocationDTOList.get(0).getId());
        assertEquals(allocationList.get(1).getId(), allocationDTOList.get(1).getId());
        assertEquals(allocationList.get(2).getId(), allocationDTOList.get(2).getId());
    }
    
    @Test
    void testFilterAllocationOrderByInvalidField() {
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> api.listAllocations(null, null, null, null, "invalid", null, null));
    }
    
    private List<Allocation> persistAllocations(int numberOfAllocations) {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        return IntStream.range(0, numberOfAllocations)
                .mapToObj(i -> allocationRepository.saveAndFlush(newAllocationBuilder(room)
                                                                         .subject(DEFAULT_ALLOCATION_SUBJECT + "_" + (i + 1))
                                                                         .startAt(DEFAULT_ALLOCATION_START_AT.plusHours(i + 1))
                                                                         .endAt(DEFAULT_ALLOCATION_END_AT.plusHours(i + 1))
                                                                         .build()))
                .collect(Collectors.toList());
    }
    
}
