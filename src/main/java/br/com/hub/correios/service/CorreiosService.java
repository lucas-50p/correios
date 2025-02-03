package br.com.hub.correios.service;

import br.com.hub.correios.CorreiosApplication;
import br.com.hub.correios.controller.dto.EnderecoDTO;
import br.com.hub.correios.exceptions.NoContentException;
import br.com.hub.correios.exceptions.NoReadyException;
import br.com.hub.correios.model.Endereco;
import br.com.hub.correios.model.EnderecoStatus;
import br.com.hub.correios.model.Status;
import br.com.hub.correios.repository.EnderecoRepository;
import br.com.hub.correios.repository.EnderecoStatusRepository;
import br.com.hub.correios.repository.SetupRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CorreiosService {

    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoStatusRepository enderecoStatusRepository;

    @Autowired
    private SetupRepository setupRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final String viaCepUrl = "https://viacep.com.br/ws/{cep}/json/";

    public Status getStatus() {
        return this.enderecoStatusRepository.findById(EnderecoStatus.DEFAULT_ID)
                .orElse(new EnderecoStatus(EnderecoStatus.DEFAULT_ID, Status.NEED_SETUP))
                .getStatus();
    }

    public EnderecoDTO getEnderecoZipCode(String zipCode) throws Exception {

        // Fazendo a requisição para a API ViaCEP passando o CEP como parâmetro
        String url = viaCepUrl;

        // Usando RestTemplate para consumir a API do ViaCEP
        String response = restTemplate.getForObject(url, String.class, zipCode);

        EnderecoDTO enderecoDTO = converterJsonParaEnderecoDTO(response);
        return enderecoDTO;
    }

    private EnderecoDTO converterJsonParaEnderecoDTO(String json) throws Exception {

        JsonNode jsonNode = objectMapper.readTree(json);

        String zipCode = jsonNode.get("cep").asText();
        String rua = jsonNode.get("logradouro").asText();
        String distrito = jsonNode.get("bairro").asText();
        String cidade = jsonNode.get("localidade").asText();
        String estado = jsonNode.get("uf").asText();

        return new EnderecoDTO(zipCode, rua, distrito, cidade, estado);
    }

    private void saveServiceStatus(Status status) {
        EnderecoStatus enderecoStatus = new EnderecoStatus();
        enderecoStatus.setId(EnderecoStatus.DEFAULT_ID);
        enderecoStatus.setStatus(status);
        enderecoStatusRepository.save(enderecoStatus);

    }

    @EventListener(ApplicationStartedEvent.class)
    protected void setupOnStartup() {

        try {
            this.setup();
        } catch (Exception exc){
            logger.error("Error during setup: ", exc);
            System.exit(999); // Ou apenas lance a exceção, se preferir
        }
    }

    public void setup() throws Exception {

        logger.info("---");
        logger.info("--- STARTING SETUP");
        logger.info("--- Please wait... This may take a few minutes");
        logger.info("---");
        logger.info("---");

        if(this.getStatus().equals(Status.NEED_SETUP)){
            this.saveServiceStatus(Status.SETUP_RUNNING);

            try{
                this.enderecoRepository.saveAll(this.setupRepository.listAdressesFromOrigin());
            } catch (Exception exc){
                this.saveServiceStatus(Status.NEED_SETUP);
                throw exc;
            }

            this.saveServiceStatus(Status.READY);

            logger.info("---");
            logger.info("---");
            logger.info("--- READY TO USE");
            logger.info("--- Good luck my friend :)");
            logger.info("---");
            logger.info("---");
        }
    }
}
