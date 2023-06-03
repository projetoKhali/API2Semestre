package org.openjfx.api2semestre.view.controllers.templates;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.ReportInterval;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class TotalHours {

    @FXML
    private Label lblNumeroApontamentos;

    @FXML
    private Label lblTotalHoras;

    @FXML
    private Label lblTotalHorasVerba;

    public void setTotalHours(ReportInterval[] listReportInterval) {
        long totalHora = 0;
        for (ReportInterval reportInterval : listReportInterval) {
            totalHora += reportInterval.getTotal().toNanoOfDay();
        }
        lblTotalHoras.setText(String.valueOf(totalHora));
    }

    public void setTotalHoursRemunereted(ReportInterval[] listReportInterval) {
        double totalHora = 0;
        IntervalFee[] verbas = IntervalFee.getVerbas();
        for (ReportInterval reportInterval : listReportInterval) {
            for (IntervalFee verba : verbas) {
                if (reportInterval.getVerba() == verba.getCode()) {
                    totalHora += reportInterval.getTotal().toNanoOfDay() * verba.getPercent();
                    break;
                }
            }
        }
        lblTotalHorasVerba.setText(String.valueOf(totalHora));
    }

    public void setNumTotalAppointments(Appointment[] listApontamentos) {
        lblNumeroApontamentos.setText(((Integer)(listApontamentos.length)).toString());
    }

    public static Parent totalHoursParent(Appointment[] listAppointments, ReportInterval[] listReportInterval) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.getFXML("templates/totalHours"));
            Parent totalHoursParent = fxmlLoader.load();
            TotalHours controller = fxmlLoader.getController();
            controller.setNumTotalAppointments(listAppointments);
            controller.setTotalHours(listReportInterval);
            controller.setTotalHoursRemunereted(listReportInterval);
            return totalHoursParent;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erro - totalHoursParent() | erro ao gerar tela");
        }
        return null;
    }
}
