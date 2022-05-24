package com.example.client;

import connection.Network;
import io.Message;
import io.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static com.example.client.App_main_Controller.FLAG;

public class DoubleClickCanvas {
    @FXML
    public Button UPDATE;
    @FXML
    public Button DELETE;
    @FXML
    private ResourceBundle resources;

    @FXML
    void initialize() {
        this.resources = AddressController.resourceBundle;
        UPDATE.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CanvasUpdate.fxml"));
            loader.setResources(ResourceBundle.getBundle(RunClient.BUNDLES_FOLDER, RunClient.locale));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        });
        DELETE.setOnAction(event -> {
            Network network = null;
            try {
                network = new Network(RunClient.ip_adress, RunClient.port);
                User user = new User(RunClient.login, RunClient.pass);
                Message message = new Message("remove", String.valueOf(RunClient.Canvas_id), user);
                network.write(message);
                String outServer = network.read().toString();
                FLAG = false;
                if (outServer.equals("[EMPTY]")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.empty"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                } else if (outServer.equals("[NOT]")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.objectavailable"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                // e.printStackTrace();
            }
        });
        try {
            DELETE.getScene().getWindow().hide();
        }catch(NullPointerException e){
            ///
        }
    }
}
