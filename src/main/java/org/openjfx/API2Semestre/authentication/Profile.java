package org.openjfx.api2semestre.authentication;

public enum Profile {
    Colaborador(0, "Colaborador"),
    Gestor(1, "Gestor"),
    Administrator(2, "Administrador");

    public static final Profile[] PROFILES = values();

    private int profileLevel;
    private String displayName;

    private Profile (int profileLevel, String displayName) {
        this.profileLevel = profileLevel;
        this.displayName = displayName;
    }

    public int getProfileLevel() {
        return profileLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

}
