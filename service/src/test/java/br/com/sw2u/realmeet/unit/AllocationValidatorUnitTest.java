package br.com.sw2u.realmeet.unit;

import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.exception.InvalidRequestException;
import br.com.sw2u.realmeet.util.DateUtils;
import br.com.sw2u.realmeet.validator.AllocationValidator;
import br.com.sw2u.realmeet.validator.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static br.com.sw2u.realmeet.util.DateUtils.now;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newCreateAllocationDto;
import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AllocationValidatorUnitTest extends BaseUnitTest {

    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setUp() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenAllocationIsValid() {
        victim.validate(newCreateAllocationDto());
    }

    @Test
    void testValidateWhenSubjectIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().subject(null))
        );

        assertEquals(1, exception.getValidationErrors()
                                 .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING), exception.getValidationErrors()
                                                                                   .getError(0));
    }
    
    @Test
    void testValidateWhenSubjectExceedsMaxLength() {
        var exception = assertThrows(InvalidRequestException.class,
                                     () -> victim.validate(newCreateAllocationDto().subject(rightPad("X", ALLOCATION_SUJECT_MAX_LENGTH + 1, "X")))
        );
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenEmployeeNameIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().employeeName(null))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + MISSING), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenEmployeeNameExceedsMaxLength() {
        var exception = assertThrows(InvalidRequestException.class,
                                     () -> victim.validate(newCreateAllocationDto().employeeName(rightPad("X", ALLOCATION_EMPLOYEE_NAME_MAX_LENGTH + 1, "X")))
        );
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + EXCEEDS_MAX_LENGTH), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenEmployeeEmailIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().employeeEmail(null))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + MISSING), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenEmployeeEmailExceedsMaxLength() {
        var exception = assertThrows(InvalidRequestException.class,
                                     () -> victim.validate(newCreateAllocationDto().employeeEmail(rightPad("X", ALLOCATION_EMPLOYEE_EMAIL_MAX_LENGTH + 1, "X")))
        );
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + EXCEEDS_MAX_LENGTH), exception.getValidationErrors()
                .getError(0));
    }
    
    @Test
    void testValidateWhenStartAtIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDto().startAt(null))
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
                () -> victim.validate(newCreateAllocationDto().endAt(null))
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
                () -> victim.validate(newCreateAllocationDto().startAt(now().plusDays(1L)).endAt(now().plusDays(1L).minusMinutes(30L)))
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
                () -> victim.validate(newCreateAllocationDto().startAt(now().minusMinutes(30L)).endAt(now().plusMinutes(30L)))
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
                () -> victim.validate(newCreateAllocationDto().startAt(now().plusDays(1L)).endAt(now().plusDays(1L).plusSeconds(ALLOCATION_MAX_DURATION_SECONDS + 1)))
        );
        
        assertEquals(1, exception.getValidationErrors()
                .getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_DURATION), exception.getValidationErrors()
                .getError(0));
    }
    
}
