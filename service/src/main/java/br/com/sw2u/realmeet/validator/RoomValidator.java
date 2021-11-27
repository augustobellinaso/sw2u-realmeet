package br.com.sw2u.realmeet.validator;

import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2u.realmeet.validator.ValidatorUtils.*;

import br.com.sw2u.realmeet.api.model.CreateRoomDTO;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.InvalidRequestException;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {

    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO createRoomDTO) {
        var validationErrors = new ValidationErrors();

        //Room name
        validateRequired(
                createRoomDTO.getName(),
                ROOM_NAME,
                validationErrors
        );
        validateMaxLength(
                createRoomDTO.getName(),
                ROOM_NAME,
                ROOM_NAME_MAX_LENGTH,
                validationErrors
        );

        //Room seats
        validateRequired(
                createRoomDTO.getSeats(),
                ROOM_SEATS,
                validationErrors
        );
        validateMinValue(
                createRoomDTO.getSeats(),
                ROOM_SEATS,
                ROOM_SEATS_MIN_VALUE,
                validationErrors
        );
        validateMaxValue(
                createRoomDTO.getSeats(),
                ROOM_SEATS,
                ROOM_SEATS_MAX_VALUE,
                validationErrors
        );

        throwOnError(validationErrors);

        validateDuplicatedName(createRoomDTO.getName());
    }

    public void validateDuplicatedName(String name) {
        roomRepository
                .findByNameAndActive(
                        name,
                        true
                )
                .ifPresent(__ -> {
                    throw new InvalidRequestException(new ValidationError(
                            ROOM_NAME,
                            ROOM_NAME + ROOM_NAME_DUPLICATED
                    ));
                });

    }
}
