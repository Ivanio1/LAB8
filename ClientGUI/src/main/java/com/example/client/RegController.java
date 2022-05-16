package com.example.client;


import connection.Network;
import io.Message;
import io.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private TextField LoginReg;

    @FXML
    private TextField PswrdReg;

    @FXML
    private Button Reg;

    @FXML
    void initialize() {

        this.resources=AddressController.resourceBundle;
        Reg.setOnAction(event->{
            //Network network = new Network(RunClient.ip_adress, RunClient.port);
            String login = LoginReg.getText().trim();
            String pass = PswrdReg.getText().trim();
            if(!login.isEmpty() ){
                if(!pass.isEmpty()){
                    Network network = null;
                    try {
                        network = new Network(RunClient.ip_adress, RunClient.port);
                        User user = new User(login,pass);

                        Message message = new Message("REGISTRATION",user,true);

                        network.write(message);

                        String outServer = network.read().toString();

                        if(outServer.equals(RunClient.REGISTRATION_ISSUCESS)){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION); //если проверка не прошла
                            alert.setTitle("Success");
                            alert.setHeaderText("Success");
                            alert.setContentText(resources.getString("rrrr"));
                            alert.showAndWait().ifPresent(rs -> {
                            });
                            Reg.getScene().getWindow().hide();
                        }else{
                            LoginReg.setText("");
                            PswrdReg.setText("");
                            Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                            alert.setTitle("Error");
                            alert.setHeaderText(resources.getString("error.lp"));
                            alert.setContentText(resources.getString("rrr"));
                            alert.showAndWait().ifPresent(rs -> {
                            });
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    LoginReg.setText("");
                    PswrdReg.setText("");
                    Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                    alert.setTitle("Error");
                    alert.setHeaderText(resources.getString("error.lp"));
                    alert.setContentText(resources.getString("error.password"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                }
            }else{
                LoginReg.setText("");
                PswrdReg.setText("");
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
