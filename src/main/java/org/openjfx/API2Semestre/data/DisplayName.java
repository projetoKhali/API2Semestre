package org.openjfx.api2semestre.data;

public class DisplayName implements HasDisplayName {
    private String name;
    public DisplayName (String name) { this.name = name; }
    @Override public String getName() { return name; }    
}
