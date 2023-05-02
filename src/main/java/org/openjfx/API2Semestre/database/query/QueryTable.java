package org.openjfx.api2semestre.database.query;

public enum QueryTable {
    Appointment("apontamento (hora_inicio, hora_fim, requester, projeto, cliente, tipo, justificativa, cr_id, aprovacao) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    ViewAppointment("vw_apontamento WHERE requester = ?");
    // User,
    // Client,
    // Squad,
    // IntervalFee,

    private String stringValue;
    private QueryTable (String stringValue) { this.stringValue = stringValue; }
    public String getStringValue() { return stringValue; }
}
