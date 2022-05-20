package com.example.client;


import app.collection.Color;
import connection.Network;
import io.Message;
import io.RandomColor;
import io.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuthController {

    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private TextField LoginAuth;

    @FXML
    private TextField PswrdAuth;

    @FXML
    private Button Auth;

    @FXML
    private Button Register;
    public static String user_color;

    @FXML
    void initialize() {
        this.resources = AddressController.resourceBundle;
        Register.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("registerScene.fxml")); //загрузка экрана регистрации
            loader.setResources(ResourceBundle.getBundle(RunClient.BUNDLES_FOLDER, RunClient.locale));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });

        Auth.setOnAction(event -> {
            String login = LoginAuth.getText().trim();
            String pass = PswrdAuth.getText().trim();
            String color = RandomColor.getRandomColor();
            user_color = color;
            App_main_Controller.colors.put(login, user_color);
            if (!login.isEmpty()) {
                if (!pass.isEmpty()) {
                    try {
                        Network network = new Network(RunClient.ip_adress, RunClient.port);

                        User user = new User(login, pass);

                        Message message = new Message("AUTHORIZATION", user, false);

                        network.write(message);
                        String outServer = network.read().toString();

                        if (outServer.equals(RunClient.AUTHORIZATION_ISSUCCESS)) {
                            RunClient.login = login;
                            RunClient.pass = pass;
                            Auth.getScene().getWindow().hide();
                            Stage stage = new Stage();
                            Parent root = null;
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("mainScene.fxml")); //загрузка экрана регистрации
                                loader.setResources(ResourceBundle.getBundle(RunClient.BUNDLES_FOLDER, RunClient.locale));
                                loader.load();
                                Scene scene = new Scene(loader.getRoot());
                                stage.setScene(scene);
                                stage.setTitle("MAIN_APP_SOBOLEV_IVAN");
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            LoginAuth.setText("");
                            PswrdAuth.setText("");
                            Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                            alert.setTitle("Error");
                            alert.setHeaderText(resources.getString("address.connection.error.title"));
                            alert.setContentText(resources.getString("error.logpass"));
                            alert.showAndWait().ifPresent(rs -> {
                            });
                        }


                    } catch (IOException | ClassNotFoundException e) {
                        LoginAuth.setText("");
                        PswrdAuth.setText("");
                        Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                        alert.setTitle("Error");
                        alert.setHeaderText(resources.getString("address.connection.error.title"));
                        alert.setContentText(resources.getString("address.connection.error"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    }
                } else {
                    LoginAuth.setText("");
                    PswrdAuth.setText("");
                    Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                    alert.setTitle("Error");
                    alert.setHeaderText(resources.getString("error.lp"));
                    alert.setContentText(resources.getString("error.password"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                }
            } else {
                LoginAuth.setText("");
                PswrdAuth.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                alert.setTitle("Error");
                alert.setHeaderText(resources.getString("error.lp"));
                alert.setContentText(resources.getString("error.login"));
                alert.showAndWait().ifPresent(rs -> {
                });
            }
        });
    }
}
