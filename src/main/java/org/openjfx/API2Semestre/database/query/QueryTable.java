package org.openjfx.api2semestre.database.query;

public enum QueryTable {

    Appointment("public.apontamento"),
    ViewAppointment("vw_apontamento"),

    User("public.usuario"),
    // ViewUser(""),

    ResultCenter("public.centro_resultado"),
    ViewResultCenter("vw_centro_resultado"),

    // TODO: Client,

    // TODO: IntervalFee,

    Member("public.membro_cr");

    private String stringValue;
    private QueryTable (String stringValue) { this.stringValue = stringValue; }
    public String getStringValue() { return stringValue; }
}
