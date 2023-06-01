package org.openjfx.api2semestre.view.controllers.templates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TotalHours {

    @FXML
    private static Label lblNumeroApontamentos;

    @FXML
    private static Label lblTotalHoras;

    @FXML
    private static Label lblTotalHorasVerba;

    public static void setTotalHours(String totalHoras) {
        lblTotalHoras.setText(totalHoras);
    }

    public static void setTotalHoursVerb(String totalHorasVerba) {
        lblTotalHorasVerba.setText(totalHorasVerba);
    }

    public static void setNumTotalAppointments(String totalApontamentos) {
        lblNumeroApontamentos.setText(totalApontamentos);
    }
}
