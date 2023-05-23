package org.openjfx.api2semestre.view.controllers.popups;

import java.util.List;

@FunctionalInterface
public interface PopupCallbackHandler<T extends Popup> {
    void handlePopupList(List<T> controllers);
}
