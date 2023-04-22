package org.openjfx.api2semestre.classes;

/// Define o tipo de apontamento entre Hora-Extra ou Sobreaviso
public enum AppointmentType {
    Overtime("Overtime"), // Hora-Extra
    OnNotice("OnNotice"); // Sobreaviso

    // The String value between the parentheses of each of the enum values above
    private String stringValue;

    // Create a new AppointmentType from a string. Exclusively for the list above
    private AppointmentType (String value) {
        this.stringValue = value;
    }

    // returns the string value of the AppointmentType
    public String getStringValue() {
        return stringValue;
    }

}
