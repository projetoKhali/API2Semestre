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

import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
    
    private static  ChartData emptyChart (
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
        for (long timeAtPosition = startChart; timeAtPosition <= endChart; timeAtPosition += 60000) {
            
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
    @SuppressWarnings("unused") private static class ChartDataCategory {
        LineChart<String, Number> chart;
        XYChart.Series<String, Number> series;
        CategoryAxis xAxis;
        NumberAxis yAxis;
        public ChartDataCategory(
            LineChart<String, Number> chart,
            XYChart.Series<String, Number> series,
            CategoryAxis xAxis,
            NumberAxis yAxis
        ) {
            this.chart = chart;
            this.series = series;
            this.xAxis = xAxis;
            this.yAxis = yAxis;
        }
    }
    private static ChartDataCategory emptyChartCategory (
        String title,
        Optional<CategoryAxis> xAxisOptional,
        Optional<NumberAxis> yAxisOptional
    
    ) {
        // Create the x-axis representing time
        CategoryAxis xAxis = xAxisOptional.isPresent() ? xAxisOptional.get() : new CategoryAxis();

        // Create the y-axis representing integers
        NumberAxis yAxis = yAxisOptional.isPresent() ? yAxisOptional.get() : new NumberAxis();

        // Create the line chart
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        // Set the title of the chart
        lineChart.setTitle(title);

        // Create a series to hold the data points
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Add the series to the line chart
        lineChart.getData().add(series);

        // Remove os circulos dos pontos da linha
        lineChart.setCreateSymbols(false);

        // Remove a legenda
        lineChart.setLegendVisible(false);

        return new ChartDataCategory(lineChart, series, xAxis, yAxis);

    }
    public static LineChart<String, Number> weekIntersectionCountGraph (Appointment[] appointments) {

        // Cria um eixo de categorias para os dias da semana
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.observableArrayList(
                "seg", "ter", "qua", "qui", "sex", "sab", "dom"));

        ChartDataCategory chartData = emptyChartCategory(
            "Volume de Horas por dia da semana",
            Optional.of(xAxis),
            Optional.empty()
        );

        LineChart<String, Number> lineChart = chartData.chart;
        XYChart.Series<String, Number> series = chartData.series;
        NumberAxis yAxis = chartData.yAxis;

        // Add the data points to the series
        // long startChart = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        // long endChart = Timestamp.valueOf("2023-01-02 00:00:00").getTime();

        final LocalDate defaultDate = LocalDate.of(2023, 1, 1);

        int maxTotalHours = 0;

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            String dayOfWeekName = dayOfWeek.toString();
            int totalHours = 0;

            for (Appointment apt : appointments) {
                LocalDateTime aptStartDateTime = apt.getStart().toLocalDateTime();
                LocalDateTime aptEndDateTime = apt.getEnd().toLocalDateTime();

                LocalDateTime currentDate = aptStartDateTime;
                while (!currentDate.isAfter(aptEndDateTime)) {
                    if (currentDate.getDayOfWeek() == dayOfWeek) {
                        totalHours++;
                    }
                    currentDate = currentDate.plus(1, ChronoUnit.HOURS);
                }
            }


            // Add the data point to the series
            series.getData().add(new XYChart.Data<>(dayOfWeek.toString(), totalHours));
            if (totalHours > maxTotalHours) maxTotalHours = totalHours;

        }

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);        
        yAxis.setUpperBound(maxTotalHours + 1);
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
        Map<LocalDate, Double> hoursPerDay = new HashMap<>();

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

        }

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);        
        yAxis.setUpperBound(maxIntersectionCount + 1);
        yAxis.setTickUnit(1.0);
        yAxis.setMinorTickVisible(false);

        return lineChart;
    }
    
    private static double calculateDurationInHours(LocalDateTime start, LocalDateTime end) {
        long minutes = start.until(end, ChronoUnit.MINUTES);
        return minutes / 60.0;
    }
}