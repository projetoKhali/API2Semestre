package org.openjfx.api2semestre.view_utils;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
// import javafx.scene.control.TableColumn;

public class PrettyTableCell<S, T> extends TableCell<S, T> {
    // private final TableColumn<S, T> column;
    private PrettyTableCellInstruction<S, T>[] instructions;

    // public PrettyTableCell(TableColumn<S, T> column, PrettyTableCellInstruction<S, T>[] instructions) {
    public PrettyTableCell(PrettyTableCellInstruction<S, T>[] instructions) {
        // this.column = column;
        this.instructions = instructions;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setAlignment(Pos.CENTER);
        if (empty || item == null) {
            setText(null);
            return;
        }
        setText(item.toString());
        if (instructions == null) return;
        for (var instruction : instructions) {
            if (instruction.apply(this, item, empty)) break;
        }
    }
}
