package org.openjfx.api2semestre.view_macros;

import java.util.Optional;

import org.openjfx.api2semestre.view_utils.AppointmentWrapper;
import org.openjfx.api2semestre.view_utils.PrettyTableCell;
import org.openjfx.api2semestre.view_utils.PrettyTableCellInstruction;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

public class ColumnConfigStatus extends ColumnConfig <AppointmentWrapper, String> {
 
    public ColumnConfigStatus(
        TableColumn<AppointmentWrapper, String> column,
        String propertyName,
        String displayName
    ) {
        super(column, propertyName, displayName, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void build (TableView<AppointmentWrapper> tabela, Optional<ChangeListener<Boolean>> applyFilterCallback) {
        super.build(tabela, applyFilterCallback);
        column.setCellFactory(column -> {
            return new PrettyTableCell<AppointmentWrapper, String>(new PrettyTableCellInstruction[] {
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Pendente"), new Color(0.97, 1, 0.6, 1)),
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Aprovado"), new Color(0.43, 0.84, 0.47, 1)),
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Rejeitado"), new Color(0.87, 0.43, 0.43, 1))
            });
        });

    }
}
