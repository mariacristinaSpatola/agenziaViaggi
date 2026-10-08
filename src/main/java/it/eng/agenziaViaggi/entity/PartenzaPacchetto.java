package it.eng.agenziaViaggi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class PartenzaPacchetto {

    public enum Stato {
        DISPONIBILE,
        ESAURITO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataPartenza;

    @Column(nullable = false)
    private LocalDate dataRientro;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Stato stato = Stato.DISPONIBILE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pacchetto_id", nullable = false)
    private PacchettoViaggio pacchetto;

    protected PartenzaPacchetto() {
    }

    public PartenzaPacchetto(LocalDate dataPartenza, LocalDate dataRientro, BigDecimal prezzo) {
        validaPeriodo(dataPartenza, dataRientro);
        validaPrezzo(prezzo);
        this.dataPartenza = dataPartenza;
        this.dataRientro = dataRientro;
        this.prezzo = prezzo;
    }

    public void aggiorna(LocalDate dataPartenza, LocalDate dataRientro, BigDecimal prezzo) {
        validaPeriodo(dataPartenza, dataRientro);
        validaPrezzo(prezzo);
        this.dataPartenza = dataPartenza;
        this.dataRientro = dataRientro;
        this.prezzo = prezzo;
    }

    public void esaurisci() {
        this.stato = Stato.ESAURITO;
    }

    public void rendiDisponibile() {
        this.stato = Stato.DISPONIBILE;
    }

    void setPacchetto(PacchettoViaggio pacchetto) {
        this.pacchetto = pacchetto;
    }

    private static void validaPeriodo(LocalDate dataPartenza, LocalDate dataRientro) {
        if (dataPartenza == null || dataRientro == null || !dataRientro.isAfter(dataPartenza)) {
            throw new IllegalArgumentException(
                    "La data di rientro deve essere successiva alla data di partenza");
        }
    }

    private static void validaPrezzo(BigDecimal prezzo) {
        if (prezzo == null || prezzo.signum() <= 0) {
            throw new IllegalArgumentException("Il prezzo deve essere maggiore di zero");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataPartenza() {
        return dataPartenza;
    }

    public LocalDate getDataRientro() {
        return dataRientro;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public Stato getStato() {
        return stato;
    }
}