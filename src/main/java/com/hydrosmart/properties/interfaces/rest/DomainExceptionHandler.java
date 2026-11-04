package com.hydrosmart.properties.interfaces.rest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.dao.DataIntegrityViolationException;
import com.hydrosmart.properties.domain.model.valueobjects.DomainException;
import com.hydrosmart.properties.interfaces.rest.resources.ErrorResource;
@RestControllerAdvice
public class DomainExceptionHandler {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResource> domain(DomainException e) {
        var status=switch(e.kind()) { case INVALID->HttpStatus.BAD_REQUEST; case FORBIDDEN->HttpStatus.FORBIDDEN; case NOT_FOUND->HttpStatus.NOT_FOUND; case CONFLICT->HttpStatus.CONFLICT; };
        return ResponseEntity.status(status).body(new ErrorResource(e.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResource> conflict(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResource("duplicate_or_resource_in_use"));
    }
}
