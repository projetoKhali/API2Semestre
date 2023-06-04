package org.openjfx.api2semestre.view.utils.interfaces;

import javafx.scene.control.TableView;

public interface Popup<T> {
    T getSelected();
    void setSelected (T item);
    TableView<T> getTable();
}
