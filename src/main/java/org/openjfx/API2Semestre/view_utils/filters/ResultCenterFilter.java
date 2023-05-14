package org.openjfx.api2semestre.view_utils.filters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.data.ResultCenter;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class ResultCenterFilter {
    
    public static List<ResultCenter> filterFromView (
        List<ResultCenter> resultCenters,
        Optional<TableColumn<ResultCenter, String>> col_nome,
        Optional<TableColumn<ResultCenter, String>> col_sigla,
        Optional<TableColumn<ResultCenter, String>> col_codigo,
        Optional<TableColumn<ResultCenter, String>> col_gestor
    ) {
        return apply(
            resultCenters,
            getValueOfTextField(col_nome),
            getValueOfTextField(col_sigla),
            getValueOfTextField(col_codigo),
            getValueOfTextField(col_gestor)
        );
    }

    private static Optional<String> getValueOfTextField (Optional<TableColumn<ResultCenter, String>> column) {
        if (!column.isPresent()) return Optional.empty();
        var header = column.get().getGraphic();
        if (header instanceof TextField) return Optional.of(((TextField)header).getText());
        if (header instanceof Label) return Optional.of(((Label)header).getText());
        return Optional.empty();
    }

    public static List<ResultCenter> apply (
        List<ResultCenter> resultCenters,
        Optional<String> nome,
        Optional<String> sigla,
        Optional<String> codigo,
        Optional<String> gestor
    ) {
        return resultCenters.stream().filter((ResultCenter resultCenter) -> {
            if (nome.isPresent() && !resultCenter.getNome().toLowerCase().contains(nome.get().toLowerCase())) return false;
            if (sigla.isPresent() && !resultCenter.getSigla().toLowerCase().contains(sigla.get().toLowerCase())) return false;
            if (codigo.isPresent() && !resultCenter.getCodigo().toLowerCase().contains(codigo.get().toLowerCase())) return false;
            if (gestor.isPresent() && !resultCenter.getGestorNome().toLowerCase().contains(gestor.get().toLowerCase())) return false;
            return true;
        }).collect(Collectors.toList());

    }
}