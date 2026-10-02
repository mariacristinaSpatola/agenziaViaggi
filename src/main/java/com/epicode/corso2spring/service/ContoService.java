package com.epicode.corso2spring.service;

import com.epicode.corso2spring.dto.ContoDTO;
import com.epicode.corso2spring.dto.CreaContoRequest;
import com.epicode.corso2spring.exception.ContoNonTrovato;
import com.epicode.corso2spring.model.Conto;
import com.epicode.corso2spring.repository.ContoRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

/** @Service: logica di business. Non sa nulla di HTTP: lavora solo con DTO ed entità. */
@Service
public class ContoService {

	private static final int DIMENSIONE_PAGINA = 10;

	private final ContoRepository repository;

	/** Iniezione delle dipendenze via costruttore: Spring passa il repository già creato. */
	public ContoService(ContoRepository repository) {
		this.repository = repository;
	}

	public List<ContoDTO> elenca(int pagina) {
		return repository.trovaTutti().stream()
				.sorted(Comparator.comparingLong(Conto::getId)) // la mappa non garantisce l'ordine
				.skip((long) Math.max(pagina, 0) * DIMENSIONE_PAGINA)
				.limit(DIMENSIONE_PAGINA)
				.map(this::toDto)
				.toList();
	}

	public ContoDTO leggi(long id) {
		return repository.trova(id).map(this::toDto).orElseThrow(() -> new ContoNonTrovato(id));
	}

	public ContoDTO crea(CreaContoRequest richiesta) {
		return toDto(repository.crea(richiesta.intestatario().trim(), richiesta.saldoIniziale()));
	}

	public ContoDTO aggiorna(long id, CreaContoRequest richiesta) {
		Conto conto = repository.trova(id).orElseThrow(() -> new ContoNonTrovato(id));
		conto.setIntestatario(richiesta.intestatario().trim());
		conto.setSaldo(richiesta.saldoIniziale());
		return toDto(conto);
	}

	public void elimina(long id) {
		if (!repository.elimina(id)) {
			throw new ContoNonTrovato(id);
		}
	}

	/** Conversione entità -> DTO: il flag antiriciclaggio resta fuori. */
	private ContoDTO toDto(Conto c) {
		return new ContoDTO(c.getId(), c.getIntestatario(), c.getSaldo());
	}
}
