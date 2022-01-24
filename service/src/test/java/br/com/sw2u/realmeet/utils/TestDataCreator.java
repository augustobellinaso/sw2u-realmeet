package br.com.sw2u.realmeet.utils;

import br.com.sw2u.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.model.Employee;
import br.com.sw2u.realmeet.domain.model.Employee.Builder;

import static br.com.sw2u.realmeet.domain.entity.Allocation.newBuilder;
import static br.com.sw2u.realmeet.utils.TestConstants.*;

public final class TestDataCreator {
    
    private TestDataCreator() {
    }
    
    public static Room.Builder newRoomBuilder() {
        return Room.newBuilder()
                .name(DEFAULT_ROOM_NAME)
                .seats(DEFAULT_ROOM_SEATS);
    }
    
    public static br.com.sw2u.realmeet.domain.entity.Allocation.Builder newAllocationBuilder(Room room) {
        return newBuilder()
                .employee(
                        Employee.newBuilder()
                                .email(DEFAULT_EMPLOYEE_EMAIL)
                                .name(DEFAULT_EMPLOYEE_NAME)
                                .build()
                )
                .subject(DEFAULT_ALLOCATION_SUBJECT)
                .room(room)
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);
    }
    
    public static CreateRoomDTO newCreateRoomDto() {
        return (CreateRoomDTO) new CreateRoomDTO().name(DEFAULT_ROOM_NAME)
                .seats(DEFAULT_ROOM_SEATS);
    }
    
    public static CreateAllocationDTO newCreateAllocationDto() {
        return new CreateAllocationDTO().subject(DEFAULT_ALLOCATION_SUBJECT)
                .roomId(DEFAULT_ROOM_ID)
                .employeeEmail(DEFAULT_EMPLOYEE_EMAIL)
                .employeeName(DEFAULT_EMPLOYEE_NAME)
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);
    }
    
    public static UpdateAllocationDTO newUpdateAllocationDto() {
        return new UpdateAllocationDTO().subject(DEFAULT_ALLOCATION_SUBJECT)
                .startAt(DEFAULT_ALLOCATION_START_AT)
                .endAt(DEFAULT_ALLOCATION_END_AT);
    }
    
    public static Builder newEmployeeBuilder() {
        return Employee.newBuilder()
                .email(DEFAULT_EMPLOYEE_EMAIL)
                .name(DEFAULT_EMPLOYEE_NAME);
    }
}
