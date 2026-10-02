package com.epicode.corso2spring.repository;

import com.epicode.corso2spring.model.Conto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

/**
 * @Repository: bean Spring dello strato di accesso ai dati.
 * I conti stanno in una ConcurrentHashMap (thread-safe: il server gestisce più richieste insieme)
 * e vengono salvati periodicamente su file txt; all'avvio il file viene riletto.
 *
 * Formato file, una riga per conto: id;intestatario(URL-encoded);saldo;flagAntiriciclaggio
 */
@Repository
public class ContoRepository {

	private static final Logger log = LoggerFactory.getLogger(ContoRepository.class);

	private final Map<Long, Conto> conti = new ConcurrentHashMap<>();
	private final AtomicLong contatoreId = new AtomicLong(0); // generatore id thread-safe
	private final Path file;

	/** @Value legge la proprietà da application.properties (con valore di default dopo i ':'). */
	public ContoRepository(@Value("${conti.file:conti.txt}") String percorsoFile) {
		this.file = Path.of(percorsoFile);
	}

	/** @PostConstruct: eseguito una volta, dopo la creazione del bean. Qui ricarica i conti dal file. */
	@PostConstruct
	void carica() {
		if (!Files.exists(file)) {
			return;
		}
		try {
			for (String riga : Files.readAllLines(file, StandardCharsets.UTF_8)) {
				if (riga.isBlank()) {
					continue;
				}
				String[] p = riga.split(";", -1);
				long id = Long.parseLong(p[0]);
				String intestatario = URLDecoder.decode(p[1], StandardCharsets.UTF_8);
				conti.put(id, new Conto(id, intestatario, new BigDecimal(p[2]), Boolean.parseBoolean(p[3])));
				contatoreId.accumulateAndGet(id, Math::max); // il prossimo id parte dopo il massimo letto
			}
			log.info("Caricati {} conti da {}", conti.size(), file.toAbsolutePath());
		} catch (IOException | RuntimeException e) {
			log.error("Impossibile leggere {}", file.toAbsolutePath(), e);
		}
	}

	/**
	 * @Scheduled: Spring richiama il metodo ogni N millisecondi (richiede @EnableScheduling).
	 * fixedDelay = attesa dalla FINE dell'esecuzione precedente.
	 * @PreDestroy: salva anche allo spegnimento, così non si perde nulla.
	 */
	@Scheduled(fixedDelayString = "${conti.salvataggio-ms:10000}", initialDelayString = "${conti.salvataggio-ms:10000}")
	@PreDestroy
	public synchronized void salva() {
		List<String> righe = new ArrayList<>();
		for (Conto c : conti.values()) {
			righe.add(c.getId() + ";" + URLEncoder.encode(c.getIntestatario(), StandardCharsets.UTF_8)
					+ ";" + c.getSaldo().toPlainString() + ";" + c.isSegnalatoAntiriciclaggio());
		}
		try {
			// Scrive su file temporaneo e poi sostituisce: un crash a metà non corrompe il file buono
			Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
			Files.write(tmp, righe, StandardCharsets.UTF_8);
			Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error("Impossibile salvare {}", file.toAbsolutePath(), e);
		}
	}

	public Conto crea(String intestatario, BigDecimal saldo) {
		long id = contatoreId.incrementAndGet();
		Conto conto = new Conto(id, intestatario, saldo, false);
		conti.put(id, conto);
		return conto;
	}

	public Optional<Conto> trova(long id) {
		return Optional.ofNullable(conti.get(id));
	}

	public Collection<Conto> trovaTutti() {
		return conti.values();
	}

	public boolean elimina(long id) {
		return conti.remove(id) != null;
	}
}
