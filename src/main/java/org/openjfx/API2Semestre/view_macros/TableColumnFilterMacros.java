package org.openjfx.api2semestre.view_macros;

import javafx.scene.control.TextField;

import java.util.function.Supplier;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;


public final class TableColumnFilterMacros {
    
    /// Given a Table of S and the given TableColumn of S and String:
    /// Replaces the column's header with an "select / deselect all" TextField.
    /// Binds the neccessary callbacks to each row's TextFieldes as well
    public static final <T> void setTextFieldHeader (
        TableColumn<T, String> column,
        String defaultTitle,
        BooleanProperty enableFilter
    ) {
        // final String defaultTitle = "|" + titleb + "|";
        // Define the onClick function for the column header
        column.setText(null);
        Label defaultColumnTitleLabel = new Label(defaultTitle);
        column.setGraphic(defaultColumnTitleLabel);
        defaultColumnTitleLabel.setMaxWidth(Double.MAX_VALUE);
        defaultColumnTitleLabel.setOnMouseClicked(clickEvent -> {
            if (clickEvent.getClickCount() == 1) {

                // Create a TextField and set it as the header graphic
                TextField textField = new TextField();
                textField.setText(defaultTitle);
                column.setGraphic(textField);

                // Focus the TextField and select all its contents
                textField.requestFocus();
                textField.selectAll();

                Supplier<Boolean> enableFilterCallback = () -> {
                    String currentTextFieldText = textField.getText();
                    return currentTextFieldText.length() > 0 && !currentTextFieldText.equals(defaultTitle);
                };

                // When text inside TextField changes
                textField.onKeyTypedProperty().set(
                    editEvent -> {
                        enableFilter.set(enableFilterCallback.get());
                        // System.out.println("FilterMacros -- title: " + defaultTitle + " | filter: " + enableFilter.get());
                    }
                );

                // When the user presses Enter set the Label's text to the TextField value
                textField.setOnAction(actionEvent -> {
                    column.setText(null);
                    column.setGraphic(defaultColumnTitleLabel);
                    defaultColumnTitleLabel.setText(textField.getText());
                    enableFilter.set(enableFilterCallback.get());
                    // System.out.println("FilterMacros -- title: " + defaultTitle + " | filter: " + enableFilter.get());
                });

                // When the TextField loses focus, set the column header text to its previous value
                // but only if TextField doesn't contain text
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue && !enableFilterCallback.get()) {
                        column.setGraphic(defaultColumnTitleLabel);
                        defaultColumnTitleLabel.setText(defaultTitle);
                        enableFilter.set(false);
                        // System.out.println("FilterMacros -- title: " + defaultTitle + " | filter: " + enableFilter.get());
                    }
                });
            }
        });
    }


}
