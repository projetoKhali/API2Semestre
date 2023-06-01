package org.openjfx.api2semestre.view.controllers.views;

import java.util.ArrayList;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Permission;
import org.openjfx.api2semestre.view.utils.dashboard.DashboardContext;
import org.openjfx.api2semestre.view.utils.dashboard.DashboardTab;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class Dashboard {

    @FXML private TabPane tabPane;

    private DashboardTab[] tabs;

    public void initialize() {
        generateTabs();
    }

    private void generateTabs () {
        ArrayList<DashboardTab> tabList = new ArrayList<>();
        Permission[] userPermissions = Permission.getPermissions(Authentication.getCurrentUser());

        for (DashboardContext dashboardContext : DashboardContext.values()) {
            if (!dashboardContext.userHasAccess(userPermissions)) continue;

            Parent tabTemplateRoot = App.loadFXML("templates/dashboardTab");

            // Create the tab and set its content
            Tab tab = new Tab();
            tab.setText(dashboardContext.getName());
            tab.setContent(tabTemplateRoot);

            // Add the tab to the TabPane
            tabPane.getTabs().add(tab);

            tabList.add(new DashboardTab(
                (HBox) tabTemplateRoot.lookup("#hb_filters"),
                (FlowPane) tabTemplateRoot.lookup("#fp_charts")                
            ));
        }

        tabs = tabList.toArray(DashboardTab[]::new);
    }
}
