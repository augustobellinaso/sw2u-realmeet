package br.com.sw2u.realmeet.validator;

import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2u.realmeet.validator.ValidatorUtils.*;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {

    public void validate(CreateRoomDTO createRoomDTO) {
        var validationErrors = new ValidationErrors();

        //Room name
        validateRequired(createRoomDTO.getName(), ROOM_NAME, validationErrors);
        validateMaxLength(createRoomDTO.getName(), ROOM_NAME, ROOM_NAME_MAX_LENGTH, validationErrors);

        //Room seats
        validateRequired(createRoomDTO.getSeats(), ROOM_SEATS, validationErrors);
        validateMinValue(createRoomDTO.getSeats(), ROOM_SEATS, ROOM_SEATS_MIN_VALUE, validationErrors);
        validateMaxValue(createRoomDTO.getSeats(), ROOM_SEATS, ROOM_SEATS_MAX_VALUE, validationErrors);

        throwOnError(validationErrors);
    }
}
