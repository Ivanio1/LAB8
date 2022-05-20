package com.example.client;

import connection.Network;
import io.Message;
import io.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.ResourceBundle;

import static com.example.client.App_main_Controller.FLAG;

public class ScriptScene {
    @FXML
    public Button GO;
    @FXML
    public TextArea TEXT;
    @FXML
    private ResourceBundle resources;

    @FXML
    void initialize() {
        this.resources = AddressController.resourceBundle;

        GO.setOnAction(event -> {
            Network network = null;
            if (!TEXT.getText().equals("")) {
                try {
                    network = new Network(RunClient.ip_adress, RunClient.port);
                    User user = new User(RunClient.login, RunClient.pass);
                    Message message = new Message("execute_script", TEXT.getText(), user);
                    network.write(message);
                    String outServer = network.read().toString();
                    FLAG=false;
                    //System.out.println(outServer);
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    TextArea area = new TextArea(outServer);
                    area.setWrapText(true);
                    area.setEditable(false);

                    alert1.getDialogPane().setContent(area);
                    alert1.setResizable(true);
                    alert1.showAndWait().ifPresent(rs -> {
                    });
                    GO.getScene().getWindow().hide();

                } catch (IOException | ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                alert1.setTitle("Error");
                alert1.setHeaderText("Null");
                alert1.setContentText(resources.getString("NullArgument"));
                alert1.showAndWait().ifPresent(rs -> {
                });
            }
        });
    }
}
