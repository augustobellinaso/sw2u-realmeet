package br.com.sw2u.realmeet.report.validator;

import br.com.sw2u.realmeet.report.model.AbstractReportData;
import br.com.sw2u.realmeet.report.model.AllocationReportData;
import br.com.sw2u.realmeet.validator.ValidationErrors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Period;

import static br.com.sw2u.realmeet.util.Constants.ALLOCATION_REPORT_MAX_MONTHS_INTERVAL;
import static br.com.sw2u.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2u.realmeet.validator.ValidatorUtils.validateRequired;

@Component
public class AllocationReportValidator extends AbstractReportValidator {
    
    private final int maxMonthsInterval;
    
    public AllocationReportValidator(@Value(ALLOCATION_REPORT_MAX_MONTHS_INTERVAL) int maxMonthsInterval) {
        this.maxMonthsInterval = maxMonthsInterval;
    }
    
    @Override
    protected void validate(AbstractReportData abstractReportData, ValidationErrors validationErrors) {
        
        var allocationReportData = (AllocationReportData) abstractReportData;
        
        validateRequired(allocationReportData.getDateFrom(), FIELD_DATE_FROM, validationErrors);
        validateRequired(allocationReportData.getDateTo(), FIELD_DATE_TO, validationErrors);
        
        if (allocationReportData.getDateFrom().isAfter(allocationReportData.getDateTo())) {
            validationErrors.add(FIELD_DATE_FROM, FIELD_DATE_FROM + INCONSISTENT);
        } else if (Period.between(allocationReportData.getDateFrom(), allocationReportData.getDateTo()).getMonths() > maxMonthsInterval) {
            validationErrors.add(FIELD_DATE_TO, FIELD_DATE_TO + EXCEEDS_INTERVAL);
        }
        
        
    }
}
