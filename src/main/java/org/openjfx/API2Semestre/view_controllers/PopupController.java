package org.openjfx.api2semestre.view_controllers;

import org.openjfx.api2semestre.view_utils.AppointmentWrapper;

public interface PopupController {
    AppointmentWrapper getSelected();
    void setSelected (AppointmentWrapper aptWrapper);
    void buildTable ();
}
