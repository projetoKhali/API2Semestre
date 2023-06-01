package org.openjfx.api2semestre.view.utils.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public Appointment[] loadData(User currentUser) {
        switch (profile) {
            case Administrator: return QueryLibs.selectAllAppointments();
            case Colaborador: QueryLibs.selectAppointmentsByUser(currentUser.getId());
            case Gestor:

                // cria uma lista com todas as squads que o gestor faz parte
                ResultCenter[] listResultCenters = QueryLibs.selectAllResultCentersOfUser(currentUser.getId());
            
                List<Appointment> listAppointments = new ArrayList<>();
                List<User> loadedUsers = new ArrayList<>();
        
                // cria uma lista com todos os usuários que estão na squads criadas anteriormente
                for (ResultCenter resultCenter : listResultCenters) {
                    User[] crUsers = QueryLibs.selectAllUsersInResultCenter(resultCenter.getId());
                    loadedUsers.addAll(Arrays.asList(crUsers));
                }
            
                // cria uma lista com todos os apontamentos dos usuários criados anteriormente
                for (User user : loadedUsers) {
                    Appointment[] userAppointments = QueryLibs.selectAppointmentsByUser(user.getId());
                    listAppointments.addAll(Arrays.asList(userAppointments));
                }
            
                // cria um array com todos os apontamentos e reportIntervals
                return listAppointments.toArray(Appointment[]::new);
            default: return new Appointment[0];
        }
    }

    @Override public String getName() { return profile.getName(); }

}
