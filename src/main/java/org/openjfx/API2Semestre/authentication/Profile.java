package org.openjfx.api2semestre.authentication;

import org.openjfx.api2semestre.data.HasDisplayName;

public enum Profile implements HasDisplayName {
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

    public static Profile of (int level) {
        for (Profile profile : PROFILES) {
            if (profile.getProfileLevel() == level) {
                return profile;
            }
        }
        throw new IllegalArgumentException("Invalid profile level: " + level);
    }

    public int getProfileLevel() { return profileLevel; }
    public String getName() { return displayName; }
}
