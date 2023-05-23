package org.openjfx.api2semestre.view.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Permission;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.database.QueryLibs;

public class ViewsManager {
    private static View[] views;

    public static View[] getViews() {
        return views;
    }

    public static void setViews(View[] views) {
        ViewsManager.views = views;
    }

    public static Optional<String> handleViews () throws Exception {
        final User user = Authentication.getCurrentUser();

        Permission[] userPermissions = getPermissions(user);

        List<View> userViews = new ArrayList<>();
        View firstView = null;
        for (View view : View.VIEWS) {
            if (userViews.stream().filter(
                repeatedView -> repeatedView.getUniqueID() == view.getUniqueID()
            )
            .collect(Collectors.toList()).size() > 0) continue;
            if (!view.userHasAccess(userPermissions)) continue;
            userViews.add(view);
            if (firstView == null) firstView = view;
        }

        if (userViews.isEmpty() || firstView == null) {
            System.out.println("ProvisoryLogin.handleViews() -- Error: User has no permissions!");
            ViewsManager.setViews(new View[0]);
            return Optional.empty();
        }

        ViewsManager.setViews(userViews.toArray(View[]::new));
        return Optional.of(firstView.getFxmlFileName());

    }

    private static Permission[] getPermissions (User u) {
        List<Permission> permissions = new ArrayList<>();
        if (u.getPerfil() == Profile.Administrator) {
            permissions.add(Permission.FullAccess);
            permissions.add(Permission.Register);
            permissions.add(Permission.Report);
        }
        if (QueryLibs.selectResultCentersManagedBy(u.getId()).length > 0) permissions.add(Permission.Validate);
        if (QueryLibs.selectResultCentersOfMember(u.getId()).length > 0) permissions.add(Permission.Appoint);
        return permissions.toArray(Permission[]::new);
    }

}
