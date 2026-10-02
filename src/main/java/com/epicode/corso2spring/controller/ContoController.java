package com.epicode.corso2spring.controller;

import com.epicode.corso2spring.dto.ContoDTO;
import com.epicode.corso2spring.dto.CreaContoRequest;
import com.epicode.corso2spring.service.ContoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @RestController = @Controller + @ResponseBody: i valori restituiti diventano JSON nel corpo della risposta.
 * @RequestMapping: prefisso comune a tutti gli endpoint della classe.
 * @Tag (OpenAPI): raggruppa gli endpoint nella documentazione.
 * Il controller non contiene logica: traduce HTTP <-> chiamate al service.
 */
@RestController
@RequestMapping("/conti")
@Tag(name = "Conti", description = "Operazioni CRUD sui conti")
public class ContoController {

	private final ContoService servizio;

	public ContoController(ContoService servizio) {
		this.servizio = servizio;
	}


	// @GetMapping = HTTP GET. @RequestParam legge ?pagina=... dalla query string.
	@GetMapping
	@Operation(summary = "Elenca i conti", description = "Restituisce 10 conti per pagina, ordinati per id")
	public List<ContoDTO> elenca(
			@Parameter(description = "Numero pagina, parte da 0")
			@RequestParam(defaultValue = "0") int pagina) {

		return servizio.elenca(pagina); // status 200 e conversione in JSON automatici
	}






	// {id} nel percorso viene legato al parametro annotato con @PathVariable.
	@GetMapping("/{id}")
	@Operation(summary = "Legge un conto")
	@ApiResponse(responseCode = "200", description = "Conto trovato")
	@ApiResponse(responseCode = "404", description = "Conto inesistente")
	public ContoDTO leggi(@PathVariable long id) {
		return servizio.leggi(id);
	}

	// @RequestBody: il JSON del corpo diventa un record. @Valid attiva le regole di validazione (400 se violate).
	@PostMapping
	@Operation(summary = "Crea un conto")
	@ApiResponse(responseCode = "201", description = "Conto creato; l'URL è nell'header Location")
	@ApiResponse(responseCode = "400", description = "Dati non validi")
	public ResponseEntity<ContoDTO> crea(@Valid @RequestBody CreaContoRequest richiesta) {
		ContoDTO creato = servizio.crea(richiesta);
		// Costruisce http://host/conti/{id} partendo dalla richiesta corrente
		URI posizione = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(creato.id()).toUri();
		return ResponseEntity.created(posizione).body(creato); // 201 + Location
	}

	@PutMapping("/{id}")
	@Operation(summary = "Sostituisce intestatario e saldo di un conto")
	@ApiResponse(responseCode = "200", description = "Conto aggiornato")
	@ApiResponse(responseCode = "400", description = "Dati non validi")
	@ApiResponse(responseCode = "404", description = "Conto inesistente")
	public ContoDTO aggiorna(@PathVariable long id, @Valid @RequestBody CreaContoRequest richiesta) {
		return servizio.aggiorna(id, richiesta);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina un conto")
	@ApiResponse(responseCode = "204", description = "Conto eliminato")
	@ApiResponse(responseCode = "404", description = "Conto inesistente")
	public ResponseEntity<Void> elimina(@PathVariable long id) {
		servizio.elimina(id);
		return ResponseEntity.noContent().build(); // 204 senza corpo
	}
}
