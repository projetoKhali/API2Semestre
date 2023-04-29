package org.openjfx.api2semestre.view_macros;

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ColumnConfigString <T> extends ColumnConfig<T, String> {

    private Optional<BooleanProperty> enableFilterBooleanOptional;
    public ColumnConfigString(
        TableColumn<T, String> column,
        String propertyName,
        String displayName,
        Optional<BooleanProperty> enableFilterBooleanOptional
    ) {
        super(column, propertyName, displayName, false);
        this.enableFilterBooleanOptional = enableFilterBooleanOptional;
    }

    public Optional<BooleanProperty> getEnableFilterBoolean() { return enableFilterBooleanOptional; }

    @Override
    public void build (TableView<T> tabela, Optional<ChangeListener<Boolean>> applyFilterCallback) {
        super.build(tabela, applyFilterCallback);
        if (enableFilterBooleanOptional.isPresent() && applyFilterCallback.isPresent()) {
            BooleanProperty enableFilterBoolean = enableFilterBooleanOptional.get();
            TableColumnFilterMacros.setTextFieldHeader(column, displayName, enableFilterBoolean);
            enableFilterBoolean.addListener(applyFilterCallback.get());
        }
    }
}
