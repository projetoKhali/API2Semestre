package org.openjfx.api2semestre.view.controllers.popups;

import java.util.List;

import org.openjfx.api2semestre.view.utils.wrappers.AppointmentWrapper;

@FunctionalInterface public interface PopupCallback<T extends Popup<AppointmentWrapper>> {
    void handlePopupList(List<T> controllers);
}
