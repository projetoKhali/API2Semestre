package org.openjfx.api2semestre.view.controllers.popups;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.macros.TableMacros.Formatter;


public class ResultCenterEdit implements Popup<ResultCenter> {

    @FXML private TableView<ResultCenter> tabela;

    @FXML private TableColumn<ResultCenter, String> col_nome;
    @FXML private TableColumn<ResultCenter, String> col_sigla;
    @FXML private TableColumn<ResultCenter, String> col_codigo;
    @FXML private TableColumn<ResultCenter, String> col_gestor;

    @Override public void setSelected(ResultCenter resultCenter) {
        col_nome.setCellValueFactory( new PropertyValueFactory<>( "name" ));
        col_sigla.setCellValueFactory( new PropertyValueFactory<>( "acronym" ));
        col_codigo.setCellValueFactory( new PropertyValueFactory<>( "code" ));
        col_gestor.setCellValueFactory( new PropertyValueFactory<>( "managerName" ));

        TableMacros.<ResultCenter, String>enableEditableCells(
            col_nome,
            (String value) -> !value.isBlank(),
            (ResultCenter item, String value) -> item.setName(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );
        TableMacros.<ResultCenter, String>enableEditableCells(
            col_sigla,
            (String value) -> !value.isBlank(),
            (ResultCenter item, String value) -> item.setAcronym(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );
        TableMacros.<ResultCenter, String>enableEditableCells(
            col_codigo,
            (String value) -> !value.isBlank(),
            (ResultCenter item, String value) -> item.setCode(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );
        // TableMacros.<ResultCenter, String>enableEditableCells(
        //     col_gestor,
        //     (String value) -> !value.isBlank(),
        //     (ResultCenter item, String value) -> item.?(value),
        //     Formatter.DEFAULT_STRING_FORMATTER
        // );

        tabela.setItems(FXCollections.observableArrayList(List.of(resultCenter)));
    }

    @Override public ResultCenter getSelected() { return tabela.getItems().get(0); }
    @Override public TableView<ResultCenter> getTable() { return tabela; }

}