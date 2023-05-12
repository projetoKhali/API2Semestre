package org.openjfx.api2semestre.view_macros;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableView;

public class TableMacros {
    
    public static <T> void buildTable (
        TableView<T> tabela,
        ColumnConfig<T,?>[] columns,
        Optional<ChangeListener<Boolean>> applyFilterCallback
    ) {
        for (ColumnConfig<T, ?> column : columns) {
            column.build(tabela, applyFilterCallback);
        }
    }
}
