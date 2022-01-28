package br.com.sw2u.realmeet.unit;

import br.com.sw2u.realmeet.config.JasperReportsConfiguration;
import br.com.sw2u.realmeet.core.BaseUnitTest;
import br.com.sw2u.realmeet.domain.repository.AllocationRepository;
import br.com.sw2u.realmeet.domain.repository.RoomRepository;
import br.com.sw2u.realmeet.exception.InvalidRequestException;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import br.com.sw2u.realmeet.report.enumeration.ReportFormat;
import br.com.sw2u.realmeet.report.handler.AllocationReportHandler;
import br.com.sw2u.realmeet.report.resolver.ReportHandlerResolver;
import br.com.sw2u.realmeet.report.validator.AllocationReportValidator;
import br.com.sw2u.realmeet.service.ReportCreationService;
import br.com.sw2u.realmeet.service.ReportDispatcherService;
import br.com.sw2u.realmeet.service.RoomService;
import br.com.sw2u.realmeet.util.Constants;
import br.com.sw2u.realmeet.validator.RoomValidator;
import br.com.sw2u.realmeet.validator.ValidationError;
import br.com.sw2u.realmeet.validator.ValidatorConstants;
import net.sf.jasperreports.engine.JasperReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Optional;

import static br.com.sw2u.realmeet.report.enumeration.ReportFormat.PDF;
import static br.com.sw2u.realmeet.util.Constants.EMPTY;
import static br.com.sw2u.realmeet.utils.MapperUtils.roomMapper;
import static br.com.sw2u.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2u.realmeet.utils.TestConstants.EMAIL_TO;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newCreateRoomDto;
import static br.com.sw2u.realmeet.utils.TestDataCreator.newRoomBuilder;
import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AllocationReportCreationServiceUnitTest extends BaseUnitTest {
    
    private static final int MAX_MONTHS = 2;
    
    private ReportCreationService victim;
    
    @Mock
    private ReportHandlerResolver reportHandlerResolver;
    
    @Mock
    private ReportDispatcherService reportDispatcherService;
    
    @Mock
    private AllocationRepository allocationRepository;
    
    @BeforeEach
    void setupEach() {
        victim = new ReportCreationService(reportHandlerResolver, reportDispatcherService);
        given(reportHandlerResolver.resolveReportHandler(any())).willReturn(
                new AllocationReportHandler(
                        new JasperReportsConfiguration().allocationReport(),
                        allocationRepository,
                        new AllocationReportValidator(MAX_MONTHS)
                ));
    }
    
    @Test
    void testCreateAllocationReportSuccess() {
        victim.createAllocationReport(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1), EMAIL_TO, PDF.name());
        
        verify(reportDispatcherService).dispatch(any());
    }
    
    @Test
    void testCreateAllocationReportNoEmail() {
        var exception = assertThrows(InvalidRequestException.class, () ->victim.createAllocationReport(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1), EMPTY, PDF.name()));
        
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(FIELD_EMAIL, FIELD_EMAIL + MISSING), exception.getValidationErrors().getError(0));
    }
    
    @Test
    void testCreateAllocationReportDateFromAfterDateTo() {
        var exception = assertThrows(InvalidRequestException.class, () ->victim.createAllocationReport(LocalDate.now().minusDays(1), LocalDate.now().minusDays(5), EMAIL_TO, PDF.name()));
        
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(FIELD_DATE_FROM, FIELD_DATE_FROM + INCONSISTENT), exception.getValidationErrors().getError(0));
    }
    
    @Test
    void testCreateAllocationReportDateToExceedsMaxInterval() {
        var exception = assertThrows(InvalidRequestException.class, () ->victim.createAllocationReport(LocalDate.now().minusMonths(MAX_MONTHS + 2), LocalDate.now().plusMonths(1), EMAIL_TO, PDF.name()));
        
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(FIELD_DATE_TO, FIELD_DATE_TO + EXCEEDS_INTERVAL), exception.getValidationErrors().getError(0));
    }
    
}
