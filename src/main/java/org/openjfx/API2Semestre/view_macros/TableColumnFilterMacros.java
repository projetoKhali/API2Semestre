package org.openjfx.api2semestre.view_macros;

import javafx.scene.control.TextField;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public final class TableColumnFilterMacros {
    
    /// Given a Table of S and the given TableColumn of S and String:
    /// Replaces the column's header with an "select / deselect all" TextField.
    /// Binds the neccessary callbacks to each row's TextFieldes as well
    public static final <T> void setTextFieldHeader (
        TableColumn<T, String> column,
        String titleb,
        BooleanProperty enableFilter
    ) {
        final String title = "|" + titleb + "|";
        // Define the onClick function for the column header
        column.setText(null);
        Label defaultColumnTitleLabel = new Label(title);
        column.setGraphic(defaultColumnTitleLabel);
        defaultColumnTitleLabel.setMaxWidth(Double.MAX_VALUE);
        defaultColumnTitleLabel.setOnMouseClicked(clickEvent -> {
            if (clickEvent.getClickCount() == 1) {
                // Create a TextField and set it as the header graphic
                TextField textField = new TextField();
                textField.setText(title);
                column.setGraphic(textField);

                // Focus the TextField and select all its contents
                textField.requestFocus();
                textField.selectAll();

                textField.onInputMethodTextChangedProperty().set(
                    editEvent -> {
                        enableFilter.set(textField.getText().length() > 0);
                        System.out.println("FilterMacros -- title: " + title + " | filter: " + enableFilter);
                    }
                );

                // When the user presses Enter set the Label's text to the TextField value
                textField.setOnAction(actionEvent -> {
                    column.setText(null);
                    column.setGraphic(defaultColumnTitleLabel);
                    defaultColumnTitleLabel.setText(textField.getText());
                    enableFilter.set(false);
                    System.out.println("FilterMacros -- title: " + title + " | filter: " + enableFilter);
                });

                // When the TextField loses focus, set the column header text to its previous value
                // but only if TextField doesn't contain text
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    String currentTextFieldText = textField.getText();
                    if (!newValue && (currentTextFieldText.length() == 0 || currentTextFieldText.equals(title))) {
                        column.setGraphic(defaultColumnTitleLabel);
                        defaultColumnTitleLabel.setText(title);
                        enableFilter.set(false);
                        System.out.println("FilterMacros -- title: " + title + " | filter: " + enableFilter);
                    }
                });
            }
        });
    }


}
