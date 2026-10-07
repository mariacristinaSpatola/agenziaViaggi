package it.eng.agenziaViaggi.service;

import it.eng.agenziaViaggi.dto.ClienteRequest;
import it.eng.agenziaViaggi.dto.ClienteResponse;
import it.eng.agenziaViaggi.entity.Cliente;
import it.eng.agenziaViaggi.repository.ClienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClienteResponse crea(ClienteRequest richiesta) {
        Cliente cliente = new Cliente(
                richiesta.nome(), richiesta.cognome(), richiesta.dataNascita(), richiesta.indirizzo());
        return ClienteResponse.da(repository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> cerca(String nome, String cognome) {
        boolean conNome = nome != null && !nome.isBlank();
        boolean conCognome = cognome != null && !cognome.isBlank();

        List<Cliente> risultati;
        if (conNome && conCognome) {
            risultati = repository.cercaPerNomeECognome(nome, cognome);
        } else if (conNome) {
            risultati = repository.findByNomeContainingIgnoreCase(nome);
        } else if (conCognome) {
            risultati = repository.findByCognomeContainingIgnoreCase(cognome);
        } else {
            risultati = repository.findAll();
        }
        return risultati.stream().map(ClienteResponse::da).toList();
    }

    @Transactional
    public ClienteResponse modifica(Long id, ClienteRequest richiesta) {
        Cliente cliente = repository.findById(id).orElseThrow(() -> new ClienteNonTrovatoException(id));
        cliente.aggiorna(richiesta.nome(), richiesta.cognome(), richiesta.dataNascita(), richiesta.indirizzo());
        return ClienteResponse.da(cliente);
    }

    @Transactional
    public void elimina(Long id) {
        if (!repository.existsById(id)) {
            throw new ClienteNonTrovatoException(id);
        }
        repository.deleteById(id);
    }
}
