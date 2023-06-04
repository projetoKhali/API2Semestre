package org.openjfx.api2semestre.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.appointment.VwAppointment;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.utils.DateConverter;

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

    // Lista para teste
    
    // ReportInterval[] reportsIntervals = new ReportInterval[]{
    //     new ReportInterval(1, 1601, DateConverter.stringToTimestamp("2023-05-04 15:00:30"), DateConverter.stringToTimestamp("2023-05-04 16:00:30")),
    //     new ReportInterval(2, 1601, DateConverter.stringToTimestamp("2023-05-03 15:00:30"), DateConverter.stringToTimestamp("2023-05-04 16:00:30")),
    //     new ReportInterval(3, 1601, DateConverter.stringToTimestamp("2023-05-02 15:00:30"), DateConverter.stringToTimestamp("2023-05-02 16:00:30")),
    //     new ReportInterval(4, 1601, DateConverter.stringToTimestamp("2023-05-01 15:00:30"), DateConverter.stringToTimestamp("2023-05-01 16:00:30")),
    //     new ReportInterval(5, 1601, DateConverter.stringToTimestamp("2023-04-28 15:00:30"), DateConverter.stringToTimestamp("2023-04-28 16:00:30"))
    // };

    // List<ReportInterval> listReportIntervals = Arrays.asList(reportsIntervals);

    // inserir condições do filtro
    public static void exporterCSV(List<ReportInterval> data, Boolean[] camposBoolean, String LocalFile, Timestamp data_inicio, Timestamp data_fim) {
    // public static void exporterCSV(List<ReportInterval> data, Boolean[] camposBoolean, String LocalFile, Timestamp data_inicio, Timestamp data_fim) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(LocalFile));

            // escreve o cabeçalho do arquivo csv
            List<String> campos = new ArrayList<>();
            if(camposBoolean[0]){campos.add("Matricula");}
            if(camposBoolean[1]){campos.add("Colaborador");}
            if(camposBoolean[2]){campos.add("Verba");}
            if(camposBoolean[3]){campos.add("Hora Início");}
            if(camposBoolean[4]){campos.add("Hora Fim");}
            if(camposBoolean[5]){campos.add("Total");}
            if(camposBoolean[6]){campos.add("Centro Resultado");}
            if(camposBoolean[7]){campos.add("Cliente");}
            if(camposBoolean[8]){campos.add("Projeto");}
            if(camposBoolean[9]){campos.add("Justificativa");}
            writer.writeNext(campos.toArray(String[]::new));

            // escreve registros enquanto houver
            for (ReportInterval reportInterval : data) {

                Optional<VwAppointment> optionalAppointment = QueryLibs.selectAppointmentById(reportInterval.getAppointmmentId());

                if (optionalAppointment.isEmpty()) {
                    System.out.println("Erro ao retornar apontamento");
                    continue;
                }
                VwAppointment appointment = optionalAppointment.get();
                
                List<String> dados = new ArrayList<>();
                if(!(reportInterval.getStart().after(data_fim) || reportInterval.getEnd().before(data_inicio))){
                    if(camposBoolean[0]){dados.add(appointment.getMatricula());}
                    if(camposBoolean[1]){dados.add(appointment.getRequester());}
                    if(camposBoolean[2]){dados.add(Integer.toString(reportInterval.getVerba()));}
                    if(camposBoolean[3]){dados.add(reportInterval.getStart().toString());}
                    if(camposBoolean[4]){dados.add(reportInterval.getEnd().toString());}
                    if(camposBoolean[5]){dados.add(reportInterval.getTotal());}
                    if(camposBoolean[6]){dados.add(appointment.getCrName());}
                    if(camposBoolean[7]){dados.add(appointment.getClient());}
                    if(camposBoolean[8]){dados.add( appointment.getProject());}
                    if(camposBoolean[9]){dados.add(appointment.getJustification());}
                }
                System.out.print("teste, datafim: " + data_fim);
    
                writer.writeNext(dados.toArray(String[]::new));
            }
            System.out.println("Successful Extraction! - ReportExporter.java");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
