package org.openjfx.api2semestre.view.controllers.templates;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.util.List;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.report.ReportInterval;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TotalHours {

    @FXML
    private static Label lblNumeroApontamentos;

    @FXML
    private static Label lblTotalHoras;

    @FXML
    private static Label lblTotalHorasVerba;

    public static void setTotalHours(ReportInterval[] listReportInterval) {
        long totalHora = 0;
        for (ReportInterval reportInterval : listReportInterval) {
            totalHora += reportInterval.getTotal().toNanoOfDay();
        }
        lblTotalHoras.setText(String.valueOf(totalHora));
    }

    public static void setTotalHoursRemunereted(ReportInterval[] listReportInterval) {
        long totalHora = 0;
        for (ReportInterval reportInterval : listReportInterval) {
            totalHora += reportInterval.getTotal().toNanoOfDay() * reportInterval.getVerba();
        }
    }

    public static void setNumTotalAppointments(List<Appointment> listApontamentos) {
        lblNumeroApontamentos.setText(((Integer)(listApontamentos.size()+1)).toString());
    }
}
