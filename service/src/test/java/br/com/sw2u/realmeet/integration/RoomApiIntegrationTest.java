package br.com.sw2u.realmeet.integration;

import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newCreateRoomDto;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2u.realmeet.api.facade.RoomApi;
import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2u.realmeet.core.BaseIntegrationTest;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.Test;

class RoomApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RoomApi roomApi;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalhostBasePath(roomApi.getApiClient(), "/v1");
    }

    @Test
    void testGetRoomByIdSuccess() {
        var room = newRoomBuilder().build();
        roomRepository.saveAndFlush(room);
        assertNotNull(room.getId());
        assertTrue(room.getActive());
        var dto = roomApi.getRoom(room.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
        assertEquals(room.getId(), dto.getId());
    }

    @Test
    void testGetRoomInactive() {
        var room = newRoomBuilder().active(false)
                                   .build();
        roomRepository.saveAndFlush(room);
        assertFalse(room.getActive());
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.getRoom(room.getId()));
    }

    @Test
    void testRoomNotExists() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.getRoom(DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateRoomWithSuccess() {
        var createRoomDto = newCreateRoomDto();
        var roomDto = roomApi.createRoom(createRoomDto);
        assertEquals(createRoomDto.getName(), roomDto.getName());
        assertEquals(createRoomDto.getSeats(), roomDto.getSeats());
        assertNotNull(roomDto.getId());
        var room = roomRepository.findById(roomDto.getId())
                                 .orElseThrow();
        assertEquals(roomDto.getName(), room.getName());
        assertEquals(roomDto.getSeats(), room.getSeats());
    }

    @Test
    void testCreateRoomValidationError() {
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> roomApi.createRoom((CreateRoomDTO) newCreateRoomDto().name(null)));
    }

    @Test
    void testDeleteRoomSuccess() {
        var roomId = roomRepository.saveAndFlush(newRoomBuilder().build())
                                   .getId();
        roomApi.deleteRoom(roomId);

        assertFalse(roomRepository.findById(roomId)
                                  .orElseThrow()
                                  .getActive());
    }

    @Test
    void testDeleteRoomDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.deleteRoom(1L));
    }

    @Test
    void testUpdateRoomSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var updateRoomDTO = new UpdateRoomDTO().name(room.getName() + "_")
                                               .seats(room.getSeats() + 1);

        roomApi.updateRoom(room.getId(), updateRoomDTO);

        var udpatedRoom = roomRepository.findById(room.getId())
                                        .orElseThrow();
        assertEquals(updateRoomDTO.getName(), udpatedRoom.getName());
        assertEquals(updateRoomDTO.getSeats(), udpatedRoom.getSeats());
    }

    @Test
    void testUpdateRoomDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> roomApi.updateRoom(1L, new UpdateRoomDTO().name("Room").seats(10)));
    }

    @Test
    void testUpdateRoomValidationError() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () -> roomApi.updateRoom(room.getId(), new UpdateRoomDTO().name(null).seats(10)));
    }
}
