package br.com.sw2u.realmeet.utils;

import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ROOM_SEATS;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.domain.entity.Room;
import br.com.sw2u.realmeet.domain.entity.Room.RoomBuilder;

public final class TestDataCreator {

    private TestDataCreator() {
    }

    public static RoomBuilder newRoomBuilder() {
        return Room.newRoomBuilder()
                   .name(DEFAULT_ROOM_NAME)
                   .seats(DEFAULT_ROOM_SEATS);
    }

    public static CreateRoomDTO newCreateRoomDto() {
        return (CreateRoomDTO) new CreateRoomDTO().name(DEFAULT_ROOM_NAME)
                                  .seats(DEFAULT_ROOM_SEATS);
    }
}
