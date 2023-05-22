package org.openjfx.api2semestre.view.utils.wrappers;

import javafx.beans.property.BooleanProperty;

public interface HasSelectedProperty {
    public BooleanProperty selectedProperty();
    public boolean getSelected();
    public void setSelected(boolean selected);

}
