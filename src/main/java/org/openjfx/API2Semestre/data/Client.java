package org.openjfx.api2semestre.data;

import org.openjfx.api2semestre.database.Data;

public class Client extends Data implements HasDisplayName {
    private Integer id;
    private String razaoSocial;
    private String cnpj;

    public Client (
        int id,
        String razaoSocial,
        String cnpj
    ) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    public Client (
        Integer id,
        String razaoSocial,
        String cnpj
    ) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    public Client (
        String razaoSocial,
        String cnpj
    ) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    /// Função getName para implementar a interface HasDisplayName, permitindo compatibilidade com a LookupTextField
    public String getName() { return getRazaoSocial(); }

    public Integer getId() { return id; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getCNPJ() { return cnpj; }

    public void set(Integer id) { this.id = id; }
    public void setNome(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public void setId(Integer id) { this.id = id; }
}
