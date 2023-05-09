package org.openjfx.api2semestre.authentication;

import java.util.List;

import org.openjfx.api2semestre.database.Data;

public class User extends Data {

    private String nome;   // change to id
    private Profile perfil;
    private String email;
    private String senha;
    private String matricula;

    private List<String> cr_isMemberOf;
    private List<String> cr_manaegdBy;

    // public User(String nome, Profile perfil, String email, String senha, String matricula) {
    //     this.nome = nome;
    //     this.perfil = perfil;
    //     this.email = email;
    //     this.senha = senha;
    //     this.matricula = matricula;
    // }


    public User(
        String nome,
        Profile perfil,
        List<String> cr_isMemberOf,
        List<String> cr_manaegdBy
    ) {
        this.nome = nome;
        this.perfil = perfil;
        this.cr_isMemberOf = cr_isMemberOf;
        this.cr_manaegdBy = cr_manaegdBy;
    }

    public String getNome() { return nome; }
    public Profile getPerfil() { return perfil; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getMatricula() { return matricula; }

    public List<String> getIsMemberOfCRs() { return cr_isMemberOf; }
    public List<String> getManagesCRs() { return cr_manaegdBy; }

    // public void setNome(String nome) { this.nome = nome; }
    // public void setPerfil(Profile perfil) { this.perfil = perfil; }
    // public void setEmail(String email) { this.email = email; }
    // public void setSenha(String senha) { this.senha = senha; }
    // public void setMatricula(String matricula) { this.matricula = matricula; }

}
