package org.openjfx.api2semestre.database.query;

public enum QueryTable {
    Appointment("public.apontamento"),
    User("public.usuario"),
    ViewAppointment("vw_apontamento");
    // TODO: User,
    // TODO: Client,
    // TODO: Squad,
    // TODO: IntervalFee,

    private String stringValue;
    private QueryTable (String stringValue) { this.stringValue = stringValue; }
    public String getStringValue() { return stringValue; }
}
