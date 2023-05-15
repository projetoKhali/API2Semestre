package org.openjfx.api2semestre.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.appointments.VwAppointment;
import org.openjfx.api2semestre.database.QueryLibs;

import com.opencsv.CSVWriter;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ReportExporter {

    // Extração de relatório csv de todos os colaboradores com as horas trabalhadas
    // (matrícula, nome, verba, quantidade de horas, cliente, CR, projeto, justificativa);

    // seleciona local de salvamento
    public static String showSaveDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        // titulo da janela
        fileChooser.setTitle("Salvar relatório");
        // nome padrão do arquivo
        fileChooser.setInitialFileName("relatorio.csv");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }
    
    public static void exporterCSV(List<ReportInterval> data, String LocalFile) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(LocalFile));

            // escreve o cabeçalho do arquivo csv
            writer.writeNext(new String[] {
                "Matricula",
                "Colaborador",
                "Verba",
                "Hora Início",
                "Hora Fim",
                "Total",
                "Centro Resultado",
                "Cliente",
                "Projeto",
                "Justificativa"
            });

            // escreve registros enquanto houver
            for (ReportInterval reportInterval : data) {

                Optional<VwAppointment> optionalAppointment = QueryLibs.selectAppointmentById(reportInterval.getAppointmmentId());

                if (optionalAppointment.isEmpty()) {
                    System.out.println("Erro ao retornar apontamento");
                    continue;
                }
                VwAppointment appointment = optionalAppointment.get();

                writer.writeNext(new String[] {
                        appointment.getMatricula(),
                        appointment.getRequester(),
                        Integer.toString(reportInterval.getVerba()),
                        reportInterval.getStart().toString(),
                        reportInterval.getEnd().toString(),
                        reportInterval.getTotal(),
                        appointment.getCrName(),
                        appointment.getClient(),
                        appointment.getProject(),
                        appointment.getJustification()
                });
            }
            System.out.println("Successful Extraction! - ReportExporter.java");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
