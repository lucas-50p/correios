package br.com.hub.correios.controller;

import br.com.hub.correios.exceptions.NoContentException;
import br.com.hub.correios.exceptions.NoReadyException;
import br.com.hub.correios.model.Endereco;
import br.com.hub.correios.service.CorreiosService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorreioController {

    @Autowired
    private CorreiosService correiosService;

    @GetMapping("/status")
    public String getStaus(){
        return "Service status: " + this.correiosService.getStatus();
    }

    @GetMapping("/zipCode/{zipCode}")
    public Endereco getEnderecoCep(@PathVariable("zipCode") String zipCode) throws NoContentException, NoReadyException {
        // TODO apagar
//        Endereco endereco = new Endereco();
//        endereco.setZipCode(zipCode);
//        return endereco;
        return this.correiosService.getEnderecoZipCode(zipCode);
    }
}
