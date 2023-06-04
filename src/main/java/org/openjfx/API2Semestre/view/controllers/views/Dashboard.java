package org.openjfx.api2semestre.view.controllers.views;

import java.util.ArrayList;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Permission;
import org.openjfx.api2semestre.view.controllers.templates.DashboardTab;
import org.openjfx.api2semestre.view.utils.dashboard.DashboardContext;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Dashboard {

    @FXML private TabPane tabPane;

    @SuppressWarnings("unused") private DashboardTab[] tabs;

    public void initialize() {
        
        generateTabs();
    }

    private void generateTabs () {
        ArrayList<DashboardTab> tabList = new ArrayList<>();
        Permission[] userPermissions = Permission.getPermissions(Authentication.getCurrentUser());

        for (DashboardContext dashboardContext : DashboardContext.values()) {
            if (!dashboardContext.userHasAccess(userPermissions)) continue;

            FXMLLoader loader = new FXMLLoader(App.getFXML("templates/dashboardTab"));

            try {
                Parent tabTemplateRoot = loader.load();
                DashboardTab dashboardTab = loader.getController();
                dashboardTab.setContext(dashboardContext);

                // Create the tab and set its content
                Tab tab = new Tab();
                tab.setText(dashboardContext.getName());
                tab.setContent(tabTemplateRoot);
                tab.setClosable(false);

                // Add the tab to the TabPane
                tabPane.getTabs().add(tab);

                tabList.add(dashboardTab);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tabs = tabList.toArray(DashboardTab[]::new);
    }
}