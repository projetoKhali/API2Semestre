package org.openjfx.api2semestre.view.manager;

import org.openjfx.api2semestre.authentication.Permission;

public enum View {

    Appointments(0, "Apontamentos", "views/appointments", new Permission[] {
        Permission.Appoint
    }),
    AppointmentsADM(1, "Apontamentos (Adm)", "views/listagemAdm", new Permission[] {
        Permission.FullAccess
    }),
    Approvals(2, "Aprovação", "views/approvals", new Permission[] {
        Permission.Validate
    }),
    Users(3, "Usuários", "views/users", new Permission[] {
        Permission.Register
    }),
    ResultCenters(4, "Centros de Resultado", "views/resultCenters", new Permission[] {
        Permission.Register
    }),
    Clients(5, "Clientes", "views/clients", new Permission[] {
        Permission.Register
    }),
    Report(6, "Relatório", "views/report", new Permission[] {
        Permission.Report
    }),
    Parametrization(7, "Parametrização", "views/parametrization", new Permission[] {
        Permission.FullAccess
    }),
    Dashboard(8, "Dashboard", "views/dashboard", new Permission[] {}),
    ResetPassword(9, "Alterar senha", "views/resetPassword", new Permission[] {}),
    ;

    public static final View[] VIEWS = values();

    private int uniqueID;
    private String displayName;
    private String fxmlFile;
    private Permission[] requiredPermissions;

    private View (int uniqueID, String displayName, String fxmlFileName, Permission[] requiredPermissions) {
        this.uniqueID = uniqueID;
        this.displayName = displayName;
        this.fxmlFile = fxmlFileName;
        this.requiredPermissions = requiredPermissions;
    }

    public int getUniqueID() { return uniqueID; }
    public String getDisplayName() { return displayName; }
    public String getFxmlFileName() { return fxmlFile; }
    public Permission[] getRequiredPermissions() { return requiredPermissions; }

    public boolean userHasAccess (Permission[] permissions) {
        for (Permission requiredPermission : requiredPermissions) {
            boolean hasThisPermission = false;
            for (Permission permissionOwned : permissions) {
                if (permissionOwned != requiredPermission) continue;
                hasThisPermission = true;
                break;
            }
            if (!hasThisPermission) return false;
        }
        return true;
    }

}
