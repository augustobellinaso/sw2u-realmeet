package br.com.sw2u.realmeet.unit;

import static br.com.sw2u.realmeet.utils.TestDataCreator.newCreateRoomDto;
import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.exception.InvalidRequestException;
import br.com.sw2u.realmeet.validator.RoomValidator;
import br.com.sw2u.realmeet.validator.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomValidatorUnitTest extends BaseUnitTest {

    private RoomValidator victim;

    @BeforeEach
    void setUp() {
        victim = new RoomValidator();
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(newCreateRoomDto());
    }

    @Test
    void testValidateWhenRoomNameIsMissing() {
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRoomDto().name(null)));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomSeatsIsMissing() {
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRoomDto().seats(null)));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_SEATS, ROOM_SEATS + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomSeatsAreLessThanMinValue() {
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRoomDto().seats(ROOM_SEATS_MIN_VALUE - 1)));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_SEATS, ROOM_SEATS + BELOW_MIN_VALUE), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomSeatsAreMoreThanMaxValue() {
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRoomDto().seats(ROOM_SEATS_MAX_VALUE + 1)));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_SEATS, ROOM_SEATS + EXCEEDS_MAX_VALUE), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomNameLengthIsGreaterThanAllowed() {
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRoomDto().name(rightPad("X", ROOM_NAME_MAX_LENGTH + 1, "X"))));

        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }
}
