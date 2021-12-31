package br.com.sw2u.realmeet.exception;

import br.com.sw2u.realmeet.validator.ValidationError;
import br.com.sw2u.realmeet.validator.ValidationErrors;

import static br.com.sw2u.realmeet.validator.ValidatorConstants.INVALID;
import static br.com.sw2u.realmeet.validator.ValidatorConstants.ORDER_BY;

public class InvalidOrderByFieldException extends InvalidRequestException{
    
    private static final long serialVersionUID = 1L;
    
    public InvalidOrderByFieldException() {
        super(new ValidationError(ORDER_BY, ORDER_BY + INVALID));
    }
}
