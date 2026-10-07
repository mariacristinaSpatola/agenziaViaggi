package it.eng.agenziaViaggi.controller;

import it.eng.agenziaViaggi.dto.ClienteRequest;
import it.eng.agenziaViaggi.dto.ClienteResponse;
import it.eng.agenziaViaggi.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clienti")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crea(@Valid @RequestBody ClienteRequest richiesta) {
        return service.crea(richiesta);
    }

    @GetMapping
    public List<ClienteResponse> cerca(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome) {
        return service.cerca(nome, cognome);
    }

    @PutMapping("/{id}")
    public ClienteResponse modifica(@PathVariable Long id, @Valid @RequestBody ClienteRequest richiesta) {
        return service.modifica(id, richiesta);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void elimina(@PathVariable Long id) {
        service.elimina(id);
    }
}
