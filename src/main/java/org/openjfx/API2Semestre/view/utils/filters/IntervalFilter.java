package org.openjfx.api2semestre.view.utils.filters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.view.utils.wrappers.ReportIntervalWrapper;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class IntervalFilter {

    public static List<ReportIntervalWrapper> filterFromView (
        List<ReportIntervalWrapper> intervals,
        Optional<TableColumn<ReportIntervalWrapper, String>> col_matricula,
        Optional<TableColumn<ReportIntervalWrapper, String>> col_colaborador,
        Optional<TableColumn<ReportIntervalWrapper, String>> col_verba,
        Optional<TableColumn<ReportIntervalWrapper, String>> col_cr,
        Optional<TableColumn<ReportIntervalWrapper, String>> col_cliente,
        Optional<TableColumn<ReportIntervalWrapper, String>> col_projeto
    ) {
        return apply(
            intervals,
            getValueOfTextField(col_matricula),
            getValueOfTextField(col_colaborador),
            getValueOfTextField(col_verba),
            getValueOfTextField(col_cr),
            getValueOfTextField(col_cliente),
            getValueOfTextField(col_projeto)
        );
    }

    private static Optional<String> getValueOfTextField (Optional<TableColumn<ReportIntervalWrapper, String>> column) {
        if (!column.isPresent()) return Optional.empty();
        var header = column.get().getGraphic();
        if (header instanceof TextField) return Optional.of(((TextField)header).getText());
        if (header instanceof Label) return Optional.of(((Label)header).getText());
        return Optional.empty();
    }

    public static List<ReportIntervalWrapper> apply (
        List<ReportIntervalWrapper> intervals,
        Optional<String> matricula,
        Optional<String> colaborador,
        Optional<String> verba,
        Optional<String> cr,
        Optional<String> cliente,
        Optional<String> projeto
    ) {
        return intervals.stream().filter((ReportIntervalWrapper interval) -> {
            if (matricula.isPresent() && !interval.getRequesterRegistration().toLowerCase().contains(matricula.get().toLowerCase())) return false;
            if (colaborador.isPresent() && !interval.getRequesterName().toLowerCase().contains(colaborador.get().toLowerCase())) return false;
            if (verba.isPresent() && !interval.getIntervalFeeCode().toLowerCase().contains(verba.get().toLowerCase())) return false;
            if (cr.isPresent() && !interval.getResultCenterName().toLowerCase().contains(cr.get().toLowerCase())) return false;
            if (cliente.isPresent() && !interval.getClientName().toLowerCase().contains(cliente.get().toLowerCase())) return false;
            if (projeto.isPresent() && !interval.getProjectName().toLowerCase().contains(projeto.get().toLowerCase())) return false;
            return true;
        }).collect(Collectors.toList());

    }
}
