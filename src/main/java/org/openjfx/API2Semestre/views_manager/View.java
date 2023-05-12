package org.openjfx.api2semestre.views_manager;

import org.openjfx.api2semestre.authentication.Permission;

public enum View {

    AppointmentsADM(0, "Apontamentos", "views/listagemAdm", new Permission[] {
        Permission.FullAccess
    }),
    Appointments(0, "Apontamentos", "views/appointments", new Permission[] {
        Permission.Appoint
    }),
    Approvals(1, "Aprovação", "views/approvals", new Permission[] {
        Permission.Validate
    }),
    Report(5, "Relatório", "views/report", new Permission[] {
        Permission.Report
    }),
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
