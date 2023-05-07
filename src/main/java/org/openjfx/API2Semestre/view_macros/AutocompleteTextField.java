package org.openjfx.api2semestre.view_macros;

import org.openjfx.api2semestre.authentication.User;

import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

public class AutocompleteTextField {

    public static void setupAutocomplete(TextField textField, User[] suggestions) {
        ListView<User> suggestionsList = new ListView<>(FXCollections.observableArrayList(suggestions));
        suggestionsList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNome());
                }
            }
        });
        Popup popup = new Popup();
        popup.getContent().add(suggestionsList);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                popup.hide();
            } else {
                suggestionsList.getItems().clear();
                for (User suggestion : suggestions) {
                    // System.out.println(
                    //     "suggestion(" + suggestion.getNome().toLowerCase() + 
                    //     ") newValue(" + newValue.toLowerCase() +
                    //     ") contains? " + suggestion.getNome().toLowerCase().contains(newValue.toLowerCase())
                    // );
                    if (suggestion.getNome().toLowerCase().contains(newValue.toLowerCase())) {
                        suggestionsList.getItems().add(suggestion);
                    }
                }
                if (!suggestionsList.getItems().isEmpty()) {
                    Bounds bounds = textField.localToScreen(textField.getBoundsInLocal());
                    double popupY = bounds.getMaxY();
                    double cellHeight = suggestionsList.getFixedCellSize();
                    double popupHeight = cellHeight * suggestionsList.getItems().size();
                    suggestionsList.setMaxHeight(popupHeight);
                    suggestionsList.setMinHeight(popupHeight);
                    suggestionsList.setPrefHeight(popupHeight);
                    popup.setHeight(popupHeight);

                    popup.show(textField, bounds.getMinX(), popupY);
                } else {
                    popup.hide();
                }
            }
        });

        suggestionsList.setOnMouseClicked(event -> {
            User selectedUser = suggestionsList.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                textField.setText(selectedUser.getNome());
            }
            popup.hide();
        });
    }
}
