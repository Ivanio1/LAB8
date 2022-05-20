package com.example.client;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;


import app.collection.GUI.Lang;
import app.collection.GUI.LocaleManager;
import connection.Network;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddressController extends Observable implements Initializable {
    @FXML
    public static Label LABEL;
    private static final String RU_CODE = "ru";
    private static final String EN_CODE = "en";
    private static final String CZ_CODE = "cz";
    private static final String SV_CODE = "sv";
    public static ResourceBundle resourceBundle;
    public Text host;



    @FXML
    private URL location;

    @FXML
    private ComboBox<Lang> languages;

    @FXML
    private TextField IP_adress;

    @FXML
    private TextField Port;

    @FXML
    private Button Input;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        FillLang();
        initListeners();

    }

    private void initListeners() {
        // слушает изменение языка
        languages.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Lang selectedLang = (Lang) languages.getSelectionModel().getSelectedItem();
                LocaleManager.setCurrentLang(selectedLang);
                // уведомить всех слушателей, что произошла смена языка
                setChanged();
                notifyObservers(selectedLang);
            }
        });
        Input.setOnAction(event -> {

               String addressConnect = IP_adress.getText().trim();
               String portConnect = Port.getText().trim();
               if (!addressConnect.isEmpty() && addressConnect.length() == addressConnect.replaceAll("[^0-9.]", "").length() || addressConnect.toLowerCase().equals("localhost")) {//здесь должна быть провекра на правильность формата ввода
                   if (!portConnect.isEmpty() && portConnect.length() == portConnect.replaceAll("[^0-9]", "").length()) {
                       try {
                           if (!addressConnect.equals("localhost"))
                               addressConnect = addressConnect.replaceAll("[^0-9.]", "");
                           Network network = new Network(addressConnect.toLowerCase(), Integer.parseInt(portConnect));
                           //установка соединения
                           RunClient.ip_adress = addressConnect;
                           RunClient.port = Integer.parseInt(portConnect);

                           Input.getScene().getWindow().hide();
                           FXMLLoader loader = new FXMLLoader();
                           loader.setLocation(getClass().getResource("authScene.fxml")); //загрузка экрана входа
                            loader.setResources(ResourceBundle.getBundle(RunClient.BUNDLES_FOLDER, RunClient.locale));
                           try {
                               loader.load();
                           } catch (IOException e) {
                               //e.printStackTrace();
                           }

                           Parent root = loader.getRoot();
                           Stage stage = new Stage();
                           stage.setTitle(resourceBundle.getString("auth.title"));
                           stage.setScene(new Scene(root));
                           stage.show();
                       } catch (IOException e) {
                           Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                           alert.setTitle("Error");
                           alert.setHeaderText(resourceBundle.getString("address.connection.error.title"));
                           alert.setContentText(resourceBundle.getString("address.connection.error"));
                           alert.showAndWait().ifPresent(rs -> {
                           });
                       }
                   } else {
                       Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                       alert.setTitle("Error");
                       alert.setHeaderText(resourceBundle.getString("address.null.title"));
                       alert.setContentText(resourceBundle.getString("address.null"));
                       alert.showAndWait().ifPresent(rs -> {
                       });
                   }
               } else {
                   Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                   alert.setTitle("Error");
                   alert.setHeaderText(resourceBundle.getString("address.null.title"));
                   alert.setContentText(resourceBundle.getString("address.null"));
                   alert.showAndWait().ifPresent(rs -> {
                   });
               }

        });
    }

    public void FillLang() {
        Lang langRU = new Lang(0, RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
        Lang langEN = new Lang(1, EN_CODE, resourceBundle.getString("en"), LocaleManager.EN_LOCALE);
        Lang langCZ = new Lang(2, CZ_CODE, resourceBundle.getString("cz"), LocaleManager.CZ_LOCALE);
        Lang langSV = new Lang(3, SV_CODE, resourceBundle.getString("sv"), LocaleManager.SV_LOCALE);
        languages.getItems().add(langRU);
        languages.getItems().add(langEN);
        languages.getItems().add(langCZ);
        languages.getItems().add(langSV);
        if (LocaleManager.getCurrentLang() == null) {// по-умолчанию показывать выбранный русский язык (можно текущие настройки языка сохранять в файл)
            languages.getSelectionModel().select(0);
        } else {
            languages.getSelectionModel().select(LocaleManager.getCurrentLang().getIndex());
        }
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
