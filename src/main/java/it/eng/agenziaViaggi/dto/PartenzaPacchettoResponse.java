package it.eng.agenziaViaggi.dto;

import it.eng.agenziaViaggi.entity.PartenzaPacchetto;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PartenzaPacchettoResponse(
        Long id,
        LocalDate dataPartenza,
        LocalDate dataRientro,
        BigDecimal prezzo,
        String stato) {

    public static PartenzaPacchettoResponse da(PartenzaPacchetto partenza) {
        return new PartenzaPacchettoResponse(
                partenza.getId(),
                partenza.getDataPartenza(),
                partenza.getDataRientro(),
                partenza.getPrezzo(),
                partenza.getStato().name());
    }
}