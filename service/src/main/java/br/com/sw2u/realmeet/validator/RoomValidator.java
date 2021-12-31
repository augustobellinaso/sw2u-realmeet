package br.com.sw2u.realmeet.validator;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2u.realmeet.validator.ValidatorUtils.*;
import static java.util.Objects.isNull;

@Component
public class RoomValidator {

    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO createRoomDTO) {
        var validationErrors = new ValidationErrors();

        if (validateRoomName(createRoomDTO.getName(), validationErrors) && validateRoomSeats(createRoomDTO.getSeats(), validationErrors)) {
            validateDuplicatedName(null, createRoomDTO.getName(), validationErrors);
        }

        throwOnError(validationErrors);
    }

    public void validate(Long roomId, UpdateRoomDTO updateRoomDTO) {
        var validationErrors = new ValidationErrors();

        if (validateRequired(roomId, ROOM_ID, validationErrors) &&
            validateRoomName(updateRoomDTO.getName(), validationErrors) &&
            validateRoomSeats(updateRoomDTO.getSeats(), validationErrors)) {
            validateDuplicatedName(roomId, updateRoomDTO.getName(), validationErrors);
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

    public void validateDuplicatedName(Long roomIdToExclude, String name, ValidationErrors validationErrors) {
        roomRepository.findByNameAndActive(name, true)
                      .ifPresent(room -> {
                                     if (isNull(roomIdToExclude) || !Objects.equals(room.getId(), roomIdToExclude)) {
                                         validationErrors.add(ROOM_NAME, ROOM_NAME + ROOM_NAME_DUPLICATED);
                                     }
                                 }
                      );
    }
}
