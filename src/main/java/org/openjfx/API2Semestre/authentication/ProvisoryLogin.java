package org.openjfx.api2semestre.authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.views_manager.View;
import org.openjfx.api2semestre.views_manager.ViewsManager;

public class ProvisoryLogin {

    public static List<String> parseSquads (String inputString) throws Exception {
        List<String> result = Arrays.asList(inputString.split(",")).stream().map(squad -> squad.trim()).collect(Collectors.toList());
        if (!result.isEmpty()) return result;
        return List.of();
    }

    public static String handleViews () throws Exception {
        final User user = Authentication.getCurrentUser();

        Permission[] userPermissions = getPermissions(user);

        List<View> userViews = new ArrayList<>();
        View firstView = null;
        for (View view : View.VIEWS) {
            if (userViews.stream().filter(
                repeatedView -> repeatedView.getUniqueID() == view.getUniqueID()
            ).collect(Collectors.toList()).size() > 0) continue;
            if (!view.userHasAccess(userPermissions)) continue;
            userViews.add(view);
            if (firstView == null) firstView = view;
        }

        if (userViews.isEmpty() || firstView == null) throw new Exception("ProvisoryLogin.handleViews() -- Error: User has no permissions!");

        ViewsManager.setViews(userViews.toArray(new View[0]));
        return firstView.getFxmlFileName();

    }

    private static Permission[] getPermissions (User u) {
        List<Permission> permissions = new ArrayList<>();
        if (u.getPerfil() == Profile.Administrator) permissions.add(Permission.FullAccess);
        if (QueryLibs.selectResultCentersManagedBy(u.getId()).length > 0) permissions.add(Permission.Validate);
        if (QueryLibs.selectResultCentersOfMember(u.getId()).length > 0) permissions.add(Permission.Appoint);
        return permissions.toArray(new Permission[0]);
    }
}