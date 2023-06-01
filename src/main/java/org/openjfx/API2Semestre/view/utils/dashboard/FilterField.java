package org.openjfx.api2semestre.view.utils.dashboard;

import org.openjfx.api2semestre.data.HasDisplayName;

public enum FilterField implements HasDisplayName {
    AppointmentStart ("Início"),
    AppointmentEnd   ("Fim") ,
    AppointmentType  ("Tipo"),
    ResultCenter     ("Centro de Resultado"),
    Project          ("Projeto"),
    Client           ("Cliente"),
    Requester        ("Colaborador"),
    Manager          ("Gestor");

    private String displayName;

    private FilterField (String displayName) {
        this.displayName = displayName;
    }

    @Override public String getName() { return displayName; }

}
