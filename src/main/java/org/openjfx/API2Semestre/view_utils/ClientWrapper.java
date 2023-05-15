package org.openjfx.api2semestre.view_utils;

import org.openjfx.api2semestre.data.Client;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ClientWrapper implements HasSelectedProperty {

    private Client cliente;

    public Client getAppointment() {
        return cliente;
    }
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public ClientWrapper (Client cliente) {
        this.cliente = cliente;
    }
    public String getRazaoSocial() { return cliente.getRazaoSocial(); }
    public String getCNPJ() { return cliente.getCNPJ(); }
    
    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

    @Override
    public boolean getSelected() {
        return selected.get();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

}