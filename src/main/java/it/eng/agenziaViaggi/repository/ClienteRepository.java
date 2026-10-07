package it.eng.agenziaViaggi.repository;

import it.eng.agenziaViaggi.entity.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    List<Cliente> findByCognomeContainingIgnoreCase(String cognome);

    @Query("""
            select c from Cliente c
            where lower(c.nome) like lower(concat('%', :nome, '%'))
              and lower(c.cognome) like lower(concat('%', :cognome, '%'))
            """)
    List<Cliente> cercaPerNomeECognome(@Param("nome") String nome, @Param("cognome") String cognome);
}
