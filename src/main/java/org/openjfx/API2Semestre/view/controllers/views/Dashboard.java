package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.appointment.AppointmentType;
import org.openjfx.api2semestre.utils.DateConverter;
import org.openjfx.api2semestre.view.utils.ChartGenerator;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class Dashboard {

    @FXML private HBox hb_filters;

    @FXML private FlowPane fp_charts;

    public void initialize() {

        Appointment[] appointments = new Appointment[] {
            // new Appointment(1, "Julio", AppointmentType.Overtime, 
            //     DateConverter.stringToTimestamp("2023-05-05 22:00:00"), 
            //     DateConverter.stringToTimestamp("2023-05-07 19:00:00"), 
            //     "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            // ),
            // new Appointment(1, "Julio", AppointmentType.Overtime, 
            //     DateConverter.stringToTimestamp("2023-05-05 22:00:00"), 
            //     DateConverter.stringToTimestamp("2023-05-06 01:00:00"), 
            //     "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            // ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 12:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 13:00:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 12:30:00"), 
                DateConverter.stringToTimestamp("2023-05-05 13:30:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 13:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:00:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 13:30:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:30:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 14:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 15:00:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 14:15:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:45:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            )
        };

        fp_charts.getChildren().add(ChartGenerator.hourIntersectionCountGraph(appointments));
    }
}
