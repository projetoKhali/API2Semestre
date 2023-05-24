package org.openjfx.api2semestre.view.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.openjfx.api2semestre.appointment.Appointment;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

public class ChartGenerator {

    private class ChartData {

        XYChart.Series<Number, Number> series;
        NumberAxis xAxis;
        NumberAxis yAxis;

        public ChartData(XYChart.Series<Number, Number> series, NumberAxis xAxis, NumberAxis yAxis) {
            this.series = series;
            this.xAxis = xAxis;
            this.yAxis = yAxis;
        }

        @SuppressWarnings("unused") public XYChart.Series<Number, Number> getSeries() { return series; }
        @SuppressWarnings("unused") public NumberAxis getxAxis() { return xAxis; }
        @SuppressWarnings("unused") public NumberAxis getyAxis() { return yAxis; }
    }
    
    private ChartData emptyChart (
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

        return new ChartData(series, xAxis, yAxis);

    }

    public void hourIntersectionCountGraph (Appointment[] appointments) {

        NumberAxis xAxis = new NumberAxis(0, 1440, 60);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override public Number fromString(String string) { return null; }
            @Override public String toString (Number object) {
                int minutes = object.intValue();
                return String.format("%02d:%02d", (minutes / 60) % 24, minutes % 60);
            }
        });

        ChartData chart = emptyChart(
            "Frequência de Apontamento por Hora do Dia",
            Optional.of(xAxis),
            Optional.empty()
        );

        XYChart.Series<Number, Number> series = chart.series;
        NumberAxis yAxis = chart.yAxis;

        // Add the data points to the series
        long start = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2023-01-02 00:00:00").getTime();

        final LocalDate defaultDate = LocalDate.of(2023, 1, 1);

        int maxIntersectionCount = 0;

        // Convert the timestamps to time values in minutes
        for (long time = start; time <= end; time += 60000) {
            
            double position = (double) (time - start) / (end - start) * 24 * 60;

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
                    if (time + (i * 86400000) < aptStart && time + (i * 86400000) > aptEnd) continue;
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

    }
}