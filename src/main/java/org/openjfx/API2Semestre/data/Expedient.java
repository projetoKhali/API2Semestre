package org.openjfx.api2semestre.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;

import org.openjfx.api2semestre.database.SQLConnection;

public class Expedient {
    private static LocalTime nightShiftStart = null;
    private static LocalTime nightShiftEnd = null;
    private static Integer closingDay = null;

    public static Integer getClosingDay() { return closingDay; }
    public static LocalTime getNightShiftStart() { return nightShiftStart; }
    public static LocalTime getNightShiftEnd() { return nightShiftEnd; }

    private static final LocalTime defaultNightShiftStart = LocalTime.of(22, 0, 0);
    private static final LocalTime defaultNightShiftEnd = LocalTime.of(6, 0, 0);
    private static final Integer defaultClosingDay = 25;

    public static String getExpedient (LocalTime intervalFeeStart, LocalTime intervalFeeEnd) {
        if (nightShiftStart == null || nightShiftEnd == null) loadData();
        if (nightShiftStart.equals(intervalFeeStart) && nightShiftEnd.equals(intervalFeeEnd)) return "Noturno";
        if (nightShiftStart.equals(intervalFeeEnd) && nightShiftEnd.equals(intervalFeeStart)) return "Diurno";
        return "N/A";
    }

    public static void loadData () {
        try {
            Connection connection = SQLConnection.connect();
            ResultSet result = connection.prepareStatement("SELECT * FROM parametrization WHERE id = 1").executeQuery();
            if (result.next()) {
                nightShiftStart = result.getTime("night_shift_start").toLocalTime();
                nightShiftEnd = result.getTime("night_shift_end").toLocalTime();
                closingDay = result.getInt("closing_day");
            }
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nightShiftStart == null || nightShiftEnd == null || closingDay == null) saveData(
            nightShiftStart == null ? defaultNightShiftStart : nightShiftStart,
            nightShiftEnd == null ? defaultNightShiftEnd : nightShiftEnd,
            closingDay == null ? defaultClosingDay : closingDay
        );
    }
    
    public static void saveData (LocalTime newStart, LocalTime newEnd, Integer newClosingDay) {
        if (nightShiftStart == newStart && nightShiftEnd == newEnd && closingDay == newClosingDay) return;
        try {
            Connection connection = SQLConnection.connect();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO parametrization (id, night_shift_start, night_shift_end, closing_day) VALUES (1, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET night_shift_start = ?, night_shift_end = ?, closing_day = ?;");
            statement.setTime(1, Time.valueOf(newStart));
            statement.setTime(2, Time.valueOf(newEnd));
            statement.setInt(3, newClosingDay);
            statement.setTime(4, Time.valueOf(newStart));
            statement.setTime(5, Time.valueOf(newEnd));
            statement.setInt(6, newClosingDay);
            nightShiftStart = newStart;
            nightShiftEnd = newEnd;
            closingDay = newClosingDay;
            statement.execute();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
