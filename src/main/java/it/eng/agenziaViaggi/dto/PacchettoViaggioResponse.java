package it.eng.agenziaViaggi.dto;

import it.eng.agenziaViaggi.entity.PacchettoViaggio;
import java.util.List;

public record PacchettoViaggioResponse(
        Long id,
        String nome,
        String descrizione,
        String destinazione,
        List<PartenzaPacchettoResponse> partenze) {

    public static PacchettoViaggioResponse da(PacchettoViaggio pacchetto) {
        return new PacchettoViaggioResponse(
                pacchetto.getId(),
                pacchetto.getNome(),
                pacchetto.getDescrizione(),
                pacchetto.getDestinazione(),
                pacchetto.getPartenze().stream()
                        .map(PartenzaPacchettoResponse::da)
                        .toList());
    }
}