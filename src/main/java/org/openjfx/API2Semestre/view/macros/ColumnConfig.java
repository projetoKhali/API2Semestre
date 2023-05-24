package org.openjfx.api2semestre.view.macros;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ColumnConfig <T, S> {
        
    protected TableColumn<T,S> column;
    protected String propertyName;
    protected String displayName;
    protected boolean isCheckBoxColumn;

    public ColumnConfig(TableColumn<T, S> column, String propertyName, String displayName, boolean isCheckBoxColumn) {
        this.column = column;
        this.propertyName = propertyName;
        this.displayName = displayName;
        this.isCheckBoxColumn = isCheckBoxColumn;
    }
    
    public TableColumn<T, S> getColumn() { return column; }
    public String getPropertyName() { return propertyName; }
    public String getDisplayName() { return displayName; }
    public boolean isCheckBoxColumn() { return isCheckBoxColumn; }

    public void build (TableView<T> tabela, Optional<ChangeListener<Boolean>> applyFilterCallback) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
    }

}
