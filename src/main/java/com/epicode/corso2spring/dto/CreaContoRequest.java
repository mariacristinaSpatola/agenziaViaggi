package com.epicode.corso2spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DTO di ingresso per creazione/modifica. Le annotazioni di Bean Validation
 * descrivono le regole, ma diventano effettive solo con @Valid nel controller.
 */
@Schema(description = "Dati per creare o modificare un conto")
public record CreaContoRequest(

		@NotBlank(message = "L'intestatario è obbligatorio") // non null, non vuoto, non solo spazi
		@Size(max = 100, message = "L'intestatario può avere al massimo 100 caratteri")
		@Schema(description = "Nome dell'intestatario", example = "Anna Rossi")
		String intestatario,

		@NotNull(message = "Il saldo iniziale è obbligatorio") // @DecimalMin da solo accetterebbe null
		@DecimalMin(value = "0.0", message = "Il saldo iniziale non può essere negativo")
		@Schema(description = "Saldo iniziale, >= 0", example = "100.00")
		BigDecimal saldoIniziale) {
}
