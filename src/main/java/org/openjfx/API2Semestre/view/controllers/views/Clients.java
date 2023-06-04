package org.openjfx.api2semestre.view.controllers.views;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.data.Client;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view.macros.ColumnConfig;
import org.openjfx.api2semestre.view.macros.ColumnConfigString;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.macros.TableMacros.Formatter;
import org.openjfx.api2semestre.view.utils.filters.ClientFilter;
import org.openjfx.api2semestre.view.utils.wrappers.ClientWrapper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class Clients implements Initializable {

    @FXML private TextField tf_razao;
    @FXML private TextField tf_cnpj;

    @FXML private TableColumn<ClientWrapper, String> col_cnpj;
    @FXML private TableColumn<ClientWrapper, String> col_razao;

    private BooleanProperty col_cnpj_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_razao_enableFilter = new SimpleBooleanProperty();

    @FXML private TableView<ClientWrapper> tabela;
    private ObservableList<ClientWrapper> displayedClients;
    private List<Client> loadedClients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildTable();

        updateTable();  

        TableMacros.<ClientWrapper, String>enableEditableCells(
            col_cnpj,
            (String value) -> ! value.isBlank(),
            (ClientWrapper item, String value) -> item.getClient().setCnpj(value),
            new Formatter<String>() {
                private final StringConverter<String> converter = null;
                @Override public String format(String value, boolean editing) { return value; }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<String> getConverter() { return converter; }
            }
        );

        TableMacros.<ClientWrapper, String>enableEditableCells(
            col_razao,
            (String value) -> ! value.isBlank(),
            (ClientWrapper item, String value) -> item.getClient().setNome(value),
            new Formatter<String>() {
                private final StringConverter<String> converter = null;
                @Override public String format(String value, boolean editing) { return value; }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<String> getConverter() { return converter; }
            }
        );
    }

    @SuppressWarnings("unchecked") private void buildTable () {

        ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter();
            }
        };

        TableMacros.<ClientWrapper>createDeleteColumn(tabela, "cliente", (ClientWrapper clientWrapper) -> {
            QueryLibs.deleteClient(clientWrapper.getClient().getId());
            updateTable();
        });


        TableMacros.buildTable(
            tabela,
            new ColumnConfig[] {
                new ColumnConfigString<ClientWrapper>(col_razao, "razaoSocial", "Razão Social", Optional.of(col_razao_enableFilter)),
                new ColumnConfigString<ClientWrapper>(col_cnpj, "cnpj", "CNPJ", Optional.of(col_cnpj_enableFilter))
            },
            Optional.of(applyFilterCallback)
        );
    }

    private void updateTable () {

        loadedClients = Arrays.asList(QueryLibs.selectAllClients());

        applyFilter();
    }

    private void applyFilter () {

        List<Client> clientToDisplay = ClientFilter.filterFromView(
            loadedClients,
            Optional.empty(),
            Optional.empty()
        );

        displayedClients = FXCollections.observableArrayList(
            clientToDisplay.stream().map((Client cliente) -> new ClientWrapper(cliente)).collect(Collectors.toList())
        );

        tabela.setItems(displayedClients);
        tabela.refresh();

    }

    @FXML private void register (ActionEvent event) {

        String razao = tf_razao.getText();
        String cnpj = tf_cnpj.getText();

        if (razao.isEmpty() || cnpj.isEmpty()) {
            System.out.println("ClientsController.register -- Campo vazio ou inválido");
            return;
        }

        QueryLibs.insertClient(new Client(
            razao,
            cnpj
        ));
        updateTable();

    }
}
