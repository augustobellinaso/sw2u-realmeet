package br.com.sw2u.realmeet.unit;

import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.mapper.AllocationMapper;
import br.com.sw2u.realmeet.mapper.RoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static br.com.sw2u.realmeet.utils.MapperUtils.allocationMapper;
import static br.com.sw2u.realmeet.utils.MapperUtils.roomMapper;
import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2u.realmeet.utils.TestDataCreator.*;
import static org.junit.jupiter.api.Assertions.*;

class AllocationMapperUnitTest extends BaseUnitTest {

    private AllocationMapper victim;

    @BeforeEach
    void setupEach() {
        victim = allocationMapper();
    }

    @Test
    void testFromCreateAllocationDTOToEntity() {
        var createAllocationDto = newCreateAllocationDto();
        var allocation = victim.fromCreateAllocationDTOToEntity(createAllocationDto, newRoomBuilder().build());
        
        assertNull(allocation.getId());
        assertEquals(createAllocationDto.getStartAt(), allocation.getStartAt());
        assertEquals(createAllocationDto.getEndAt(), allocation.getEndAt());
        assertEquals(createAllocationDto.getSubject(), allocation.getSubject());
        assertEquals(createAllocationDto.getEmployeeEmail(), allocation.getEmployee().getEmail());
        assertEquals(createAllocationDto.getEmployeeName(), allocation.getEmployee().getName());
    }
    
    @Test
    void testFromEntityToAllocationDTO() {
        var allocation = newAllocationBuilder(newRoomBuilder().id(1L).build()).build();
        var allocationDto = victim.fromEntityToAllocationDTO(allocation);
        
        assertEquals(allocation.getId(), allocationDto.getId());
        assertEquals(allocation.getRoom().getId(), allocationDto.getRoomId());
        assertNotNull(allocationDto.getRoomId());
        assertEquals(allocation.getStartAt(), allocationDto.getStartAt());
        assertEquals(allocation.getEndAt(), allocationDto.getEndAt());
        assertEquals(allocation.getSubject(), allocationDto.getSubject());
        assertEquals(allocation.getEmployee().getEmail(), allocationDto.getEmployeeEmail());
        assertEquals(allocation.getEmployee().getName(), allocationDto.getEmployeeName());
    }
}

