package br.com.sw2u.realmeet.unit;

import static br.com.sw2u.realmeet.utils.MapperUtils.roomMapper;
import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newCreateRoomDto;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import br.com.sw2u.realmeet.service.RoomService;
import br.com.sw2u.realmeet.validator.RoomValidator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class RoomServiceUnitTest extends BaseUnitTest {
    private RoomService victim;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomValidator roomValidator;

    @BeforeEach
    void setupEach() {
        victim = new RoomService(
                roomRepository,
                roomMapper(),
                roomValidator
        );
    }

    @Test
    void testGetRoomSuccess() {
        var room = newRoomBuilder()
                .id(DEFAULT_ROOM_ID)
                .build();
        when(roomRepository.findByIdAndActive(
                DEFAULT_ROOM_ID,
                true
        )).thenReturn(Optional.of(room));
        var dto = victim.getRoom(DEFAULT_ROOM_ID);
        assertEquals(
                room.getName(),
                dto.getName()
        );
        assertEquals(
                room.getSeats(),
                dto.getSeats()
        );
        assertEquals(
                room.getId(),
                dto.getId()
        );
    }

    @Test
    void testGetRoomNotFound() {
        when(roomRepository.findByIdAndActive(
                DEFAULT_ROOM_ID,
                true
        )).thenReturn(Optional.empty());
        assertThrows(
                RoomNotFoundException.class,
                () -> victim.getRoom(DEFAULT_ROOM_ID)
        );
    }

    @Test
    void testCreateRoomWithSuccess() {
        var createRoomDto = newCreateRoomDto();
        var roomDTO = victim.createRoom(createRoomDto);

        assertEquals(
                createRoomDto.getName(),
                roomDTO.getName()
        );
        assertEquals(
                createRoomDto.getSeats(),
                roomDTO.getSeats()
        );

        verify(roomRepository).save(any());
    }
}
