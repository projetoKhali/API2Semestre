package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.view.controllers.Base;
import org.openjfx.api2semestre.view.controllers.templates.ViewButton;
import org.openjfx.api2semestre.view.manager.View;
import org.openjfx.api2semestre.view.manager.ViewsManager;

public class App extends Application {

    /// Retorna o local do arquivo .env a ser utilizado para conectar com o banco de dados.
    /// App é a classe principal do projeto então faz sentido essa informação estar aqui
    /// para mais facilmente ser encontrada e modificada para testes ou configuração.
    /// O arquivo deve estar no seguinte formato:
    /// host:substitua_pelo_host_do_banco_de_dados
    /// port:substitua_pelo_port_do_banco_de_dados
    /// userName:substitua_pelo_userName_do_banco_de_dados
    /// password:substitua_pela_password_do_banco_de_dados
    /// database:substitua_pelo_database_do_banco_de_dados
    public static final String getEnvLocation () { return "././2.env"; }

    /// Referencias para scene e stage, são populadas ao rodar o programa e modificadas durante execução
    private static Scene scene;
    private static Stage stage;

    public static Stage getStage () { return stage; }
    private static void setStage (Stage newStage) { stage = newStage; }

    /// Nome da tela em exibição no momento
    @SuppressWarnings("unused") private static String currentViewFxmlFile;

    /// referencia para o controller da base da janela "base.fxml"
    private static Base baseController;

    @Override public void start(Stage stage) throws IOException {
        setStage(stage);

        //// -------------------------------------------------------------------------------------------------------
        //// ------------ ESTABELECER CONEXÃO COM O BD PARA SQL FILES OU TABELA USER -------------------------------
        //// -------------------------------------------------------------------------------------------------------

        // Inicia a conexão com o banco de dados
        // Optional<java.sql.Connection> connectionOptional = org.openjfx.api2semestre.database.QueryLibs.connect();
        
        //// -------------------------------------------------------------------------------------------------------
        //// ------------ INICIALIZAR TABELAS E VIEWS | SQL FILES --------------------------------------------------
        //// -------------------------------------------------------------------------------------------------------

        // Para criar as tabelas que estiverem faltando
        // org.openjfx.api2semestre.database.QueryLibs.executeSqlFile("SQL/tabelas.sql", connectionOptional);

        // Para criar as views que estiverem faltando
        // org.openjfx.api2semestre.database.QueryLibs.executeSqlFile("SQL/views.sql", connectionOptional);
        
        //// -------------------------------------------------------------------------------------------------------
        //// ------------ POPULAR A TABELA USER --------------------------------------------------------------------
        //// -------------------------------------------------------------------------------------------------------

        // org.openjfx.api2semestre.database.QueryLibs.insertUser(
        //     new org.openjfx.api2semestre.authentication.User(
        //         "adm exemplo",
        //         org.openjfx.api2semestre.authentication.Profile.Administrator,
        //         "a@d.m", //                             <---------------------------- LOGIN
        //         "123" //                                <---------------------------- SENHA
        //     ),
        //     connectionOptional
        // );

        // org.openjfx.api2semestre.database.QueryLibs.insertUser(
        //     new org.openjfx.api2semestre.authentication.User(
        //         "ges exemplo",
        //         org.openjfx.api2semestre.authentication.Profile.Gestor,
        //         "g@e.s", //                             <---------------------------- LOGIN
        //         "123" //                                <---------------------------- SENHA
        //     ),
        //     connectionOptional
        // );

        // org.openjfx.api2semestre.database.QueryLibs.insertUser(
        //     new org.openjfx.api2semestre.authentication.User(
        //         "col exemplo",
        //         org.openjfx.api2semestre.authentication.Profile.Colaborador,
        //         "c@o.l", //                             <---------------------------- LOGIN
        //         "123" //                                <---------------------------- SENHA
        //     ),
        //     connectionOptional
        // );

        //// -------------------------------------------------------------------------------------------------------
        //// ------------ FLUXO NORMAL DE UTILIZAÇÃO ---------------------------------------------------------------
        //// -------------------------------------------------------------------------------------------------------

        loginView();

        //// -------------------------------------------------------------------------------------------------------
        //// ------------ TESTE DE TELAS ---------------------------------------------------------------------------
        //// -------------------------------------------------------------------------------------------------------

        // scene = new Scene(loadFXML((currentViewFxmlFile = "views/empty")));
        // stage.setScene(scene);
        // stage.show();
        // Authentication.login("a@d.m", "123");
        // try { ViewsManager.handleViews(); } catch (Exception e) { e.printStackTrace(); }
        // changeView(Optional.of("views/dashboard"));
    }

    /// Inicia a tela de login
    public static void loginView () {

        // Reseta o baseController pra null já que a tela de login não utiliza base.fxml
        // Isso é feito caso o usuário tenha chegado na tela de login através de um logout
        baseController = null;

        try {

            // Tenta carregar o fxml da tela de login e define como a nova "scene",
            // Define a nova scene como scene do stage e chama .show() para:
            // abrir, redimensionar e reposicionar a janela
            scene = new Scene(loadFXML((currentViewFxmlFile = "views/login")));
            stage.setScene(scene);
            stage.show();
            // System.out.println(currentViewFxmlFile);

        } catch (Exception ex) {
            System.out.println("App.loginView() -- Erro!");
            ex.printStackTrace();
        }
    }

    // centraliza a janela na tela
    public static void centerWindow () {
        javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.5);
        stage.setY(bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.5);
    }

    /// Carrega a base de qualquer tela pós login, contendo a logo da Khali e o menu lateral
    static void loadBase () throws IOException {

        // Puxa o FXMLLoader com o fxml requisitado através de App.getFXML
        FXMLLoader loader = new FXMLLoader(App.getFXML("base"));

        // Caso a scene e o baseController não sejam nulos, define scene como uma nova scene,
        // utilizando as mesmas dimensões de tela estabelecidas previamente na scene anterior.
        // Isso é feito para manter a janela do mesmo tamanho ao trocar de telas através do menu lateral.
        if (scene != null && baseController != null) scene = new Scene(loader.load(), scene.getWidth(), scene.getHeight());

        // Caso contrário, estamos carregando a base após efetuar login,
        // nesse caso não utilizamos as dimensões de janela anteriores
        // já que essas correspondem às dimensões da tela de login
        else scene = new Scene(loader.load());

        // define a nova scene do stage 
        stage.setScene(scene);

        // define baseController como o controlador da base recem carregada
        baseController = loader.getController();

        // pega a label lb_currentUser através do baseController e coloca o texto de usuário logado
        Label lb_currentUser = baseController.getLb_currentUser();
        lb_currentUser.setText("Logado como " + Authentication.getCurrentUser().getName());
        lb_currentUser.setFont(new Font(12));
        lb_currentUser.setWrapText(true);

    }

    public static void closeWindow() {
        stage.hide();
    }

    /// Troca a exibição para a tela especificada
    public static void changeView (Optional<String> newViewFxmlOptional) {

        try {

            // carrega a base
            loadBase();

            // retorna caso nenhuma tela seja especificada. A base será carregada sem conteúdo 
            // porem exibindo o menu lateral
            if (newViewFxmlOptional.isEmpty()) return;

            // carrega a nova tela
            String newViewFxml = newViewFxmlOptional.get();
            Parent module = loadFXML(newViewFxml);

            // define a nova tela como conteudo da base
            baseController.getAp_content().getChildren().add(module);
            currentViewFxmlFile = newViewFxml;

            // itera sobre as telas que o usuário logado tem acesso
            View[] accessibleViews = ViewsManager.getViews();
            if (accessibleViews != null) for (View view : accessibleViews) {

                // carrega o template de botão pra tela
                FXMLLoader viewButtonLoader = new FXMLLoader(App.getFXML("templates/viewButtonTemplate"));

                // adiciona o botão ao menu lateral
                baseController.getVb_views().getChildren().add(viewButtonLoader.load());

                // puxa o controller do botão
                ViewButton viewButtonTemplateController = viewButtonLoader.getController();

                // puxa o caminho de arquivo da tela que corresponde a esse botão
                String buttonViewFxmlFile = view.getFxmlFileName();

                // define a tela que o botão irá abrir ao ser clicado e define o texto de exibição
                viewButtonTemplateController.setView(buttonViewFxmlFile);
                viewButtonTemplateController.setText(view.getDisplayName());

                // desabilita o clique ao botão correspondente a tela em exibição no momento
                if (buttonViewFxmlFile.equals(newViewFxml)) viewButtonTemplateController.setDisable(true);
                // if (buttonViewFxmlFile.equals(currentViewFxmlFile)) viewButtonTemplateController.setDisable(false);
            }

        } catch (Exception ex) {
            System.out.println("App.changeView() -- Erro ao trocar para a tela '" + newViewFxmlOptional + "'");
            ex.printStackTrace();
        }
    }

    /// Função responsável por retornar o caminho da tela com o nome especificado.
    /// Como App está no primeiro nivel dentro da hierarquia de packages,
    /// essa função faz com que não seja necessário adicionar um ou mais "./" ao inicio da string
    /// ao carregar uma tela a partir de outro arquivo que esteja em outro nivel da hierarquia
    public static URL getFXML (String fxml) {
        return App.class.getResource(fxml + ".fxml");
    }

    /// Encapsula o código de carregamento de um arquivo fxml.
    /// Retorna a raiz (o node principal) do fxml do arquivo carregado.
    public static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getFXML(fxml));
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.setProperty("javafx.fxml.debug", "true");
        launch();
    }

}