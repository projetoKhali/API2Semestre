package org.openjfx.api2semestre.view.macros;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

public class TableMacros {
    
    public static <T> void buildTable (
        TableView<T> tabela,
        ColumnConfig<T,?>[] columns,
        Optional<ChangeListener<Boolean>> applyFilterCallback
    ) {
        for (ColumnConfig<T, ?> column : columns) {
            column.build(tabela, applyFilterCallback);
        }
    }

    public static <S, T> void enableEditableCells(TableColumn<S, T> column, Validator<T> validator, Updater<S, T> updater, Formatter<T> formatter) {

        column.setEditable(true);

        column.setCellFactory(c -> {
            TextFieldTableCell<S, T> cell = new TextFieldTableCell<S, T>(formatter.getConverter()) {
                @Override
                public void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(formatter.format(item, isEditing()));
                    }
                }
            };

            cell.setEditable(true);

            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEditing()) {
                    cell.setEditable(true);
                    cell.startEdit();
                }
            });

            cell.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    cell.commitEdit(cell.getConverter().fromString(formatter.parse(cell.getText())));
                    S item = cell.getTableView().getItems().get(cell.getIndex());
                    updater.update(item, cell.getItem());
                    cell.setEditable(false);
                    cell.setStyle("");
                } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                    cell.cancelEdit();
                    cell.setEditable(false);
                    cell.setStyle("");
                }
            });

            cell.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.ESCAPE)) {
                    cell.setEditable(false);
                    cell.setStyle("");
                }
            });

            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && cell.isEditing()) {
                    cell.setEditable(false);
                    cell.setStyle("");
                }
            });

            return cell;
        });

        column.setOnEditCommit((CellEditEvent<S, T> event) -> {
            T newValue = event.getNewValue();
            if (validator.validate(newValue)) {
                S item = event.getTableView().getItems().get(event.getTablePosition().getRow());
                updater.update(item, newValue);
            } else {
                TableView<S> tableView = event.getTableView();
                tableView.refresh();
            }
        });
    }

    public interface Validator<T> {
        boolean validate(T value);
    }

    public interface Updater<S, T> {
        void update(S item, T value);
    }

    public interface Formatter<T> {
        String format(T value, boolean editing);
        String parse(String text);
        StringConverter<T> getConverter();
    }
        
}
