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

    /// Array contendo todas as telas que o usuário logado tem acesso
    private static View[] views;

    /// Retorna as telas do usuário que foram previamente definidas durante o login
    public static View[] getViews() {
        return views;
    }

    /// Cria a array de telas que o usuário logado possui acesso 
    public static Optional<String> handleViews () throws Exception {

        // puxa o usuário logado no momento
        final User user = Authentication.getCurrentUser();

        // puxa as permissões do usuário
        Permission[] userPermissions = getPermissions(user);

        // inicializa uma lista de View
        List<View> userViews = new ArrayList<>();

        // inicializa uma View como primeira view para ser aberta como padrão ao iniciar o programa
        View firstView = null;

        // itera através de todas as views definidas em View.java
        for (View view : View.VIEWS) {

            // cria uma lista temporária dentro do if statement contendo todas as views
            // salvas atualmente em userViews filtrando apenas as que possuam uniqueID
            // igual ao uniqueID da view atual do loop. Caso essa lista não esteja vazia,
            // pula a view atual do loop.
            // Isso impede que sejam adicionadas mais de uma view com o mesmo uniqueID em userViews.
            if (userViews.stream().filter(
                repeatedView -> repeatedView.getUniqueID() == view.getUniqueID()
            ).collect(Collectors.toList()).size() > 0) continue;

            // pula caso o usuário não possua acesso a view atual do loop de acordo com suas permissões
            if (!view.userHasAccess(userPermissions)) continue;

            // uniqueID não repete e usuário possui acesso a view. Adiciona essa view à userViews
            userViews.add(view);

        }

        // Erro e Optional.empty() como retorno caso userViews esteja vazia
        if (userViews.isEmpty()) {
            System.out.println("ViewsManager.handleViews() -- Error: User has no permissions!");
            views = new View[0];
            return Optional.empty();
        }

        // userViews não está vazia, define a primeira view de userViews como firstView
        firstView = userViews.get(0);

        views = userViews.toArray(View[]::new);
        return firstView == null ? Optional.empty() : Optional.of(firstView.getFxmlFileName());

    }

    private static Permission[] getPermissions (User u) {
        List<Permission> permissions = new ArrayList<>();
        if (u.getProfile() == Profile.Administrator) {
            permissions.add(Permission.FullAccess);
            permissions.add(Permission.Register);
            permissions.add(Permission.Report);
        }
        if (QueryLibs.selectResultCentersManagedBy(u.getId()).length > 0) permissions.add(Permission.Validate);
        if (QueryLibs.selectResultCentersOfMember(u.getId()).length > 0) permissions.add(Permission.Appoint);
        return permissions.toArray(Permission[]::new);
    }

}
