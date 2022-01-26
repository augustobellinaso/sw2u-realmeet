package br.com.sw2u.realmeet.report.validator;

import br.com.sw2u.realmeet.report.model.AbstractReportData;
import br.com.sw2u.realmeet.validator.ValidationErrors;

import static br.com.sw2u.realmeet.validator.ValidatorConstants.FIELD_EMAIL;
import static br.com.sw2u.realmeet.validator.ValidatorUtils.throwOnError;
import static br.com.sw2u.realmeet.validator.ValidatorUtils.validateRequired;

public abstract class AbstractReportValidator {
    
    public void validate(AbstractReportData abstractReportData) {
        var validationErrors = new ValidationErrors();
        
        validateRequired(abstractReportData.getEmail(), FIELD_EMAIL, validationErrors);
        validate(abstractReportData, validationErrors);
        throwOnError(validationErrors);
    }
    
    protected abstract void validate(AbstractReportData abstractReportData, ValidationErrors validationErrors);
}
