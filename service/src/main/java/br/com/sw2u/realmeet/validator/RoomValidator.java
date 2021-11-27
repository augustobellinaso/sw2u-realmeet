package br.com.sw2u.realmeet.validator;

import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2u.realmeet.validator.ValidatorUtils.*;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {

    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO createRoomDTO) {
        var validationErrors = new ValidationErrors();

        if (validateRoomName(createRoomDTO.getName(), validationErrors) && validateRoomSeats(createRoomDTO.getSeats(), validationErrors)) {
            validateDuplicatedName(createRoomDTO.getName(), validationErrors);
        }

        throwOnError(validationErrors);
    }

    private boolean validateRoomName(String name, ValidationErrors validationErrors) {
        return (
                validateRequired(name, ROOM_NAME, validationErrors) &&
                validateMaxLength(name, ROOM_NAME, ROOM_NAME_MAX_LENGTH, validationErrors)
        );
    }

    private boolean validateRoomSeats(Integer seats, ValidationErrors validationErrors) {
        return (
                validateRequired(seats, ROOM_SEATS, validationErrors) &&
                validateMinValue(seats, ROOM_SEATS, ROOM_SEATS_MIN_VALUE, validationErrors) &&
                validateMaxValue(seats, ROOM_SEATS, ROOM_SEATS_MAX_VALUE, validationErrors)
        );
    }

    public void validateDuplicatedName(String name, ValidationErrors validationErrors) {
        roomRepository.findByNameAndActive(name, true)
                      .ifPresent(__ ->
                                         validationErrors.add(ROOM_NAME, ROOM_NAME + ROOM_NAME_DUPLICATED)
                      );
    }
}
