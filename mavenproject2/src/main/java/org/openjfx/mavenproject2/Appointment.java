/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openjfx.mavenproject2;

import java.time.LocalDate;

/**
 *
 * @author User
 */
public class Appointment {
    String squad, hora_inicio, hora_final, cliente, projeto, justificativa;
    LocalDate data_inicio, data_final;

    public Appointment(String squad, LocalDate data_inicio, String hora_inicio, LocalDate data_final, String hora_final, String cliente, String projeto, String justificativa) {
        this.squad = squad;
        this.data_inicio = data_inicio;
        this.hora_inicio = hora_inicio;
        this.data_final = data_final;
        this.hora_final = hora_final;
        this.cliente = cliente;
        this.projeto = projeto;
        this.justificativa = justificativa;
    }

    public String getSquad() {
        return squad;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public String getData_final() {
        return data_final;
    }

    public String getHora_final() {
        return hora_final;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProjeto() {
        return projeto;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setSquad(String squad) {
        this.squad = squad;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public void setData_final(String data_final) {
        this.data_final = data_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
    
    
    
}
