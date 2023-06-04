package org.openjfx.api2semestre.data;

import org.openjfx.api2semestre.database.Data;

public class ResultCenter extends Data implements HasDisplayName {
    private Integer id;
    private String name;        // Nome
    private String acronym;     // Sigla
    private String code;        // Código
    private int managerId;      // ID Gestor
    private String managerName; // Nome Gestor

    private ResultCenter(
        Integer id,
        String name,
        String acronym,
        String code,
        int managerId,
        String managerName
    ) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.code = code;
        this.managerId = managerId;
        this.managerName = managerName;
    }

    public ResultCenter(
        String name,
        String acronym,
        String codigo,
        int managerId
    ) {
        this(
            null,
            name,
            acronym,
            codigo,
            managerId,
            null
        );
    }

    public ResultCenter(
        int id,
        String name,
        String acronym,
        String codigo,
        int managerId,
        String managerName
    ) {
        this(
            Integer.valueOf(id),
            name,
            acronym,
            codigo,
            managerId,
            managerName
        );
    }

    public Integer getId () { return id; }
    public String getName () { return name; }
    public String getAcronym () { return acronym; }
    public String getCode () { return code; }
    public Integer getManagerId() { return managerId; }

    public String getManagerName () { return managerName; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAcronym(String acronym) { this.acronym = acronym; }
    public void setCode(String codigo) { this.code = codigo; }   
    public void setManagerId(int managerId) { this.managerId = managerId; }
}