package br.com.hub.correios.repository;

import br.com.hub.correios.model.EnderecoStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface EnderecoStatusRepository extends CrudRepository<EnderecoStatus, Integer> {
}
