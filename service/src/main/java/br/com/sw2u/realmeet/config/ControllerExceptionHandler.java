package br.com.sw2u.realmeet.config;

import br.com.sw2u.realmeet.api.model.ResponseError;
import br.com.sw2u.realmeet.exception.AllocationNotFoundException;
import br.com.sw2u.realmeet.exception.InvalidRequestException;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.sw2u.realmeet.util.ResponseEntityUtils.notFound;

@RestControllerAdvice
public class ControllerExceptionHandler {
    
    @ExceptionHandler({RoomNotFoundException.class, AllocationNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        return notFound();
    }
    
    
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<ResponseError> handleInvalidRequestException(InvalidRequestException exception) {
        return exception
                .getValidationErrors()
                .stream()
                .map(e -> new ResponseError()
                        .field(e.getField())
                        .errorCode(e.getErrorCode()))
                .collect(Collectors.toList());
        
    }
    
}
