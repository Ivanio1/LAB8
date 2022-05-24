package com.example.client;


import app.collection.*;
import connection.Network;
import io.Message;
import io.RandomColor;
import io.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import request.Commands;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import static com.example.client.App_main_Controller.*;
import static com.example.client.RunClient.login;

public class AddController {
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
    public App_main_Controller app=new App_main_Controller();

    @FXML
    private Button add;

    @FXML
    void initialize() {
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
            try {
                if (!(Integer.parseInt(x.getText()) < 0) && !(Integer.parseInt(y.getText()) < 0) &&!(Integer.parseInt(x.getText()) > 626)&&!(Integer.parseInt(y.getText()) > 172) && isDate(personBirthday.getText().trim())) {
                    try {
                        String _personBirthday = personBirthday.getText().trim();
                        LabWork work = new LabWork(
                                name.getText(),
                                new Coordinates(Long.parseLong(x.getText()),
                                        Long.parseLong(y.getText())), Double.parseDouble(minimalPoint.getText()), String.valueOf(Date.from(Instant.now())), Difficulty.StringNameToObj(String.valueOf(difficulty.getValue())), new Person(PersonName.getText(), _personBirthday, Color.StringNameToObj(String.valueOf(eyeColor.getValue())), Country.StringNameToObj(String.valueOf(nationality.getValue()))));
                        try {
                            Network network = new Network(RunClient.ip_adress, RunClient.port);
                            Message message = new Message(Commands.ADD.getCommandName(), work,
                                    new User(RunClient.login, RunClient.pass));
                            //System.out.println(message+""+message.getLabWork()+message.getArgs());
                            network.write(message);
                            String outServer = network.read().toString();
                            FLAG=false;
                           // StaticsetINFO();
                            add.getScene().getWindow().hide();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } catch (NumberFormatException | DateTimeParseException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("error.format"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.format"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                }
            } catch (NullPointerException | NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText(resources.getString("error.format"));
                alert.showAndWait().ifPresent(rs -> {
                });
            }
        });
    }

    boolean isDate(String line) {
        boolean flag=true;
        try {
            String[] ar = line.split(" ");
            String[] ar1 = ar[0].split("-");
            String[] ar2 = ar[1].split(":");
            if (Integer.parseInt(ar1[2]) < 0 || Integer.parseInt(ar1[2]) > 31 || Integer.parseInt(ar1[1]) < 0 || Integer.parseInt(ar1[1]) > 12 || Integer.parseInt(ar2[0]) < 0 || Integer.parseInt(ar2[0]) > 23 || Integer.parseInt(ar2[1]) < 0 || Integer.parseInt(ar2[1]) > 60) {
                flag= false;
            } else flag= true;
        }catch (ArrayIndexOutOfBoundsException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(resources.getString("error.format"));
            alert.showAndWait().ifPresent(rs -> {
            });
        }
        return flag;
    }
}