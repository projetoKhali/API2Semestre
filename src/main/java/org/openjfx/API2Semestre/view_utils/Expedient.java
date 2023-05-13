package org.openjfx.api2semestre.view_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;

import org.openjfx.api2semestre.database.SQLConnection;

public class Expedient {
    static LocalTime nightShiftStart = null;
    static LocalTime nightShiftEnd = null;

    public static String get (LocalTime intervalFeeStart, LocalTime intervalFeeEnd) {
        if (nightShiftStart == null || nightShiftEnd == null) loadData();
        if (nightShiftStart.equals(intervalFeeStart) && nightShiftEnd.equals(intervalFeeEnd)) return "Noturno";
        if (nightShiftStart.equals(intervalFeeEnd) && nightShiftEnd.equals(intervalFeeStart)) return "Diurno";
        return "N/A";
    }

    private static void loadData () {
        try {
            Connection connection = SQLConnection.connect();
            ResultSet result = connection.prepareStatement("SELECT * FROM expedient WHERE id = 1").executeQuery();
            result.next();
            nightShiftStart = result.getTime("start").toLocalTime();
            nightShiftEnd = result.getTime("end").toLocalTime();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveData (LocalTime newStart, LocalTime newEnd) {
        try {
            Connection connection = SQLConnection.connect();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO expedient (id, start, end) VALUES (1, ?, ?) ON CONFLICT (id) DO UPDATE SET start = ?, end = ?;");
            statement.setTime(1, Time.valueOf(newStart));
            statement.setTime(2, Time.valueOf(newEnd));
            statement.setTime(3, Time.valueOf(newStart));
            statement.setTime(4, Time.valueOf(newEnd));
            statement.execute();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
