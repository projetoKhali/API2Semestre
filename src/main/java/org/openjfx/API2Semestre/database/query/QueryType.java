package org.openjfx.api2semestre.database.query;

public enum QueryType {
    INSERT("INSERT INTO"),
    SELECT("SELECT * FROM"),
    UPDATE("UPDATE"),
    DELETE("DELETE FROM");

    private String stringValue;
    private QueryType (String stringValue) { this.stringValue = stringValue; }
    public String getStringValue() { return stringValue; }
}
