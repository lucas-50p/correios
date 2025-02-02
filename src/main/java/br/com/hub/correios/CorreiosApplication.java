package br.com.hub.correios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.hub.correios")
public class CorreiosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorreiosApplication.class, args);
	}

}
