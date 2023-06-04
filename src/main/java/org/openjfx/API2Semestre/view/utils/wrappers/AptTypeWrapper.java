package org.openjfx.api2semestre.view.utils.wrappers;

import java.util.Optional;

import org.openjfx.api2semestre.appointment.AppointmentType;
import org.openjfx.api2semestre.data.HasDisplayName;

public class AptTypeWrapper implements HasDisplayName {
    private Optional<AppointmentType> typeOptional;

    public AptTypeWrapper(Optional<AppointmentType> typeOptional) {
        this.typeOptional = typeOptional;
    }

    public static final AptTypeWrapper[] all () {
        return new AptTypeWrapper[] {
            new AptTypeWrapper(Optional.empty()),
            new AptTypeWrapper(Optional.of(AppointmentType.Overtime)),
            new AptTypeWrapper(Optional.of(AppointmentType.OnNotice))
        };
    }

    @Override public String getName() {
        return typeOptional.isPresent() ? typeOptional.get().getStringValue() : "Todos";
    }

    public Optional<AppointmentType> getType() { return typeOptional; }
}
