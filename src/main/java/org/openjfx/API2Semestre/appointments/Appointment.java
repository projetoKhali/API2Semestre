package org.openjfx.api2semestre.appointments;
import java.sql.Timestamp;

/// Representação de um apontamento de Hora-Extra ou Sobreaviso dentro do sistema java.
/// Instanciado utilizando os dados de um apontamento na tabela .sql como atributos.
/// Utilizado para facilitar a conversação entre os sistemas dentro do código. 
public class Appointment {
    private Integer id;                 // Identificação unica do apontamento.
    // private int requester;       // Nome do solicitante do apontamnto (colaborador / gestor que lançou). TODO: mudar para referencia do tipo 'User'.
    private String requester;
    private AppointmentType type;   // Tipo de apontamento: Hora-Extra / Sobreaviso.
    private Timestamp startDate;       // Data de início do apontamento.
    private Timestamp endDate;           // Data de término do apontamento.
    private String squad;           // Nome da squad pela qual o solicitante está prestando serviço nesse apontamento. TODO: mudar para referencia do tipo 'Squad'.
    private String client;          // Nome do cliente para qual o solicitante está prestando serviço nesse apontamento. TODO: remover cliente do apontamento pois futuramente consegue ser acessado dentro de "project".
    private String project;         // Nome do projeto para qual o solicitante está prestando serviço.
    private String justification;   // Justificativa fornecida pelo solicitante para prestar esta Hora-Extra ou Sobreaviso.
    private Status status;
    private String feedback;

    private Appointment (
        Integer id,
        String requester,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        String squad,
        String client,
        String project,
        String justification,
        Status status,
        String feedback
    ) {
        this.id = id;
        this.requester = requester;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.squad = squad;
        this.client = client;
        this.project = project;
        this.justification = justification;
        this.status = status;
        this.feedback = feedback;
    }


    /// Cria uma nova instancia de apontamento
    public Appointment (
        String requester,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        String squad,
        String client,
        String project,
        String justification
    ) {
        this(
            null,
            requester,
            type,
            startDate,
            endDate,
            squad,
            client,
            project,
            justification,
            Status.of(0),
            null
        );
    }

    /// Carrega uma instancia de apontamento existente
    public Appointment (
        int id,
        String requester,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        String squad,
        String client,
        String project,
        String justification,
        int status,
        String feedback
    ) {
        this(
            id,
            requester,
            type,
            startDate,
            endDate,
            squad,
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
    public String getRequester() { return requester; }
    public Timestamp getStartDate() { return startDate; }
    public Timestamp getEndDate() { return endDate; }
    public String getSquad() { return squad; }
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
    // public void setSquad (String squad) { this.squad = squad; }
    // public void setClient (String client) { this.client = client; }
    // public void setProject (String project) { this.project = project; }
    // public void setJustification (String justification) { this.justification = justification; }
    public Appointment setStatus(int status) { this.status = Status.of(status); return this; }
    public void setFeedback (String feedback) {this.feedback = feedback; }

}