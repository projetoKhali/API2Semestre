package org.openjfx.api2semestre.appointments;
import java.sql.Timestamp;

import org.openjfx.api2semestre.database.Data;

/// Representação de um apontamento de Hora-Extra ou Sobreaviso dentro do sistema java.
/// Instanciado utilizando os dados de um apontamento na tabela .sql como atributos.
/// Utilizado para facilitar a conversação entre os sistemas dentro do código. 
public class VwAppointment extends Data {
    private Integer id;             // Identificação unica do apontamento.
    // private int requester;       // Nome do solicitante do apontamnto (colaborador / gestor que lançou). TODO: mudar para referencia do tipo 'User'.
    private String matricula;
    private String requester;
    private AppointmentType type;   // Tipo de apontamento: Hora-Extra / Sobreaviso.
    private Timestamp startDate;    // Data de início do apontamento.
    private Timestamp endDate;      // Data de término do apontamento.
    private String crId;
    private String crName;           // Nome da crName pela qual o solicitante está prestando serviço nesse apontamento. TODO: mudar para referencia do tipo 'crName'.
    private String client;          // Nome do cliente para qual o solicitante está prestando serviço nesse apontamento. TODO: remover cliente do apontamento pois futuramente consegue ser acessado dentro de "project".
    private String project;         // Nome do projeto para qual o solicitante está prestando serviço.
    private String justification;   // Justificativa fornecida pelo solicitante para prestar esta Hora-Extra ou Sobreaviso.
    private Status status;
    private String feedback;

    private VwAppointment (
        Integer id,
        String matricula,
        String requester,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        String crId,
        String crName,
        String client,
        String project,
        String justification,
        Status status,
        String feedback
    ) {
        this.id = id;
        this.matricula = matricula;
        this.requester = requester;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.crId = crId;
        this.crName = crName;
        this.client = client;
        this.project = project;
        this.justification = justification;
        this.status = status;
        this.feedback = feedback;
    }


    /// crIdia uma nova instancia de apontamento
    public VwAppointment (
        String matricula,
        String requester,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        String crId,
        String crName,
        String client,
        String project,
        String justification
    ) {
        this(
            null,
            matricula,
            requester,
            type,
            startDate,
            endDate,
            crId,
            crName,
            client,
            project,
            justification,
            Status.of(0),
            null
        );
    }

    /// Carrega uma instancia de apontamento existente
    public VwAppointment (
        int id,
        String matricula,
        String requester,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        String crId,
        String crName,
        String client,
        String project,
        String justification,
        int status,
        String feedback
    ) {
        this(
            id,
            matricula,
            requester,
            type,
            startDate,
            endDate,
            crId,
            crName,
            client,
            project,
            justification,
            Status.of(status),
            feedback
        );
    }

    /// Métodos de acesso "GET" das variaveis do apontamento.
    public Integer getId() { return id; }
    public AppointmentType getType() { return type; }
    public String getMatricula() { return matricula; }
    public String getRequester() { return requester; }
    public Timestamp getStartDate() { return startDate; }
    public Timestamp getEndDate() { return endDate; }
    public String getCrId() { return crId; }
    public String getCrName() { return crName; }
    public String getClient() { return client; }
    public String getProject() { return project; }
    public String getJustification() { return justification; }
    public Status getStatus() { return status; }
    public String getFeedback() { return feedback; }

    /// Métodos de acesso "SET" das variaveis do apontamento para modificar os valores
    // public void setId (int id) { this.id = id; }
    // public void setType (AppointmentType type) { this.type = type; }
    // public void setRequester (String requester) { this.requester = requester; }
    // public void setStartDate (Date startDate) { this.startDate = startDate; }
    // public void setEndDate (Date endDate) { this.endDate = endDate; }
    // public void setcrName (String crName) { this.crName = crName; }
    // public void setClient (String client) { this.client = client; }
    // public void setClient (String client) { this.client = client; }
    // public void setClient (String client) { this.client = client; }
    // public void setClient (String client) { this.client = client; }
    // public void setClient (String client) { this.client = client; }
    // public void setProject (String project) { this.project = project; }
    // public void setJustification (String justification) { this.justification = justification; }
    public VwAppointment setStatus(int status) { this.status = Status.of(status); return this; }
    public void setFeedback (String feedback) {this.feedback = feedback; }

    

}