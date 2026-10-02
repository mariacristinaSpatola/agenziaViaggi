package com.epicode.corso2spring.model;

import java.math.BigDecimal;

/**
 * Entità interna: contiene anche campi che NON devono mai uscire dall'API
 * (es. segnalatoAntiriciclaggio). Per questo si usano i DTO.
 */
public class Conto {

	private final long id;
	private String intestatario;
	private BigDecimal saldo;
	private boolean segnalatoAntiriciclaggio; // interno: non esposto nei DTO

	public Conto(long id, String intestatario, BigDecimal saldo, boolean segnalatoAntiriciclaggio) {
		this.id = id;
		this.intestatario = intestatario;
		this.saldo = saldo;
		this.segnalatoAntiriciclaggio = segnalatoAntiriciclaggio;
	}

	public long getId() {
		return id;
	}

	public String getIntestatario() {
		return intestatario;
	}

	public void setIntestatario(String intestatario) {
		this.intestatario = intestatario;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public boolean isSegnalatoAntiriciclaggio() {
		return segnalatoAntiriciclaggio;
	}

	public void setSegnalatoAntiriciclaggio(boolean segnalatoAntiriciclaggio) {
		this.segnalatoAntiriciclaggio = segnalatoAntiriciclaggio;
	}
}
