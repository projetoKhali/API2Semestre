package org.openjfx.api2semestre.view.controllers.popups;

import org.openjfx.api2semestre.view.utils.wrappers.AppointmentWrapper;

public interface Popup {
    AppointmentWrapper getSelected();
    void setSelected (AppointmentWrapper aptWrapper);
    void buildTable ();
}
