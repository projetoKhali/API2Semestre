package org.openjfx.api2semestre.database.query;

public enum TableProperty {

    // Property universal! Todas as tabelas tem:
    Id("id"),

    // Multiplas tabelas possuem essas:
    Name("nome"),
    UserId("usr_id"),
    ResultCenterId("cr_id"),

    // tabela Appointments
    Apt_UserName("requester"),
    Apt_UserRegistration("matricula"),
    Apt_Type("tipo"),
    Apt_StartDate("hora_inicio"),
    Apt_EndDate("hora_fim"),
    Apt_ResultCenterName("centro_nome"),
    Apt_ClientId("clt_id"),
    Apt_ClientName("cliente_nome"),
    Apt_Project("projeto"),
    Apt_Justification("justificativa"),
    Apt_Status("aprovacao"),
    Apt_Feedback("feedback"),
    
    // tabela Users
    Usr_Email("email"),
    Usr_Password("senha"),
    Usr_Profile("perfil"),
    Usr_Registration("matricula"),

    // tabela ResultCenters
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
