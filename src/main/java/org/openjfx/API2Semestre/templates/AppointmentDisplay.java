package org.openjfx.api2semestre.templates;

import java.net.URL;
import java.util.ResourceBundle;

import org.openjfx.api2semestre.classes.Appointment;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
// import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AppointmentDisplay implements Initializable {
    
    private static final String[] STATUS = new String[] {"Pendente", "Aprovado", "Recusado"};

    @FXML
    private Label status;

    @FXML
    private Label tipo;

    @FXML
    private Label inicio;

    @FXML
    private Label fim;

    @FXML
    private Label cr;    

    @FXML
    private Label cliente;

    @FXML
    private Label projeto;

    public AppointmentDisplay () {
        super();
    }

    // public AppointmentDisplay (Appointment apt) {
    //     super();
    //     this.setAppointment(apt);
    // }

    public void setAppointment (Appointment apt) {

        System.out.println("status:\t" + status);
        System.out.println("tipo:\t" + tipo);
        System.out.println("inicio:\t" + inicio);
        System.out.println("fim:\t" + fim);
        System.out.println("cr:\t" + cr);
        System.out.println("cliente:\t" + cliente);
        System.out.println("projeto:\t" + projeto);

        status.setText(STATUS[apt.getAprovacao()]);
        tipo.setText(apt.getType().getStringValue());
        inicio.setText(apt.getStartDate().toString());
        fim.setText(apt.getEndDate().toString());
        cr.setText(apt.getSquad());
        cliente.setText(apt.getClient());
        projeto.setText(apt.getProject());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
