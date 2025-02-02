package br.com.hub.correios.repository;

import br.com.hub.correios.model.ConsultaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaLogRepository extends JpaRepository<ConsultaLog, Long> {
}
