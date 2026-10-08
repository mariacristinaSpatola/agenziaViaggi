package it.eng.agenziaViaggi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PacchettoViaggioRequest(
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Size(max = 2000) String descrizione,
        @NotBlank @Size(max = 150) String destinazione) {
}