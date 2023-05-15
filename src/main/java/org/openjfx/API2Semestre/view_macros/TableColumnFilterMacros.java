package org.openjfx.api2semestre.view_macros;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

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
        Label label = new Label(defaultTitle);
        column.setGraphic(label);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setOnMouseClicked(clickEvent -> {
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
                        // System.out.println("KeyTyped: '" + editEvent.getCharacter() + "'");
                        if (enableFilterCallback.get()) {
                            enableFilter.set(false);
                            enableFilter.set(true);
                        } else {
                            enableFilter.set(false);
                        }
                        column.getGraphic().setStyle("-fx-text-fill: black;");
                    }
                );

                // When a key is pressed 
                textField.onKeyPressedProperty().set(
                    pressEvent -> {
                        KeyCode key = pressEvent.getCode();
                        // System.out.println("KeyPressed: '" + pressEvent.getCharacter() + "' | " + key);
                        if (key != KeyCode.ESCAPE) return;
                        column.setGraphic(label);
                        label.setText(defaultTitle);
                        enableFilter.set(false);
                    }
                );
                // When the user presses Enter set the Label's text to the TextField value
                textField.setOnAction(actionEvent -> {
                    column.setText(null);
                    column.setGraphic(label);
                    label.setText(textField.getText());
                    if (!label.getText().equals(defaultTitle)) {
                        column.getGraphic().setStyle("-fx-text-fill: blue;");
                    } else {
                        column.getGraphic().setStyle("-fx-text-fill: black;");
                    }
                    if (enableFilterCallback.get()) {
                        enableFilter.set(false);
                        enableFilter.set(true);
                    } else {
                        enableFilter.set(false);
                    }
                });

                // When the TextField loses focus, set the column header text to its previous value
                // but only if TextField doesn't contain text
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue && !enableFilterCallback.get()) {
                        column.setGraphic(label);
                        label.setText(defaultTitle);
                        enableFilter.set(false);
                    } 
                    if (!label.getText().equals(defaultTitle)) {
                        column.getGraphic().setStyle("-fx-text-fill: blue;");
                    } else {
                        column.getGraphic().setStyle("-fx-text-fill: black;");
                    }
                });
            }
        });
    }


}
