package it.eng.agenziaViaggi.controller;

import it.eng.agenziaViaggi.service.ClienteNonTrovatoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Estende ResponseEntityExceptionHandler cosi' anche gli errori di validazione diventano ProblemDetail
@RestControllerAdvice
public class GestoreErrori extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClienteNonTrovatoException.class)
    public ProblemDetail clienteNonTrovato(ClienteNonTrovatoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
