package org.openjfx.api2semestre.database.query;

public enum TableProperty {

    // Universal property - All tables have this!
    Id("id"),

    // Appointments table
    Requester("requester"),
    Type("tipo"),
    StartDate("hora_inicio"),
    EndDate("hora_fim"),
    ResultCenter("cr_id"),
    Client("cliente"),
    Project("projeto"),
    Justification("justificativa"),
    Status("aprovacao"),
    Feedback("feedback"),
    
    // Users table
    Name("nome"),
    Email("email"),
    Password("senha"),
    Profile("perfil"),
    Registration("matricula"),

    // ResultCenters table
    Sigla("sigla"),
    Codigo("codigo"),
    User("usr_id"),
    NomeGestor("gestor_nome"),

    // Clients table
    RazaoSocial("razao_social"),
    CNPJ("cnpj"),

    // ResultCenter/User relationship
    UserId("usr_id"),
    CrId("cr_id")
    // TODO: IntervalFees table
    
    ;

    // TableProperty variable and constructor
    private String stringValue;
    private TableProperty (String stringValue) { this.stringValue = stringValue; }

    // Getter: raw string value
    public String getStringValue() { return stringValue; }

    // Getter: string value formatted for "SELECT where _ = ?"
    public String getStringValueWhere() { return stringValue + " = ?"; }

}
