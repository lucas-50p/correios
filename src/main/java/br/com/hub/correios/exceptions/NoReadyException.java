package br.com.hub.correios.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Serviço em instalação, Por favor aguarde de 3 a 5 minutos")// 503
public class NoReadyException extends RuntimeException{
}
