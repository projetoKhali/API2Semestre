package org.openjfx.api2semestre.classes;

/// Define o tipo de apontamento entre Hora-Extra ou Sobreaviso
public enum AppointmentType {
    Overtime(true, "Hora-Extra"),
    OnNotice(false, "Sobreaviso");

    // The boolean and String value between the parentheses of each of the enum values above
    private boolean booleanValue;
    private String stringValue;

    // Create a new AppointmentType from a string. Exclusively for the list above
    private AppointmentType (boolean booleanValue, String stringValue) {
        this.booleanValue = booleanValue;
        this.stringValue = stringValue;
    }

    // returns the boolean value of the AppointmentType
    public boolean getBooleanValue() {
        return booleanValue;
    }

    // returns the string value of the AppointmentType
    public String getStringValue() {
        return stringValue;
    }

    public static AppointmentType of (boolean booleanValue) {
        return booleanValue ? AppointmentType.Overtime : AppointmentType.OnNotice;
    }
    
}
