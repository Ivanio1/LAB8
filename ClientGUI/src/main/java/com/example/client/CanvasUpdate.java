package com.example.client;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
;
import app.collection.*;

import connection.Network;
import io.Message;
import io.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import request.Commands;


public class CanvasUpdate {
    @FXML
    public Label ID;
    @FXML
    public TextField minimalPoint;
    @FXML
    public TextField name;
    @FXML
    public TextField y;
    @FXML
    public TextField x;
    @FXML
    public TextField PersonName;
    @FXML
    public ComboBox difficulty;
    @FXML
    public ComboBox eyeColor;
    @FXML
    public ComboBox nationality;
    @FXML
    public TextField personBirthday;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private Button add;

    @FXML
    void initialize() {
        ID.setText(String.valueOf(RunClient.Canvas_id));
        ObservableList<String> _difficulty = FXCollections.observableArrayList(
                Difficulty.EASY.getName(),
                Difficulty.HARD.getName(),
                Difficulty.VERY_HARD.getName(),
                Difficulty.HOPELESS.getName());

        difficulty.setItems(_difficulty);


        ObservableList<String> _color = FXCollections.observableArrayList(
                Color.GREEN.getName(), Color.RED.getName(), Color.BLACK.getName(), Color.ORANGE.getName(), Color.BROWN.getName());

        eyeColor.setItems(_color);
        ObservableList<String> _n = FXCollections.observableArrayList(
                Country.USA.getName(), Country.GERMANY.getName(), Country.INDIA.getName(), Country.VATICAN.getName(), Country.SOUTH_KOREA.getName());

        nationality.setItems(_n);

        add.setOnAction(event -> {
            if (Integer.parseInt(x.getText()) < 0 || Integer.parseInt(x.getText()) < 0) {
                try {
                    String _personBirthday = personBirthday.getText().trim();

                    LabWork work = new LabWork(Integer.parseInt(ID.getText()), RunClient.login,
                            name.getText(),
                            new Coordinates(Long.parseLong(x.getText()),
                                    Long.parseLong(y.getText())), Double.parseDouble(minimalPoint.getText()), String.valueOf(Date.from(Instant.now())), Difficulty.StringNameToObj(String.valueOf(difficulty.getValue())), new Person(PersonName.getText(), String.valueOf(_personBirthday), Color.StringNameToObj(String.valueOf(eyeColor.getValue())), Country.StringNameToObj(String.valueOf(nationality.getValue()))));

                    try {
                        Network network = new Network(RunClient.ip_adress, RunClient.port);
                        Message message = new Message(Commands.UPDATEID.getCommandName(), work,
                                new User(RunClient.login, RunClient.pass));
                        network.write(message);
                        String outServer = network.read().toString();
                        //System.out.println(outServer);
                        if (outServer.equals("[NOT]")) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Error");
                            alert.setContentText(resources.getString("error.objectavailable"));
                            alert.showAndWait().ifPresent(rs -> {
                            });
                        }
                        if (outServer.equals("[EMPTY]")) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Error");
                            alert.setContentText(resources.getString("error.empty"));
                            alert.showAndWait().ifPresent(rs -> {
                            });
                        }
                        add.getScene().getWindow().hide();

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException | DateTimeParseException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("Error Format");
                    alert.showAndWait().ifPresent(rs -> {
                    });
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Error Format");
                alert.showAndWait().ifPresent(rs -> {
                });
            }
        });
    }
}
