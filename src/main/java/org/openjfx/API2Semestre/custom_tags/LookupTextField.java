package org.openjfx.api2semestre.custom_tags;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class LookupTextField<T> extends TextField {

    private final ObservableList<T> items;
    private final ObjectProperty<T> selectedItem;
    private final EventHandler<ActionEvent> onActionHandler;

    public LookupTextField (ObservableList<T> items) {
        this.items = items;
        this.selectedItem = new SimpleObjectProperty<>();
        this.onActionHandler = getOnAction();

        setOnAction(event -> {
            if (getSelectedItem() != null) {
                onActionHandler.handle(event);
            }
        });

        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                selectedItem.set(null);
            } else {
                for (T item : items) {
                    if (item.toString().startsWith(newValue)) {
                        selectedItem.set(item);
                        break;
                    }
                }
            }
        });

        selectedItem.addListener((observable, oldValue, newValue) -> {
            setText(newValue != null ? newValue.toString() : "");
        });
    }

    public T getSelectedItem() {
        return selectedItem.get();
    }

    public ObjectProperty<T> selectedItemProperty() {
        return selectedItem;
    }

    // public void setOnActionHandler(EventHandler<ActionEvent> handler) {
    //     onActionHandler = handler;
    // }
}
