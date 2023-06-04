package org.openjfx.api2semestre.appointment;
import java.sql.Timestamp;

import org.openjfx.api2semestre.database.Data;

/// Representação de um apontamento de Hora-Extra ou Sobreaviso dentro do sistema java.
/// Instanciado utilizando os dados de um apontamento na tabela .sql como atributos.
/// Utilizado para facilitar a conversação entre os sistemas dentro do código. 
public class Appointment extends Data {
    private Integer id;                     // Identificação única do apontamento.
    private int requester;                  // Identificação única do Usuário que solicitou o Apontamento
    private String requester_registration;  // Matrícula do Usuário
    private String requester_name;          // Nome do Usuário
    private AppointmentType type;           // Tipo de apontamento: Hora-Extra / Sobreaviso.
    private Timestamp startDate;            // Data de início do apontamento.
    private Timestamp endDate;              // Data de término do apontamento.
    private int resultCenterId;             // Identificação unica do Centro de Resultado do Apontamento
    private String resultCenterName;        // Nome do Centro de Resultado
    private int clientId;                   // Identificação unica do Cliente do Apontamento
    private String clientName;              // Nome / Razão Social do Cliente
    private String project;                 // Nome do projeto para qual o solicitante está prestando serviço.
    private String justification;           // Justificativa fornecida pelo solicitante para prestar esta Hora-Extra ou Sobreaviso.
    private Status status;                  // Status atual do apontamento: Pendente, Aprovado, Rejeitado.
    private String feedback;                // Feedback do Gestor referente a aprovação ou rejeição

    /// Construtor privado com todos os parametros para ser chamado a partir dos outros construtores 
    private Appointment (
        Integer id,
        int requester,
        String requester_registration,
        String requester_name,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        int resultCenterId,
        String resultCenterName,
        int clientId,
        String clientName,
        String project,
        String justification,
        Status status,
        String feedback
    ) {
        this.id = id;
        this.requester = requester;
        this.requester_registration = requester_registration;
        this.requester_name = requester_name;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resultCenterId = resultCenterId;
        this.resultCenterName = resultCenterName;
        this.clientId = clientId;
        this.clientName = clientName;
        this.project = project;
        this.justification = justification;
        this.status = status;
        this.feedback = feedback;
    }

    /// Construtor publico para criar uma nova instancia de apontamento. 
    /// Nesse caso não possuímos todas as informações, apenas as de lançamento.
    public Appointment (
        int requester,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        int resultCenterId,
        int clientId,
        String project,
        String justification
    ) {
        this(
            null,                        // Integer id
            requester,                      // int requester
            null,    // String requester_registration
            null,            // String requester_name
            type,                           // AppointmentType type
            startDate,                      // Timestamp startDate
            endDate,                        // Timestamp endDate
            resultCenterId,                 // int resultCenterId
            null,          // int resultCenterName
            clientId,                       // int clientId
            null,                // int clientName
            project,                        // String project
            justification,                  // String justification
            Status.of(0),             // Status status
            null                   // String feedbac
        );
    }

    /// Construtor publico para carregar uma instancia de apontamento existente. 
    /// Nesse caso possuímos todos os dados pois estamos carregando a partid do banco de dados.
    public Appointment (
        int id,
        int requester,
        String requester_registration,
        String requester_name,
        AppointmentType type,
        Timestamp startDate,
        Timestamp endDate,
        int resultCenterId,
        String resultCenterName,
        int clientId,
        String clientName,
        String project,
        String justification,
        int status,
        String feedback
    ) {
        this(
            id,
            requester,
            requester_registration,
            requester_name,
            type,
            startDate,
            endDate,
            resultCenterId,
            resultCenterName,
            clientId,
            clientName,
            project,
            justification,
            Status.of(status),
            feedback
        );
    }

    /// Cria uma cópia exata desse Appointment
    public Appointment copy(){
        return new Appointment(
            id,
            requester,
            requester_registration,
            requester_name,
            type,
            startDate,
            endDate,
            resultCenterId,
            resultCenterName,
            clientId,
            clientName,
            project,
            justification,
            status,
            feedback
        );
    }

/// Getters | Métodos de acesso "GET" das variaveis do apontamento.

    /// Appointment related Getters
    public Integer getId() { return id; }
    public AppointmentType getType() { return type; }
    public Timestamp getStart() { return startDate; }
    public Timestamp getEnd() { return endDate; }
    public String getProject() { return project; }
    public String getJustification() { return justification; }
    public Status getStatus() { return status; }
    public String getFeedback() { return feedback; }

    /// Requester related Getters 
    public int getRequester() { return requester; }
    public String getRequesterRegistration() { return requester_registration; }
    public String getRequesterName() { return requester_name; }
    
    /// ResultCenter related Getters 
    public int getResultCenterId() { return resultCenterId; }
    public String getResultCenterName() { return resultCenterName; }
    
    /// Client related Getters 
    public int getClientId() { return clientId; }
    public String getClientName() { return clientName; }

/// Setters 

    /// Métodos de acesso "SET" das variaveis do apontamento para modificar os valores
    // public void setId (int id) { this.id = id; }
    // public void setType (AppointmentType type) { this.type = type; }
    // public void setRequester (int requester) { this.requester = requester; }
    public void setStart (Timestamp startDate) { this.startDate = startDate; }
    public void setEnd (Timestamp endDate) { this.endDate = endDate; }
    // public void setSquad (int resultCenterId) { this.resultCenterId = resultCenterId; }
    // public void setClient (int ClientId) { this.ClientId = ClientId; }
    // public void setProject (String project) { this.project = project; }
    // public void setJustification (String justification) { this.justification = justification; }
    public Appointment setStatus(int status) { this.status = Status.of(status); return this; }
    public void setFeedback (String feedback) {this.feedback = feedback; }

}