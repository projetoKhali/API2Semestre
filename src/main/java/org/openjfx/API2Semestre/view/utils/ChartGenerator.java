package org.openjfx.api2semestre.view.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.report.ReportInterval;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;


public class ChartGenerator {

    @SuppressWarnings("unused") private static class ChartData {
        LineChart<Number, Number> chart;
        XYChart.Series<Number, Number> series;
        NumberAxis xAxis;
        NumberAxis yAxis;
        public ChartData(
            LineChart<Number, Number> chart,
            XYChart.Series<Number, Number> series,
            NumberAxis xAxis,
            NumberAxis yAxis
        ) {
            this.chart = chart;
            this.series = series;
            this.xAxis = xAxis;
            this.yAxis = yAxis;
        }
    }
    
    private static ChartData emptyChart (
        String title,
        Optional<NumberAxis> xAxisOptional,
        Optional<NumberAxis> yAxisOptional
    
    ) {
        // Create the x-axis representing time
        NumberAxis xAxis = xAxisOptional.isPresent() ? xAxisOptional.get() : new NumberAxis();

        // Create the y-axis representing integers
        NumberAxis yAxis = yAxisOptional.isPresent() ? yAxisOptional.get() : new NumberAxis();

        // Create the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        // Set the title of the chart
        lineChart.setTitle(title);

        lineChart.setMaxWidth(416);
        lineChart.setPrefWidth(-1);
        lineChart.setMinWidth(-1);
        lineChart.setMaxHeight(336);
        lineChart.setPrefHeight(-1);
        lineChart.setMinHeight(-1);

        // Create a series to hold the data points
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        // Add the series to the line chart
        lineChart.getData().add(series);

        // Remove os circulos dos pontos da linha
        lineChart.setCreateSymbols(false);

        // Remove a legenda
        lineChart.setLegendVisible(false);

        return new ChartData(lineChart, series, xAxis, yAxis);

    }

    public static LineChart<Number, Number> hourIntersectionCountGraph (Appointment[] appointments) {

        NumberAxis xAxis = new NumberAxis(0, 1440, 60);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public Number fromString(String string) { return null; }
            @Override public String toString (Number object) {
                int minutes = object.intValue();
                return String.format("%02d:%02d", (minutes / 60) % 24, minutes % 60);
            }
        });

        ChartData chartData = emptyChart(
            "Frequência de Apontamento por Hora do Dia",
            Optional.of(xAxis),
            Optional.empty()
        );

        LineChart<Number, Number> lineChart = chartData.chart;
        XYChart.Series<Number, Number> series = chartData.series;
        NumberAxis yAxis = chartData.yAxis;

        // Add the data points to the series
        long startChart = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        long endChart = Timestamp.valueOf("2023-01-02 00:00:00").getTime();

        final LocalDate defaultDate = LocalDate.of(2023, 1, 1);

        int maxIntersectionCount = 0;

        // Convert the timestamps to time values in minutes
        for (long timeAtPosition = startChart; timeAtPosition <= endChart; timeAtPosition += 300000) {
            
            double position = (double) (timeAtPosition - startChart) / (endChart - startChart) * 24 * 60;

            // Count the number of intersections for the current time
            int intersectionCount = 0;
            for (Appointment apt : appointments) {

                LocalDateTime aptStartDateTime = apt.getStart().toLocalDateTime();
                LocalDateTime aptEndDateTime = apt.getEnd().toLocalDateTime();   

                int dayCount = aptEndDateTime.toLocalDate().compareTo(aptStartDateTime.toLocalDate());
             
                long aptStart = Timestamp.valueOf(aptStartDateTime.toLocalTime().atDate(
                    defaultDate
                )).getTime();

                long aptEnd = Timestamp.valueOf(aptEndDateTime.toLocalTime().atDate(
                    defaultDate.plusDays(dayCount)
                )).getTime();

                for (int i = 0; i < 1 + dayCount; i++) {
                    if (timeAtPosition + (i * 86400000) <= aptStart || timeAtPosition + (i * 86400000) > aptEnd) continue;
                    intersectionCount++;
                }
            }

            // Add the data point to the series
            series.getData().add(new XYChart.Data<>(position, intersectionCount));
            if (intersectionCount > maxIntersectionCount) maxIntersectionCount = intersectionCount;

        }
            
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);        
        yAxis.setUpperBound(maxIntersectionCount + 1);
        yAxis.setTickUnit(1.0);
        yAxis.setMinorTickVisible(false);

        return lineChart;
    }

    // grafico em barra, com o total de horas para cada verba
    public static BarChart<String, Number> reportIntervalChart(ReportInterval[] reportsInterval){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();

        double hours1601 = 0;
        double hours1602 = 0;
        double hours3000 = 0;
        double hours3001 = 0;
        double hours1809 = 0;
        LocalDateTime start;
        LocalDateTime end;

        for(ReportInterval verba: reportsInterval){
            start = (verba.getStart()).toLocalDateTime();
            end = (verba.getEnd()).toLocalDateTime();
            if(verba.getVerba() == 1601){
                hours1601 += (ChronoUnit.MINUTES.between(start, end))/60.0;
            }
            if(verba.getVerba() == 1602){
                hours1602 += (ChronoUnit.MINUTES.between(start, end))/60.0;
            }
            if(verba.getVerba() == 3000){
                hours3000 += (ChronoUnit.MINUTES.between(start, end))/60.0;
            }
            if(verba.getVerba() == 3001){
                hours3001 += (ChronoUnit.MINUTES.between(start, end))/60.0;
            }
            if(verba.getVerba() == 1809){
                hours1809 += (ChronoUnit.MINUTES.between(start, end))/60.0;
            }
        }

        dataSeries.getData().add(new XYChart.Data<>("1601", hours1601));
        dataSeries.getData().add(new XYChart.Data<>("1602", hours1602));
        dataSeries.getData().add(new XYChart.Data<>("3000", hours3000));
        dataSeries.getData().add(new XYChart.Data<>("3001", hours3001));
        dataSeries.getData().add(new XYChart.Data<>("1809", hours1809));
       
        // Adicione a série de dados ao gráfico de barras
        barChart.getData().add(dataSeries);
        barChart.setTitle("Total de horas por verba");
        barChart.setLegendVisible(false);

        return barChart;
       

    }

    // grafico de rosca, com o status dos apontamentos
    public static PieChart statusAppointmentChart(Appointment[] appointments){
        
        Integer aprovados = 0;
        Integer rejeitados = 0;
        Integer pendentes = 0;
                
        for(Appointment apt: appointments){
            if(apt.getStatus().getStringValue() ==  "Aprovado"){
                aprovados += 1;
            }
            if(apt.getStatus().getStringValue() ==  "Rejeitado"){
                rejeitados += 1;
            }
            if(apt.getStatus().getStringValue() ==  "Pendente"){
                pendentes += 1;
            }
        }
        // Criar os dados do gráfico de pizza
        PieChart.Data slice1 = new PieChart.Data("Aprovado", aprovados);
        PieChart.Data slice2 = new PieChart.Data("Rejeitado", rejeitados);
        PieChart.Data slice3 = new PieChart.Data("Pendente", pendentes);
        
        // Criar o gráfico de pizza e adicionar os dados
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(slice1, slice2, slice3);
        // slice1.getNode().setStyle("-fx-pie-color: #6ED678;");
        // slice2.getNode().setStyle("-fx-pie-color: #DD6E6E;");
        // slice3.getNode().setStyle("-fx-pie-color: #F7FF98;");

        // Estilizar o gráfico para ter um círculo vazio no meio
        pieChart.setClockwise(false);
        pieChart.setLabelLineLength(0);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(90); // Girar o gráfico para ter o buraco no meio
        
        
        
        pieChart.setTitle("Status dos Apontamentos");
        

        return pieChart;
        
   
    }

    
    public static LineChart<Number, Number> weekIntersectionCountGraph (Appointment[] appointments) {

        // Cria um eixo de categorias para os dias da semana
        NumberAxis xAxis = new NumberAxis();
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public Number fromString(String string) { return null; }
            @Override public String toString (Number object) {
                final String[] DIAS_STR = new String[] {"Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom"};
                return DIAS_STR[object.intValue()];
            }
        });

        ChartData chartData = emptyChart(
            "Volume de Horas por dia da semana",
            Optional.of(xAxis),
            Optional.empty()
        );

        LineChart<Number, Number> lineChart = chartData.chart;
        XYChart.Series<Number, Number> series = chartData.series;
        NumberAxis yAxis = chartData.yAxis;

        double maxTotalHours = 0.0;

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            double totalHours = 0.0;

            for (Appointment apt : appointments) {
                LocalDateTime aptStartDateTime = apt.getStart().toLocalDateTime();
                LocalDateTime aptEndDateTime = apt.getEnd().toLocalDateTime();

                LocalDate currentDate = aptStartDateTime.toLocalDate();

                while (!currentDate.isAfter(aptEndDateTime.toLocalDate())) {

                    if (currentDate.getDayOfWeek() == dayOfWeek) {
                        LocalDateTime startOfDay = currentDate.atStartOfDay();
                        LocalDateTime endOfDay = currentDate.plusDays(1).atStartOfDay();
        
                        LocalDateTime intersectionStart = aptStartDateTime.isAfter(startOfDay) ? aptStartDateTime : startOfDay;
                        LocalDateTime intersectionEnd = aptEndDateTime.isBefore(endOfDay) ? aptEndDateTime : endOfDay;
                
                        totalHours += calculateDurationInHours(intersectionStart, intersectionEnd);
                    }

                    currentDate = currentDate.plusDays(1);
                }
            }

            // Add the data point to the series
            series.getData().add(new XYChart.Data<>(dayOfWeek.getValue() % 7, totalHours));
            if (totalHours > maxTotalHours) maxTotalHours = totalHours;

        }

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);        
        xAxis.setUpperBound(6);
        xAxis.setTickUnit(1.0);
        xAxis.setMinorTickVisible(false);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);        
        yAxis.setUpperBound(((Double)maxTotalHours).intValue() + 1);
        yAxis.setTickUnit(1.0);
        yAxis.setMinorTickVisible(false);

        return lineChart;
    }

    public static LineChart<Number, Number> monthIntersectionCountGraph (Appointment[] appointments) {

        NumberAxis xAxis = new NumberAxis(0, 31, 1);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public Number fromString(String string) { return null; }
            @Override public String toString (Number object) {
                int dias = object.intValue();
                return String.valueOf(dias);
            }
        });

        ChartData chartData = emptyChart(
            "Quantidade de horas por dia do mês",
            Optional.of(xAxis),
            Optional.empty()
        );

        LineChart<Number, Number> lineChart = chartData.chart;
        XYChart.Series<Number, Number> series = chartData.series;
        NumberAxis yAxis = chartData.yAxis;

        // Mapa para armazenar as horas trabalhadas por dia
        Map<Integer, Double> hoursPerDay = new HashMap<>();

        // Calcular as horas trabalhadas em cada dia
        for (Appointment apt : appointments) {
            LocalDateTime aptStartDateTime = apt.getStart().toLocalDateTime();
            LocalDateTime aptEndDateTime = apt.getEnd().toLocalDateTime();

            LocalDate currentDate = aptStartDateTime.toLocalDate();

            while (!currentDate.isAfter(aptEndDateTime.toLocalDate())) {
                LocalDateTime startOfDay = currentDate.atStartOfDay();
                LocalDateTime endOfDay = currentDate.plusDays(1).atStartOfDay();

                LocalDateTime intersectionStart = aptStartDateTime.isAfter(startOfDay) ? aptStartDateTime : startOfDay;
                LocalDateTime intersectionEnd = aptEndDateTime.isBefore(endOfDay) ? aptEndDateTime : endOfDay;

                double duration = calculateDurationInHours(intersectionStart, intersectionEnd);

                int currentDayOfMonth = currentDate.getDayOfMonth();

                hoursPerDay.put(currentDayOfMonth, hoursPerDay.getOrDefault(currentDayOfMonth, 0.0) + duration);

                currentDate = currentDate.plusDays(1);
            }
        }

        double maxTotalHours = 0;

        double maxIntersectionCount = 0;

        // Calcular as horas trabalhadas em cada dia
        for (Appointment apt : appointments) {
            LocalDateTime startDateTime = apt.getStart().toLocalDateTime();
            LocalDateTime endDateTime = apt.getEnd().toLocalDateTime();

            LocalDate currentDate = startDateTime.toLocalDate();

            while (!currentDate.isAfter(endDateTime.toLocalDate())) {
                LocalDateTime startOfDay = currentDate.atStartOfDay();
                LocalDateTime endOfDay = currentDate.atTime(23, 59, 59);

                LocalDateTime intersectionStart = startDateTime.isAfter(startOfDay) ? startDateTime : startOfDay;
                LocalDateTime intersectionEnd = endDateTime.isBefore(endOfDay) ? endDateTime : endOfDay;

                double duration = calculateDurationInHours(intersectionStart, intersectionEnd);
                hoursPerDay.put(currentDate, hoursPerDay.getOrDefault(currentDate, 0.0) + duration);

                currentDate = currentDate.plusDays(1);
            }
        }
    
        // Adicionar os pontos de dados à série do gráfico
        for (int day = 1; day <= 31; day++) {
            double hoursWorked = hoursPerDay.getOrDefault(LocalDate.of(2023, 1, day), 0.0);
            series.getData().add(new XYChart.Data<>(day, hoursWorked));
            if (hoursWorked > maxIntersectionCount) maxIntersectionCount = hoursWorked;

            double totalHours = hoursPerDay.getOrDefault(day, 0.0);

            // Add the data point to the series
            series.getData().add(new XYChart.Data<>(day, totalHours));
            if (totalHours > maxTotalHours) maxTotalHours = totalHours;
        }

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);        
        yAxis.setUpperBound(maxTotalHours + 1);
        yAxis.setTickUnit(1.0);
        yAxis.setMinorTickVisible(false);

        return lineChart;
    }
    
    private static double calculateDurationInHours(LocalDateTime start, LocalDateTime end) {
        long minutes = start.until(end, ChronoUnit.MINUTES);
        return minutes / 60.0;
    }
}