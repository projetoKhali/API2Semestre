package org.openjfx.api2semestre.data;

import org.openjfx.api2semestre.database.Data;

public class ResultCenter extends Data {
    private Integer id;
    private String nome;
    private String sigla;
    private String codigo;
    private int gestorId;

    private ResultCenter(
        Integer id,
        String nome,
        String sigla,
        String codigo,
        int gestorId
    ) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.codigo = codigo;
        this.gestorId = gestorId;
    }

    public ResultCenter(
        String nome,
        String sigla,
        String codigo,
        int gestorId
    ) {
        this(
            null,
            nome,
            sigla,
            codigo,
            gestorId
        );
    }

    public ResultCenter(
        int id,
        String nome,
        String sigla,
        String codigo,
        int gestorId
    ) {
        this(
            Integer.valueOf(id),
            nome,
            sigla,
            codigo,
            gestorId
        );
    }

    public Integer getId () { return id; }
    public String getNome () { return nome; }
    public String getSigla () { return sigla; }
    public String getCodigo () { return codigo; }
    public Integer getGestorId() { return gestorId; }
    
    public void setId(Integer id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setSigla(String sigla) { this.sigla = sigla; }
    public void setCodigo(String codigo) { this.codigo = codigo; }   
    public void setGestorId(int gestorId) { this.gestorId = gestorId; }
}