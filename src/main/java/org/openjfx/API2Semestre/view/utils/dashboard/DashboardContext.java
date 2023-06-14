package org.openjfx.api2semestre.view.utils.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.authentication.Permission;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.data.HasDisplayName;
import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.database.QueryLibs;

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

    public Appointment[] loadData(User currentUser, Optional<java.sql.Connection> connectionOptional) {
        switch (profile) {
            case Administrator: 
            Appointment[] r1 =  QueryLibs.selectAllAppointments(connectionOptional);
            // System.out.println("DashboardContext.loadData() -- " + profile.getName() + " | " + r1.length);
            return r1;
            case Colaborador: 
            Appointment[] r2 =  QueryLibs.selectAppointmentsByUser(
                currentUser.getId(),
                connectionOptional
            );
            // System.out.println("DashboardContext.loadData() -- " + profile.getName() + " | " + r2.length);
            return r2;
            case Gestor:
                List<Appointment> appointmentsOfCRsManagedBy = new ArrayList<Appointment>();
                for (ResultCenter resultCenter : QueryLibs.selectResultCentersManagedBy(
                    currentUser.getId(),
                    connectionOptional
                )) {
                    appointmentsOfCRsManagedBy.addAll(
                        Arrays.asList(QueryLibs.selectAppointmentsOfResultCenter(
                            resultCenter.getId(),
                            connectionOptional
                        ))
                    );
                }
                
                Appointment[] r3 =  appointmentsOfCRsManagedBy.toArray(Appointment[]::new);
                // System.out.println("DashboardContext.loadData() -- " + profile.getName() + " | " + r3.length);
                return r3;
            default: return new Appointment[0];
        }
    }

    public Profile getProfile() { return profile; }
    public FilterField[] getFields() { return fields; }
    @Override public String getName() { return profile.getName(); }
}
