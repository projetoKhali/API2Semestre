package org.openjfx.api2semestre.view_controllers;

import java.util.List;

@FunctionalInterface
public interface PopupCallbackHandler<T extends PopupController> {
    void handlePopupList(List<T> controllers);
}
