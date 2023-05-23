package org.openjfx.api2semestre.view.controllers.custom_tags;

import java.util.Arrays;
import java.util.LinkedList;

import org.openjfx.api2semestre.authentication.User;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

public class LookupTextField extends TextField {

    private final ObservableList<User> suggestions = FXCollections.observableList(new LinkedList<User>());
    private final ObjectProperty<User> selectedUser;
    private final ObservableList<User> listViewSuggestions;
    private ListView<User> suggestionsListView;

    public LookupTextField (User[] suggestions) { this(new LinkedList<User>(Arrays.asList(suggestions))); }
    public LookupTextField (LinkedList<User> suggestions) {
        this.suggestions.setAll(suggestions);
        this.listViewSuggestions = FXCollections.observableArrayList();
        this.selectedUser = new SimpleObjectProperty<>();

        suggestionsListView = new ListView<>(this.listViewSuggestions);
        suggestionsListView.setCellFactory(param -> new ListCell<>() {
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
        popup.getContent().add(suggestionsListView);

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
                    if (getText().isEmpty()) {
                        updateListViewSuggestions();
                        // System.out.println(suggestionsListView.getItems().size() + " suggestions (focus)");
                        showPopup(popup);
                    }
                    // System.out.println("Textfield focus");
                } else {
                    popup.hide();
                    // System.out.println("Textfield out focus");
                }
            }
        });

        textProperty().addListener((observable, oldValue, newValue) -> {
            User selUser = selectedUser.get();
            if (newValue.isBlank() || (selUser != null && newValue.equals(selUser.getNome()))) {
                setStyle("-fx-text-fill: black;");
            } else {
                selectedUser.set(null);
                setStyle("-fx-text-fill: red;");
            }
            if (focusedProperty().get()) {
                // System.out.println("newvalue NOT empty");
                suggestionsListView.getItems().clear();
                // System.out.println(this.suggestions.length);
                for (User suggestion : this.suggestions) {
                    // System.out.println(
                    //     "suggestion(" + suggestion.getNome().toLowerCase() + 
                    //     ") newValue(" + newValue.toLowerCase() +
                    //     ") contains? " + suggestion.getNome().toLowerCase().contains(newValue.toLowerCase())
                    // );
                    if (suggestion.getNome().toLowerCase().contains(newValue.toLowerCase())) {
                        suggestionsListView.getItems().add(suggestion);
                    }
                };        
                if (!showPopup(popup)) {
                    popup.hide();
                }
            }
        });

        suggestionsListView.setOnMouseClicked(event -> {
            User selectedUser = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                setText(selectedUser.getNome());
                this.selectedUser.set(selectedUser);
                setStyle("-fx-text-fill: black;");
            }
            popup.hide();
        });
    }

    private boolean showPopup (Popup popup) {
        if (suggestionsListView.getItems().isEmpty()) {
            // System.out.println("empty");
            return false;
        }
        // System.out.println(suggestionsListView.getItems().size() + " suggestions");
        Bounds bounds = localToScreen(getBoundsInLocal());
        double popupY = bounds.getMaxY();
        double popupHeight = 24 * suggestionsListView.getItems().size();
        suggestionsListView.setMaxHeight(popupHeight);
        suggestionsListView.setMinHeight(popupHeight);
        suggestionsListView.setPrefHeight(popupHeight);
        suggestionsListView.refresh();
        popup.show(this, bounds.getMinX(), popupY);
        return true;
    }

    private void updateListViewSuggestions() {
        suggestionsListView.getItems().clear();
        for (User user : this.suggestions) {
            suggestionsListView.getItems().add(user);
        }
    }

    public User getSelectedUser() {
        return selectedUser.get();
    }

    public void clear() {
        selectedUser.set(null);
        setText("");
    }

    public ObjectProperty<User> selectedUserProperty() {
        return selectedUser;
    }

    public void addSuggestion (User suggestion) {
        this.suggestions.add(suggestion);
    }

    public void removeSuggestion (User suggestion) {
        this.suggestions.remove(suggestion);
    }


    // public void setOnActionHandler(EventHandler<ActionEvent> handler) {
    //     onActionHandler = handler;
    // }
}
