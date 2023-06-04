package org.openjfx.api2semestre.view.utils.interfaces;

public interface EditPopup<T> extends Popup<T> {
    void setSaveCallback(PopupCallback<T> callback);
}
