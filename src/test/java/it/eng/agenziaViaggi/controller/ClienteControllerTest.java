package it.eng.agenziaViaggi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.eng.agenziaViaggi.entity.Cliente;
import it.eng.agenziaViaggi.repository.ClienteRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {

    private static final String JSON_VALIDO = """
            {"nome":"Mario","cognome":"Rossi","dataNascita":"1980-05-10","indirizzo":"Via Roma 1"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository repository;

    @BeforeEach
    void svuotaDatabase() {
        repository.deleteAll();
    }

    @Test
    void creaClienteValidoRestituisce201ConIlCliente() throws Exception {
        mockMvc.perform(post("/api/clienti").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Mario"))
                .andExpect(jsonPath("$.dataNascita").value("1980-05-10"));
    }

    @Test
    void creaClienteSenzaNomeRestituisce400() throws Exception {
        String json = """
                {"nome":"","cognome":"Rossi","dataNascita":"1980-05-10","indirizzo":"Via Roma 1"}
                """;

        mockMvc.perform(post("/api/clienti").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creaClienteConDataNascitaFuturaRestituisce400() throws Exception {
        String json = """
                {"nome":"Mario","cognome":"Rossi","dataNascita":"%s","indirizzo":"Via Roma 1"}
                """.formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/clienti").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cercaPerNomeIgnoraMaiuscoleETrovaCorrispondenzeParziali() throws Exception {
        salva("Mario", "Rossi");
        salva("Marco", "Bianchi");
        salva("Luigi", "Verdi");

        mockMvc.perform(get("/api/clienti").param("nome", "MAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void cercaPerCognomeRestituisceSoloICorrispondenti() throws Exception {
        salva("Mario", "Rossi");
        salva("Luigi", "Verdi");

        mockMvc.perform(get("/api/clienti").param("cognome", "verdi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Luigi"));
    }

    @Test
    void cercaPerNomeECognomeRestituisceSoloIlClienteCheLiSoddisfaEntrambi() throws Exception {
        salva("Mario", "Rossi");
        salva("Mario", "Verdi");

        mockMvc.perform(get("/api/clienti").param("nome", "mario").param("cognome", "rossi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cognome").value("Rossi"));
    }

    @Test
    void modificaClienteEsistenteRestituisce200ConIDatiAggiornati() throws Exception {
        Long id = salva("Mario", "Rossi").getId();
        String json = """
                {"nome":"Mario","cognome":"Rossi","dataNascita":"1980-05-10","indirizzo":"Via Milano 2"}
                """;

        mockMvc.perform(put("/api/clienti/{id}", id).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.indirizzo").value("Via Milano 2"));
    }

    @Test
    void modificaClienteInesistenteRestituisce404() throws Exception {
        mockMvc.perform(put("/api/clienti/{id}", 999).contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Cliente con id 999 non trovato"));
    }

    @Test
    void modificaConDatiNonValidiRestituisce400() throws Exception {
        Long id = salva("Mario", "Rossi").getId();
        String json = """
                {"nome":"Mario","cognome":"Rossi","dataNascita":null,"indirizzo":"Via Roma 1"}
                """;

        mockMvc.perform(put("/api/clienti/{id}", id).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminaClienteEsistenteRestituisce204() throws Exception {
        Long id = salva("Mario", "Rossi").getId();

        mockMvc.perform(delete("/api/clienti/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/clienti"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void eliminaClienteInesistenteRestituisce404() throws Exception {
        mockMvc.perform(delete("/api/clienti/{id}", 999))
                .andExpect(status().isNotFound());
    }

    private Cliente salva(String nome, String cognome) {
        return repository.save(new Cliente(nome, cognome, LocalDate.of(1980, 5, 10), "Via Roma 1"));
    }
}
