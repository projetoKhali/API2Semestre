package org.openjfx.api2semestre.view.controllers.popups;

import javafx.scene.control.TableView;

public interface Popup<T> {
    T getSelected();
    void setSelected (T item);
    TableView<T> getTable();
}
