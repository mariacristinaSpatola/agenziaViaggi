package it.eng.agenziaViaggi.service;

public class ClienteNonTrovatoException extends RuntimeException {

    public ClienteNonTrovatoException(Long id) {
        super("Cliente con id " + id + " non trovato");
    }
}
