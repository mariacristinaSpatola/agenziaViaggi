package it.eng.agenziaViaggi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PacchettoViaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 2000)
    private String descrizione;

    @Column(nullable = false, length = 150)
    private String destinazione;

    @OneToMany(mappedBy = "pacchetto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartenzaPacchetto> partenze = new ArrayList<>();

    protected PacchettoViaggio() {
    }

    public PacchettoViaggio(String nome, String descrizione, String destinazione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.destinazione = destinazione;
    }

    public void aggiorna(String nome, String descrizione, String destinazione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.destinazione = destinazione;
    }

    public void aggiungiPartenza(PartenzaPacchetto partenza) {
        partenze.add(partenza);
        partenza.setPacchetto(this);
    }

    public void rimuoviPartenza(PartenzaPacchetto partenza) {
        if (partenze.remove(partenza)) {
            partenza.setPacchetto(null);
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public List<PartenzaPacchetto> getPartenze() {
        return List.copyOf(partenze);
    }
}