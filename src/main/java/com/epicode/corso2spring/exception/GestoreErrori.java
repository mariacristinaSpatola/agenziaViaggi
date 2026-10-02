package com.epicode.corso2spring.exception;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice: classe unica che intercetta le eccezioni di TUTTI i controller
 * e risponde sempre con lo stesso formato (ProblemDetail, RFC 9457).
 */
@RestControllerAdvice
public class GestoreErrori {

	private static final Logger log = LoggerFactory.getLogger(GestoreErrori.class);

	/** 404: conto inesistente. */
	@ExceptionHandler(ContoNonTrovato.class)
	public ProblemDetail contoNonTrovato(ContoNonTrovato e) {
		ProblemDetail p = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		p.setTitle("Conto non trovato");
		p.setDetail(e.getMessage());
		return p;
	}

	/** 400: la validazione (@Valid) è fallita; elenca campo e messaggio. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail validazione(MethodArgumentNotValidException e) {
		List<String> errori = e.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage())
				.toList();
		ProblemDetail p = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		p.setTitle("Richiesta non valida");
		p.setDetail("Uno o più campi non sono validi");
		p.setProperty("errori", errori); // campo extra nella risposta
		return p;
	}

	/** Rete di sicurezza: qualunque altra eccezione diventa un 500 pulito, dettagli solo nel log. */
	@ExceptionHandler(Exception.class)
	public ProblemDetail generico(Exception e) {
		log.error("Errore non gestito", e);
		ProblemDetail p = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		p.setTitle("Errore interno");
		return p;
	}
}
