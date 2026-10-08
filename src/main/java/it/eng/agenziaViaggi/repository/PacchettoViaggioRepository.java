package it.eng.agenziaViaggi.repository;

import it.eng.agenziaViaggi.entity.PacchettoViaggio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacchettoViaggioRepository extends JpaRepository<PacchettoViaggio, Long> {
}