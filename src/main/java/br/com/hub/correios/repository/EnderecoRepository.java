package br.com.hub.correios.repository;

import br.com.hub.correios.model.Endereco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface EnderecoRepository extends CrudRepository<Endereco, String> {
}
