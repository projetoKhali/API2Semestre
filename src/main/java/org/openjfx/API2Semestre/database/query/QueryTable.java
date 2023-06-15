package org.openjfx.api2semestre.database.query;

public enum QueryTable {

    Appointment("public.apontamento"),
    ViewAppointment("vw_apontamento"),

    User("public.usuario"),
    ViewUser("vw_usuario"),

    ResultCenter("public.centro_resultado"),
    ViewResultCenter("vw_centro_resultado"),

    Member("public.membro_cr"),

    Client("public.cliente"),
    ;

    private String stringValue;
    private QueryTable (String stringValue) { this.stringValue = stringValue; }
    public String getStringValue() { return stringValue; }
}
