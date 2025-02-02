package br.com.hub.correios.repository;

import br.com.hub.correios.model.Endereco;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SetupRepository {

    @Value("${setup.origin.url}")
    private String completeUrl;

    public SetupRepository() {
        System.out.println("URL Completa: " + completeUrl); // Adicione um log para verificar o valor
    }

    public List<Endereco> listAdressesFromOrigin() throws Exception {
        List<Endereco> result = new ArrayList<>();
        String resultStr = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(completeUrl))) {

            HttpEntity entity = response.getEntity();
            resultStr = EntityUtils.toString(entity);
        }

        for (String current : resultStr.split("\n")) {
            String[] currentSplited = current.split(",");

            if (currentSplited[0].length() > 2) // breaks the header line, if exists
                continue;

            Endereco endereco = new Endereco();
            endereco.setEstado(currentSplited[0]);
            endereco.setCidade(currentSplited[1]);
            endereco.setDistrito(currentSplited[2]);
            endereco.setZipCode(StringUtils.leftPad(currentSplited[3], 8, "0"));
            if (currentSplited.length > 4) {
                endereco.setRua(currentSplited[4]);
            }

            result.add(endereco);
        }

        return result;
    }

}
