package org.openjfx.api2semestre.view.controllers.views.popups;

import java.util.List;

@FunctionalInterface
public interface PopupCallbackHandler<T extends PopupController> {
    void handlePopupList(List<T> controllers);
}
