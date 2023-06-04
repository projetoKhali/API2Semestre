package org.openjfx.api2semestre.view.utils.interfaces;

import org.openjfx.api2semestre.database.Data;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public interface EditableTableView<T extends Data> {
    @FXML void saveChanges(ActionEvent event);
}
