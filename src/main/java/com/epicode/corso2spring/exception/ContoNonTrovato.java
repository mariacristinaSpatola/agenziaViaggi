package com.epicode.corso2spring.exception;

/** Eccezione di dominio: lanciata dal service, tradotta in 404 dal gestore globale. */
public class ContoNonTrovato extends RuntimeException {

	public ContoNonTrovato(long id) {
		super("Conto " + id + " inesistente");
	}
}
