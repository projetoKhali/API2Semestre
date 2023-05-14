package org.openjfx.api2semestre.view_utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.openjfx.api2semestre.data.Client;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class ClientFilter {

    // public static newDropdownFilter () {
    //     Label aptTypeLabel = new Label("AppointmentType:");
    //     ComboBox<AppointmentType> aptTypeComboBox = new ComboBox<>();
    //     aptTypeComboBox.getItems().addAll(AppointmentType.values());
    //     aptTypeComboBox.getItems().add(0, null); // add an "all" value
    //     aptTypeComboBox.setOnAction(event -> {
    //         AppointmentType selectedAppointmentType = aptTypeComboBox.getValue();
    //         Predicate<Person> filter = (selectedAppointmentType == null) ? apt -> true : apt -> apt.getAppointmentType() == selectedAppointmentType;
    //         tabela.getItems().setAll(aptList.filtered(filter));
    //     });
    //     HBox aptTypeBox = new HBox(aptTypeLabel, aptTypeComboBox);
    //     aptTypeBox.setAlignment(Pos.CENTER_LEFT);
    //     aptTypeBox.setSpacing(5);
    //     aptTypeColumn.setGraphic(aptTypeBox);
    // }
    
    public static List<Client> filterFromView (
        List<Client> cliente,
        Optional<TableColumn<ClientWrapper, String>> col_razao,
        Optional<TableColumn<ClientWrapper, String>> col_cnpj
    ) {
        return apply(
            cliente,
            getValueOfTextField(col_razao),
            getValueOfTextField(col_cnpj)
        );
    }

    private static Optional<String> getValueOfTextField (Optional<TableColumn<ClientWrapper, String>> column) {
        if (!column.isPresent()) return Optional.empty();
        var header = column.get().getGraphic();
        if (header instanceof TextField) return Optional.of(((TextField)header).getText());
        if (header instanceof Label) return Optional.of(((Label)header).getText());
        return Optional.empty();
    }

    public static List<Client> apply (
        List<Client> clientes,
        Optional<String> razao,
        Optional<String> cnpj
    ) {
        return clientes.stream().filter((Client cliente) -> {
            if (razao.isPresent() && !cliente.getRazaoSocial().contains(razao.get())) return false;
            if (cnpj.isPresent() && !cliente.getCNPJ().equals(cnpj.get())) return false;
            return true;
        }).collect(Collectors.toList());

    }
}
