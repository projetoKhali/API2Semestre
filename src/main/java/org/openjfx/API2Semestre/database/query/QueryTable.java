package org.openjfx.api2semestre.database.query;

import org.openjfx.api2semestre.authentication.User;

public enum QueryTable {
    Appointment("public.apontamento"),
    ViewAppointment("vw_apontamento"),
    User("public.usuario"),
    // ViewUser(""),
    ResultsCenter("public.centro_resultado"),
    // ViewResultsCenter("");
    Client("public.cliente");
    // TODO: User,
    // TODO: Client,
    // TODO: Squad,
    // TODO: IntervalFee,

    private String stringValue;
    private QueryTable (String stringValue) { this.stringValue = stringValue; }
    public String getStringValue() { return stringValue; }
}
