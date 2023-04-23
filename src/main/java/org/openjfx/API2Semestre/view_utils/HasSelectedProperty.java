package org.openjfx.api2semestre.view_utils;

import javafx.beans.property.BooleanProperty;

public interface HasSelectedProperty {
    public BooleanProperty selectedProperty();
    public boolean getSelected();
    public void setSelected(boolean selected);

}
