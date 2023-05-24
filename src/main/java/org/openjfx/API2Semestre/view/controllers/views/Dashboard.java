package org.openjfx.api2semestre.view.controllers.views;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class Dashboard {

    @FXML private FlowPane fp_charts;

    @FXML private HBox hb_filters;

    public void initialize() {
        System.out.println("oi");
    }
}
