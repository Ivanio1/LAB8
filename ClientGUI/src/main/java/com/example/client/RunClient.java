package com.example.client;

import app.collection.GUI.Lang;
import app.collection.GUI.LocaleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;


public class RunClient extends Application implements Observer {

    public static String ip_adress;
    public static int port;
    public static String login;
    public static String pass;
    public static LabworkTable labworkTable;
    public static final String REGISTRATION_ISSUCESS = "[110100011000000011010000101101011101000010110011]";
    public static final String AUTHORIZATION_ISSUCCESS = "[110100001011000011010000101100101101000110000010]";
    public static Timer timer = new Timer();
    private FXMLLoader fxmlLoader;
    public static final String BUNDLES_FOLDER = "com.example.client.Locale";
    private AddressController addressController;

    private Stage primaryStage;
    private AnchorPane currentRoot;
    public static Locale locale;
    public static long Canvas_id;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        createGUI();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    private AnchorPane loadFXML(Locale locale) {
        this.locale=locale;
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addressScene.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
        AnchorPane node = null;
        try {
            node = fxmlLoader.load();
            addressController = fxmlLoader.getController();
            addressController.addObserver(this);
            primaryStage.setTitle(fxmlLoader.getResources().getString("address.title"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    private void createGUI() {
        currentRoot = loadFXML(LocaleManager.RU_LOCALE);
        Scene scene = new Scene(currentRoot, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(400);
        primaryStage.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        Lang lang = (Lang) arg;
        AnchorPane newNode = loadFXML(lang.getLocale()); // получить новое дерево компонетов с нужной локалью
        currentRoot.getChildren().setAll(newNode.getChildren());// заменить старые дочерник компонента на новые - с другой локалью
    }
}
