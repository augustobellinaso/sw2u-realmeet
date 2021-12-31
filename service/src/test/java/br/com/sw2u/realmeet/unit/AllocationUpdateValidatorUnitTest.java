package br.com.sw2u.realmeet.unit;

import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.exception.InvalidRequestException;
import br.com.sw2u.realmeet.validator.AllocationValidator;
import br.com.sw2u.realmeet.validator.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static br.com.sw2u.realmeet.util.DateUtils.now;
import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ALLOCATION_ID;
import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newUpdateAllocationDto;
import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AllocationUpdateValidatorUnitTest extends BaseUnitTest {

    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setUp() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenAllocationIsValid() {
        victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto());
    }
    
    @Test
    void testValidateWhenAllocationIdIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(null, DEFAULT_ROOM_ID, newUpdateAllocationDto())
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_ID, ALLOCATION_ID + MISSING), exception.getValidationErrors()
                .getError(0));
    }

    @Test
    void testValidateWhenSubjectIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto().subject(null))
        );

        assertEquals(1, exception.getValidationErrors()
                                 .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING), exception.getValidationErrors()
                                                                                   .getError(0));
    }
    
    @Test
    void testValidateWhenSubjectExceedsMaxLength() {
        var exception = assertThrows(InvalidRequestException.class,
                                     () -> victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto().subject(rightPad("X", ALLOCATION_SUJECT_MAX_LENGTH + 1, "X")))
        );
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenStartAtIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto().startAt(null))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + MISSING), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenEndAtIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto().endAt(null))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + MISSING), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenDateOrderIsInvalid() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto().startAt(now().plusDays(1L)).endAt(now().plusDays(1L).minusMinutes(30L)))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + INCONSISTENT), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenDateIsInThePast() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto().startAt(now().minusMinutes(30L)).endAt(now().plusMinutes(30L)))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenDateIntervalExceedsMaxDuration() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(DEFAULT_ALLOCATION_ID, DEFAULT_ROOM_ID, newUpdateAllocationDto().startAt(now().plusDays(1L)).endAt(now().plusDays(1L).plusSeconds(ALLOCATION_MAX_DURATION_SECONDS + 1)))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_DURATION), exception.getValidationErrors()
                .getError(0));
    }
    
}
