package it.eng.agenziaViaggi.dto;

import it.eng.agenziaViaggi.entity.Cliente;
import java.time.LocalDate;

public record ClienteResponse(Long id, String nome, String cognome, LocalDate dataNascita, String indirizzo) {

    public static ClienteResponse da(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCognome(),
                cliente.getDataNascita(),
                cliente.getIndirizzo());
    }
}
