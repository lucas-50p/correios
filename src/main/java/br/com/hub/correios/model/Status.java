package br.com.hub.correios.model;

public enum Status {
    NEED_SETUP, // prcisar baixar o CSV dos correios
    SETUP_RUNNING, // Está baixando / salvando no bano
    READY; // Serviço está apto para ser consumido
}
