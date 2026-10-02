package com.epicode.corso2spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/** DTO di uscita: cosa restituiamo al client. Un record è immutabile e va benissimo per il trasporto dati. */
@Schema(description = "Conto restituito dall'API")
public record ContoDTO(
		@Schema(description = "Identificativo del conto", example = "42") long id,
		@Schema(description = "Nome dell'intestatario", example = "Anna Rossi") String intestatario,
		@Schema(description = "Saldo corrente", example = "1250.50") BigDecimal saldo) {
}
