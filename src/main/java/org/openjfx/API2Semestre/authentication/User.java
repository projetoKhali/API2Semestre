package org.openjfx.api2semestre.authentication;

import org.openjfx.api2semestre.data.HasDisplayName;
import org.openjfx.api2semestre.database.Data;

public class User extends Data implements HasDisplayName {

    private Integer id;    
    private String matricula;
    private String nome;
    private Profile perfil;
    private String email;
    private String senha;

    private User (
        Integer id,
        String nome, 
        Profile perfil, 
        String email, 
        String senha,
        String matricula
    ) {
        this.id = id;
        this.nome = nome;
        this.perfil = perfil;
        this.email = email;
        this.senha = senha;
        this.matricula = matricula;
    }
    public User(
        String nome, 
        Profile perfil, 
        String email, 
        String matricula
    ) {
        this(
            null,
            nome,
            perfil,
            email,
            matricula,
            matricula
        );
    }

    public User(
        int id,
        String nome, 
        Profile perfil, 
        String email, 
        String senha,
        String matricula
    ) {
        this(
            Integer.valueOf(id),
            nome,
            perfil,
            email,
            senha,
            matricula
        );
    }

    public Integer getId() { return id; }
    public String getRegistration() { return matricula; }
    public String getName() { return nome; }
    public Profile getProfile() { return perfil; }
    public String getEmail() { return email; }
    public String getPassword() { return senha; }
    
    public void setNome(String nome) { this.nome = nome; }
    public void setPerfil(Profile perfil) { this.perfil = perfil; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
}
