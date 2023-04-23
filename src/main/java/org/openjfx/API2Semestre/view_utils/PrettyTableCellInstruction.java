package org.openjfx.api2semestre.view_utils;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class PrettyTableCellInstruction <S, T> {
    private Optional<T> expectedValue;
    private Background backgroundColor;

    public PrettyTableCellInstruction (Optional<T> expectedValue, Color background) {
        this.expectedValue = expectedValue;
        this.backgroundColor = new Background(
            new BackgroundFill(
                background,
                new CornerRadii(16), new Insets(4, 4, 4, 4)
            )
        );
    }
    
    public boolean apply (TableCell<S, T> cell, T item, boolean empty) {
        if (empty && expectedValue.isPresent()) return false;
        if (!item.equals(expectedValue.get())) return false;

        cell.setBackground(backgroundColor);

        return true;
    }
}
