package org.openjfx.api2semestre.database.query;

public enum TableProperty {

    // Universal property - All tables have this!
    Id("id"),
    UserId("usr_id"),

    // Appointments tables
    Apt_UserName("requester"),
    Apt_UserRegistration("matricula"),
    Apt_Type("tipo"),
    Apt_StartDate("hora_inicio"),
    Apt_EndDate("hora_fim"),
    Apt_ResultCenterId("cr_id"),
    Apt_ResultCenterName("centro_nome"),
    Apt_ClientId("clt_id"),
    Apt_ClientName("cliente_nome"),
    Apt_Project("projeto"),
    Apt_Justification("justificativa"),
    Apt_Status("aprovacao"),
    Apt_Feedback("feedback"),
    
    // Users table
    Usr_Name("nome"),
    Usr_Email("email"),
    Usr_Password("senha"),
    Usr_Profile("perfil"),
    Usr_Registration("matricula"),

    // ResultCenters table
    CR_Sigla("sigla"),
    CR_Codigo("codigo"),
    CR_NomeGestor("gestor_nome"),

    // Clients table
    Clt_RazaoSocial("razao_social"),
    Clt_CNPJ("cnpj"),    
    ;

    // TableProperty variable and constructor
    private String stringValue;
    private TableProperty (String stringValue) { this.stringValue = stringValue; }

    // Getter: raw string value
    public String getStringValue() { return stringValue; }

    // Getter: string value formatted for "SELECT where _ = ?"
    public String getStringValueWhere() { return stringValue + " = ?"; }

}
