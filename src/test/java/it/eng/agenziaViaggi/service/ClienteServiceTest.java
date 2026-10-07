package it.eng.agenziaViaggi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.eng.agenziaViaggi.dto.ClienteRequest;
import it.eng.agenziaViaggi.dto.ClienteResponse;
import it.eng.agenziaViaggi.entity.Cliente;
import it.eng.agenziaViaggi.repository.ClienteRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    private static final ClienteRequest RICHIESTA = new ClienteRequest("Mario", "Rossi", LocalDate.of(1980, 5, 10),
            "Via Roma 1");

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @Test
    void creaSalvaIlClienteERestituisceIDati() {
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteResponse risposta = service.crea(RICHIESTA);

        assertThat(risposta.nome()).isEqualTo("Mario");
        assertThat(risposta.cognome()).isEqualTo("Rossi");
        assertThat(risposta.dataNascita()).isEqualTo(LocalDate.of(1980, 5, 10));
        assertThat(risposta.indirizzo()).isEqualTo("Via Roma 1");
    }

    @Test
    void cercaConSoloNomeUsaLaRicercaPerNome() {
        when(repository.findByNomeContainingIgnoreCase("mar")).thenReturn(List.of(cliente()));

        assertThat(service.cerca("mar", null)).hasSize(1);
        verify(repository, never()).findAll();
    }

    @Test
    void cercaConSoloCognomeUsaLaRicercaPerCognome() {
        when(repository.findByCognomeContainingIgnoreCase("ros")).thenReturn(List.of(cliente()));

        assertThat(service.cerca(" ", "ros")).hasSize(1);
    }

    @Test
    void cercaConNomeECognomeUsaLaQueryCombinata() {
        when(repository.cercaPerNomeECognome("mar", "ros")).thenReturn(List.of(cliente()));

        assertThat(service.cerca("mar", "ros")).hasSize(1);
    }

    @Test
    void cercaSenzaFiltriRestituisceTuttiIClienti() {
        when(repository.findAll()).thenReturn(List.of(cliente(), cliente()));

        assertThat(service.cerca(null, null)).hasSize(2);
    }

    @Test
    void modificaAggiornaIDatiDelCliente() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente()));
        ClienteRequest nuoviDati = new ClienteRequest("Luigi", "Verdi", LocalDate.of(1990, 1, 1), "Via Milano 2");

        ClienteResponse risposta = service.modifica(1L, nuoviDati);

        assertThat(risposta.nome()).isEqualTo("Luigi");
        assertThat(risposta.cognome()).isEqualTo("Verdi");
        assertThat(risposta.indirizzo()).isEqualTo("Via Milano 2");
    }

    @Test
    void modificaDiClienteInesistenteLanciaEccezione() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.modifica(99L, RICHIESTA))
                .isInstanceOf(ClienteNonTrovatoException.class);
    }

    @Test
    void eliminaCancellaIlClienteEsistente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.elimina(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminaDiClienteInesistenteLanciaEccezione() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.elimina(99L)).isInstanceOf(ClienteNonTrovatoException.class);
        verify(repository, never()).deleteById(any());
    }

    private static Cliente cliente() {
        return new Cliente("Mario", "Rossi", LocalDate.of(1980, 5, 10), "Via Roma 1");
    }
}
