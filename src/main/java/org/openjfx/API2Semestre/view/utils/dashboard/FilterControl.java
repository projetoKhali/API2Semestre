package org.openjfx.api2semestre.view.utils.dashboard;

import org.openjfx.api2semestre.appointment.Appointment;

import javafx.scene.control.Control;

public class FilterControl {
    private Control control;
    private FilterPredicate filterPredicate;

    @FunctionalInterface public static interface FilterPredicate {
        boolean test(Appointment appointment);
    }

    public FilterControl (Control control, FilterPredicate filterPredicate) {
        this.control = control;
        this.filterPredicate = filterPredicate;
    }

    public boolean filter (Appointment item) {
        return filterPredicate.test(item);
    }

    public Control getControl() { return control; }

}
