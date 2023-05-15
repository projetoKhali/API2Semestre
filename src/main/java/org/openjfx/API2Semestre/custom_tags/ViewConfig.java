package org.openjfx.api2semestre.custom_tags;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Group;

public class ViewConfig extends Group implements Initializable {
    private StringProperty name = new SimpleStringProperty();
    private ListProperty<Permission> permissions = new SimpleListProperty<>(FXCollections.observableArrayList());
    
    public ViewConfig (){
        super();
        // System.out.println("new ViewConfig | " + name.get());

        // for (Permission p: permissions) {
        //     System.out.println(p.getValue());
        // }
    }

    // Code to request the ViewConfig of a view.fxml
    // FXMLLoader loader = new FXMLLoader(App.getFXML("myView.fxml"));
    // Parent root = loader.load();
    // TextField myTextField = (TextField) root.lookup(".ViewConfig");
    

    public String getName() {
        return name.get();
    }
    
    public void setName(String name) {
        this.name.set(name);
    }

    public ObservableList<Permission> getPermissions() {
        return permissions.get();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // System.out.println("initialize ViewConfig | " + name.get());
        
    }
}
