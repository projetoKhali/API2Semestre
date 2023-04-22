package org.openjfx.api2semestre.classes;

// Represents the Appointment's status in the app
public enum Status {

    // The valid Status values:
    Pending (0, "Pendente"),
    Approved(1, "Aprovado"),
    Rejected(2, "Rejeitado");

    // Static array containing all Statuses
    public static final Status[] STATUS = values();

    // The index and String value between the parentheses of each of the enum values above
    private int index;
    private String stringValue;

    // Create a new Status from a string. Exclusively for the list above
    private Status (int index, String value) {
        this.index = index;
        this.stringValue = value;
    }

    // returns the string value of the Status
    public int getIntValue() {
        return index;
    }
    
    // returns the string value of the Status
    public String getStringValue() {
        return stringValue;
    }

    // returns the status of the given index
    public static Status of (int index) {
        if (index >= 0 && index < STATUS.length) { return STATUS[index]; }
        throw new IllegalArgumentException("Invalid Status value: " + index);
    }

}
