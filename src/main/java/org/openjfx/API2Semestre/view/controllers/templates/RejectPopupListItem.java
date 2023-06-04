package org.openjfx.api2semestre.view.controllers.templates;

import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.view.utils.interfaces.Popup;
import org.openjfx.api2semestre.view.utils.pretty_table_cell.PrettyTableCell;
import org.openjfx.api2semestre.view.utils.pretty_table_cell.PrettyTableCellInstruction;
import org.openjfx.api2semestre.view.utils.wrappers.AppointmentWrapper;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class RejectPopupListItem implements Popup<AppointmentWrapper> {

    @FXML private TableView<AppointmentWrapper> tabela;

    @FXML private TableColumn<AppointmentWrapper, String> col_status;
    @FXML private TableColumn<AppointmentWrapper, String> col_squad;
    @FXML private TableColumn<AppointmentWrapper, String> col_tipo;
    @FXML private TableColumn<AppointmentWrapper, String> col_inicio;
    @FXML private TableColumn<AppointmentWrapper, String> col_fim;
    @FXML private TableColumn<AppointmentWrapper, String> col_cliente;
    @FXML private TableColumn<AppointmentWrapper, String> col_projeto;
    @FXML private TableColumn<AppointmentWrapper, String> col_total;

    @FXML private TextField textField;

    @SuppressWarnings("unchecked") @Override public void setSelected(AppointmentWrapper appointmentWrapper) {
        col_status.setCellValueFactory( new PropertyValueFactory<>( "status" ));
        col_status.setCellFactory(column -> {
            return new PrettyTableCell<AppointmentWrapper, String>(new PrettyTableCellInstruction[] {
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Pendente"), new Color(0.97, 1, 0.6, 1)),
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Aprovado"), new Color(0.43, 0.84, 0.47, 1)),
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Rejeitado"), new Color(0.87, 0.43, 0.43, 1))
            });
        });
        col_squad.setCellValueFactory( new PropertyValueFactory<>( "squad" ));
        col_tipo.setCellValueFactory( new PropertyValueFactory<>( "type" ));
        col_inicio.setCellValueFactory( new PropertyValueFactory<>( "startDate" ));
        col_fim.setCellValueFactory( new PropertyValueFactory<>( "endDate" ));
        col_cliente.setCellValueFactory( new PropertyValueFactory<>( "client" ));
        col_projeto.setCellValueFactory( new PropertyValueFactory<>( "project" ));
        col_total.setCellValueFactory( new PropertyValueFactory<>( "total" ));

        tabela.setItems(FXCollections.observableArrayList(List.of(appointmentWrapper)));
    }

    @Override public AppointmentWrapper getSelected() { return tabela.getItems().get(0); }
    @Override public TableView<AppointmentWrapper> getTable() { return tabela; }

    public String getFeedback () { return textField.getText(); }

}
