package it.eng.agenziaViaggi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PartenzaPacchettoRequest(
        @NotNull LocalDate dataPartenza,
        @NotNull LocalDate dataRientro,
        @NotNull @DecimalMin("0.01") @Digits(integer = 8, fraction = 2) BigDecimal prezzo) {
}