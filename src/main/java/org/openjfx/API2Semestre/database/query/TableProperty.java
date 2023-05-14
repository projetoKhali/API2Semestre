package org.openjfx.api2semestre.database.query;

public enum TableProperty {

    // universal property - All tables have this!
    Id("id"),

    // Appointments table
    Requester("requester"),
    Type("tipo"),
    StartDate("hora_inicio"),
    EndDate("hora_fim"),
    Squad("cr_id"),
    Client("cliente"),
    Project("projeto"),
    Justification("justificativa"),
    Status("aprovacao"),
    Feedback("feedback"),


    // Users table
    Nome("nome"),
    Email("email"),
    Senha("senha"),
    Matricula("matricula"),

    // Results Center table
    Sigla("sigla"),
    Codigo("codigo"),

    // Client table
    RazaoSocial("razao_social"),
    CNPJ("cnpj");

    // IntervalFees table


    // TableProperty variable and constructor
    private String stringValue;
    private TableProperty (String stringValue) { this.stringValue = stringValue; }

    // Getter: raw string value
    public String getStringValue() { return stringValue; }

    // Getter: string value formatted for "SELECT where _ = ?"
    public String getStringValueWhere() { return stringValue + " = ?"; }

}
