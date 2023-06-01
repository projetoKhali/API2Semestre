package org.openjfx.api2semestre.view.utils.dashboard;

import org.openjfx.api2semestre.authentication.Permission;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.data.HasDisplayName;

public enum DashboardContext implements HasDisplayName {

    MyContext      (
        Profile.Colaborador,
        Permission.Appoint,
        new FilterField[0]
    ),
    ManagedContext (
        Profile.Gestor,
        Permission.Validate,
        new FilterField[] { FilterField.Requester }
    ),
    FullContext    (
        Profile.Administrator,
        Permission.FullAccess,
        new FilterField[] { FilterField.Requester, FilterField.Manager }
    );

    private Profile profile;
    private Permission permission;
    private FilterField[] fields;

    private DashboardContext (Profile profile, Permission permission, FilterField[] fields) {
        final FilterField[] BASE = new FilterField[] {
            FilterField.AppointmentStart,
            FilterField.AppointmentEnd,
            FilterField.AppointmentType,
            FilterField.ResultCenter,
            FilterField.Project,
            FilterField.Client,
        };

        this.profile = profile;
        this.permission = permission;
        this.fields = new FilterField[BASE.length + fields.length];

        for (int i = 0; i < this.fields.length; i++) {
            if (i < BASE.length) {
                this.fields[i] = BASE[i];
            } else {
                this.fields[i] = fields[i - BASE.length];
            }
        }
    }

    public boolean userHasAccess (Permission[] permissions) {
        for (Permission userPermission : permissions) {
            if (userPermission != this.permission) continue;
            return true;
        }
        return false;
    }

    @Override public String getName() { return profile.getName(); }

}
