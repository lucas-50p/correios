package br.com.hub.correios.controller;

import br.com.hub.correios.controller.dto.EnderecoDTO;
import br.com.hub.correios.exceptions.NoContentException;
import br.com.hub.correios.exceptions.NoReadyException;
import br.com.hub.correios.model.ConsultaLog;
import br.com.hub.correios.model.Endereco;
import br.com.hub.correios.repository.ConsultaLogRepository;
import br.com.hub.correios.service.CorreiosService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@RestController
public class CorreioController {

    @Autowired
    private CorreiosService correiosService;

    @Autowired
    private ConsultaLogRepository consultaLogRepository;

    @GetMapping("/status")
    public String getStaus(){

        return "Service status: " + this.correiosService.getStatus();
    }

    @GetMapping("/zipCode/{zipCode}")
    public ResponseEntity<EnderecoDTO> getEnderecoCep(@PathVariable("zipCode") String zipCode) throws NoContentException, NoReadyException {

        try{
            // Consultando o endereço
            EnderecoDTO enderecoDTO = this.correiosService.getEnderecoZipCode(zipCode);

            // Gravando o log da consulta no banco de dados
            ConsultaLog log = new ConsultaLog(zipCode, enderecoDTO, LocalDateTime.now());
            consultaLogRepository.save(log);

            return ResponseEntity.ok(enderecoDTO);
        } catch (Exception e) {
            // Se ocorrer um erro, retornamos um erro genérico
            return ResponseEntity.status(500).body(null);
        }
    }
}
