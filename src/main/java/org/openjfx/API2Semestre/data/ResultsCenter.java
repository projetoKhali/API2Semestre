package org.openjfx.api2semestre.data;

public class ResultsCenter {
    private Integer id;
    private String nome;
    private String sigla;
    private String codigo;

    private ResultsCenter(
        Integer id,
        String nome,
        String sigla,
        String codigo
    ) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.codigo = codigo;
    }

    private ResultsCenter(
        String nome,
        String sigla,
        String codigo
    ) {
        this.nome = nome;
        this.sigla = sigla;
        this.codigo = codigo;
    }

    private ResultsCenter(
        int id,
        String nome,
        String sigla,
        String codigo
    ) {
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        this.codigo = codigo;
    }

    public Integer getId () { return id; }
    public String getNome () { return nome; }
    public String getSigla () { return sigla; }
    public String getCodigo () { return codigo; }

    public void setId(Integer id){ this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setSigla(String sigla){ this.sigla = sigla; }
    public void setCodigo(String codigo){ this.codigo = codigo; }   
}
