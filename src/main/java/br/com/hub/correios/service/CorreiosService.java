package br.com.hub.correios.service;

import br.com.hub.correios.CorreiosApplication;
import br.com.hub.correios.exceptions.NoContentException;
import br.com.hub.correios.exceptions.NoReadyException;
import br.com.hub.correios.model.Endereco;
import br.com.hub.correios.model.EnderecoStatus;
import br.com.hub.correios.model.Status;
import br.com.hub.correios.repository.EnderecoRepository;
import br.com.hub.correios.repository.EnderecoStatusRepository;
import br.com.hub.correios.repository.SetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {

    private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoStatusRepository enderecoStatusRepository;

    @Autowired
    private SetupRepository setupRepository;

    public Status getStatus() {
        return this.enderecoStatusRepository.findById(EnderecoStatus.DEFAULT_ID)
                .orElse(new EnderecoStatus(EnderecoStatus.DEFAULT_ID, Status.NEED_SETUP))
                .getStatus();
    }

    public Endereco getEnderecoZipCode(String zipCode) throws NoContentException, NoReadyException{

        if(!this.getStatus().equals(Status.READY)){
            throw new NoReadyException();
        }

        return enderecoRepository.findById(zipCode)
                .orElseThrow(NoContentException::new);
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

//        logger.info("---" + a);
        logger.info("---");
        logger.info("--- STARTING SETUP");
        logger.info("--- Please wait... This may take a few minutes");
        logger.info("---");
        logger.info("---");

        if(this.getStatus().equals(Status.NEED_SETUP)){
            this.saveServiceStatus(Status.SETUP_RUNNING);

            try{
                // this.enderecoRepository.saveAll(this.setupRepository.getFromOrigin());
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
