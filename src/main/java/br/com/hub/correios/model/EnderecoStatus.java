package br.com.hub.correios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.PixelGrabber;

@Entity
public class EnderecoStatus {
    public static final int DEFAULT_ID = 1;

    @Id
    private int id;
    private Status status;

    // Construtor padrão
    public EnderecoStatus() {}

    public EnderecoStatus(int id, Status status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
