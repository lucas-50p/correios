package br.com.hub.correios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
public class Endereco {

    @Id
    private String zipCode;
    private String rua;
    private String distrito;
    private String cidade;
    private String estado;

    public Endereco(){
    }

    public Endereco(String zipCode, String rua, String distrito, String cidade, String estado) {
        this.zipCode = zipCode;
        this.rua = rua;
        this.distrito = distrito;
        this.cidade = cidade;
        this.estado = estado;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


}
