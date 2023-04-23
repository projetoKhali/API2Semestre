package org.openjfx.api2semestre.view_macros;

import org.openjfx.api2semestre.view_utils.HasSelectedProperty;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public final class TableColumnFilterMacros {
    
    /// Given a Table of T and the given TableColumn of T and Boolean:
    /// Replaces the column's header with an "select / deselect all" checkbox.
    /// Binds the neccessary callbacks to each row's checkboxes as well
    /// Also disables the Sorting function of the TableColumn
    public static final <T extends HasSelectedProperty> void setCheckBoxHeader (
        TableView<T> table,
        TableColumn<T, Boolean> column
    ) {
        column.setSortable(false);
        CheckBox selectAllCheckbox = new CheckBox();
        selectAllCheckbox.setAlignment(Pos.TOP_RIGHT);
        selectAllCheckbox.setOnAction(event -> {
            boolean newValue = selectAllCheckbox.isSelected();
            for (T aptWrapper : table.getItems()) {
                aptWrapper.setSelected(newValue);
            }
        });

        column.setGraphic(selectAllCheckbox);
        column.setCellFactory(
            new Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>>() {
                public TableCell<T, Boolean> call(TableColumn<T, Boolean> p) {
                    final TableCell<T, Boolean> cell = new TableCell<T, Boolean>() {
                        @Override
                        public void updateItem(final Boolean item, boolean empty) {
                            if (item == null)
                                return;
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                final T apt = getTableView().getItems().get(getIndex());
                                CheckBox checkBox = new CheckBox();
                                checkBox.selectedProperty().bindBidirectional(apt.selectedProperty());
                                checkBox.setOnAction(
                                    new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            boolean unSelectedFlag = false;
                                            for (T item : table.getItems()) {
                                                if (!item.getSelected()) {
                                                    unSelectedFlag = true;
                                                    break;
                                                }
                                            }
                                            if (unSelectedFlag) {
                                                selectAllCheckbox.setSelected(false);
                                            } else {
                                                selectAllCheckbox.setSelected(true);
                                            }
                                        }
                                    }
                                );
                                setGraphic(checkBox);
                            }
                        }
                    };
                    cell.setAlignment(Pos.CENTER);
                    return cell;
                }
            }
        );
    }


}
