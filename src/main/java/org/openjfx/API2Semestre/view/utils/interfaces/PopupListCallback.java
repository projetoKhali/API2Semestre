package org.openjfx.api2semestre.view.utils.interfaces;

import java.util.List;

@FunctionalInterface public interface PopupListCallback<T> {
    void handlePopupList(List<T> controllers);
}

