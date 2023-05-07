package org.openjfx.api2semestre.custom_tags;

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

    private User[] suggestions;
    private final ObjectProperty<User> selectedItem;
    private final ObservableList<User> listViewSuggestions;

    public LookupTextField (User[] suggestions) {
        this.suggestions = suggestions;
        this.listViewSuggestions = FXCollections.observableArrayList();
        this.selectedItem = new SimpleObjectProperty<>();

        ListView<User> suggestionsListView = new ListView<>(this.listViewSuggestions);
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
                        suggestionsListView.getItems().clear();
                        for (User user : getSuggestions()) {
                            suggestionsListView.getItems().add(user);
                        }
                        // System.out.println(suggestionsListView.getItems().size() + " suggestions (focus)");
                        showPopup(popup, suggestionsListView);
                    }
                    // System.out.println("Textfield focus");
                } else {
                    popup.hide();
                    // System.out.println("Textfield out focus");
                }
            }
        });

        textProperty().addListener((observable, oldValue, newValue) -> {
            if (focusedProperty().get()) {
                // System.out.println("newvalue NOT empty");
                suggestionsListView.getItems().clear();
                // System.out.println(getSuggestions().length);
                for (User suggestion : getSuggestions()) {
                    // System.out.println(
                    //     "suggestion(" + suggestion.getNome().toLowerCase() + 
                    //     ") newValue(" + newValue.toLowerCase() +
                    //     ") contains? " + suggestion.getNome().toLowerCase().contains(newValue.toLowerCase())
                    // );
                    if (suggestion.getNome().toLowerCase().contains(newValue.toLowerCase())) {
                        suggestionsListView.getItems().add(suggestion);
                    }
                };        
                if (!showPopup(popup, suggestionsListView)) {
                    popup.hide();
                }
            }
        });

        suggestionsListView.setOnMouseClicked(event -> {
            User selectedUser = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                setText(selectedUser.getNome());
                selectedItem.set(selectedUser);
            }
            popup.hide();
        });
    }

    private boolean showPopup (Popup popup, ListView<User> suggestionsListView) {
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

    public User getSelectedItem() {
        return selectedItem.get();
    }

    public void clear() {
        this.selectedItem.set(null);
        this.setText("");
    }

    public ObjectProperty<User> selectedItemProperty() {
        return selectedItem;
    }

    public void removeSuggestion (User suggestion) {
        User[] suggestions = getSuggestions();
        User[] newSuggestions = new User[suggestions.length - 1];
        int index = 0;
        for (User user : suggestions) {
            if (user.equals(suggestion)) continue;
            if (index == suggestions.length) {
                System.out.println("Khali | LookupTextField.removeSuggestion() -- index out of bounds");
                break;
            }
            newSuggestions[index] = user;
            index++;
        }
        setSuggestions(newSuggestions);
    }

    public User[] getSuggestions() { return suggestions; }
    public void setSuggestions(User[] suggestionsToDisplay) { this.suggestions = suggestionsToDisplay; }

    // public void setOnActionHandler(EventHandler<ActionEvent> handler) {
    //     onActionHandler = handler;
    // }
}
