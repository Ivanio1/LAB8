package com.example.client;

import java.io.IOException;
import java.net.URL;
import java.util.*;


import app.collection.LabWork;
import connection.Network;
import io.Message;
import io.RandomColor;
import io.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import request.Commands;

import static com.example.client.RunClient.login;


public class App_main_Controller {
    public Button EXIT;
    public TextField LT;
    @FXML
    public Button SHOW;

    @FXML
    public Button SCRIPT;
    @FXML
    public Label USER;
    @FXML
    public Button HELP;
    @FXML
    public TextField ARGFIELD;
    public ObservableList<LabworkTable> cities = FXCollections.observableArrayList();

    private static int collectionSize = 0;
    @FXML
    public Button remove_at;
    @FXML
    public Button info;
    @FXML
    public Button remove_first;
    @FXML
    public Button add_if_max;
    @FXML
    public Button max_by_author;
    @FXML
    public Button filter_greater;
    @FXML
    public Button count_by_difficulty;
    @FXML
    public Button Update_id;
    @FXML
    public TableColumn<LabworkTable, Double> MinimalPoint;
    @FXML
    public TableColumn<LabworkTable, String> Difficulty;
    @FXML
    public TableColumn<LabworkTable, String> PersonName;
    @FXML
    public TableColumn<LabworkTable, String> PersonBirth;
    @FXML
    public TableColumn<LabworkTable, String> EyeColor;
    @FXML
    public TableColumn<LabworkTable, String> Country;
    @FXML
    public Rectangle userColorSq;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Canvas canvas;

    @FXML
    private TableView<LabworkTable> objectTable;

    @FXML
    private TableColumn<LabworkTable, Long> ID;

    @FXML
    private TableColumn<LabworkTable, String> Owner;

    @FXML
    private TableColumn<LabworkTable, String> Name;

    @FXML
    private TableColumn<LabworkTable, Long> X;

    @FXML
    private TableColumn<LabworkTable, Long> Y;

    @FXML
    private TableColumn<LabworkTable, String> CreationDate;


    @FXML
    private Button Add_button;
    ArrayList<LabWork> arrayList;
    public static int FINAL_ID;
    public static Map<String, String> colors = new HashMap<>();

    void draw_coord_sys(GraphicsContext gc) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
//        gc.strokeLine(w / 2, 0, w / 2, h);
//        gc.strokeLine(0, h / 2, w, h / 2);
        gc.strokeLine(0, 0, w, 0);
        gc.strokeLine(0, h, w, h);
        gc.strokeLine(0, 0, 0, h);
        gc.strokeLine(w, 0, w, h);
    }

    void draw_work(LabWork work, Canvas canvas, GraphicsContext gc) {
        double cx = canvas.getWidth();
        double cy = canvas.getHeight();
        double r = 30;
        double b_len = 40;
        //System.out.println(colors);
        gc.setStroke(Color.valueOf(colors.get(work.getOwner())));
        gc.setLineWidth(4);
        long x = work.getCoordinates().getX();
        long y = work.getCoordinates().getY();
        gc.strokeOval(x, cy - y - 60, r, r);

    }

    public boolean isInOval(int x, int y, int x1, int y1) {
        double distance = Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
        if (distance < 30) {
            return true;
        } else return false;
    }

    @FXML
    void initialize() throws IOException, ClassNotFoundException {
        this.resources = AddressController.resourceBundle;
        USER.setText(login);
        SHOW.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
        //рисуем оси
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        draw_coord_sys(canvas.getGraphicsContext2D());

        SHOW.setOnAction(event -> {
            try {
                Network network = new Network(RunClient.ip_adress, RunClient.port);
                User user = new User(login, RunClient.pass);
                Message message = new Message(Commands.SHOW.getCommandName(), user);
                network.write(message);

                arrayList = (ArrayList) network.read();
                collectionSize = arrayList.size();
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                draw_coord_sys(gc);
                for (LabWork sh : arrayList) {
                    if (!colors.containsKey(sh.getOwner())) {
                        colors.put(sh.getOwner(), RandomColor.getRandomColor());
                    }

                    draw_work(sh, canvas, canvas.getGraphicsContext2D());
                }
                userColorSq.setFill(Color.valueOf(colors.get(login)));
                getClientObjects(arrayList);
                //}
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Server disabled");
                System.exit(0);
            }

        });
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            for (LabWork e : arrayList) {
                if (isInOval((int) (e.getCoordinates().getX() + 30), (int) (e.getCoordinates().getY() + 30), (int) event.getX(), (int) (276 - event.getY()))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Info");
                    alert.setContentText(e.toString());
                    alert.showAndWait().ifPresent(rs -> {
                    });
                    if (e.getOwner().equals(login)) {
                        RunClient.Canvas_id=e.getId();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("DoubleClickCanvas.fxml"));
                        loader.setResources(ResourceBundle.getBundle(RunClient.BUNDLES_FOLDER, RunClient.locale));
                        try {
                            loader.load();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        Parent root = loader.getRoot();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    } else {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setHeaderText("Error");
                        alert1.setContentText(resources.getString("error.objectavailable"));
                        alert1.showAndWait().ifPresent(rs -> {
                        });
                    }
                }

            }
        });
        EXIT.setOnAction(event ->

        { //Двойной клик по объекту в таблице
            System.exit(0);
        });
        objectTable.setOnMouseClicked(event ->

        { //Двойной клик по объекту в таблице
            try {
                if (event.getClickCount() == 2) {
                    if (objectTable.getSelectionModel().getSelectedItem().getOwner().equals(login)) {
                        RunClient.labworkTable = objectTable.getSelectionModel().getSelectedItem();
                        //System.out.println(RunClient.labworkTable);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("DoubleClick.fxml"));
                        loader.setResources(ResourceBundle.getBundle(RunClient.BUNDLES_FOLDER, RunClient.locale));
                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Parent root = loader.getRoot();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("error.objectavailable"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    }
                }
            } catch (NullPointerException e) {
                //JAVA......
            }
        });
        HELP.setOnAction(event ->

        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setHeaderText("Error");
            alert.setContentText(resources.getString("help"));
            alert.showAndWait().ifPresent(rs -> {
            });
        });
        Add_button.setOnAction(event ->

        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("addScene.fxml"));
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
        add_if_max.setOnAction(event ->

        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add_if_maxScene.fxml"));
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
        SCRIPT.setOnAction(event ->

        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ScriptScene.fxml"));
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
        Update_id.setOnAction(event ->

        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("updateScene.fxml"));
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
        info.setOnAction(event ->

        {
            Network network = null;
            try {
                network = new Network(RunClient.ip_adress, RunClient.port);
                User user = new User(RunClient.login, RunClient.pass);
                Message message = new Message("info", user);
                network.write(message);
                String outServer = network.read().toString();
                if (outServer.equals("[EMPTY]")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.empty"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                } else {

                    String[] s = outServer.split(",");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Info");
                    alert.setContentText(resources.getString("col.type") + ": " + s[1].replace("]", "") + "\n" + resources.getString("col.size") + ": " + s[0].replace("[", ""));
                    alert.showAndWait().ifPresent(rs -> {
                    });

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        max_by_author.setOnAction(event ->

        {
            Network network = null;
            try {
                network = new Network(RunClient.ip_adress, RunClient.port);
                User user = new User(RunClient.login, RunClient.pass);
                Message message = new Message("max_by_author", user);
                network.write(message);
                String outServer = network.read().toString();
                if (outServer.equals("[EMPTY]")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.empty"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Info");
                    alert.setContentText(outServer.replace("[", "").replace("]", ""));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                // e.printStackTrace();
            }
        });
        remove_first.setOnAction(event ->

        {
            Network network = null;
            try {
                network = new Network(RunClient.ip_adress, RunClient.port);
                User user = new User(RunClient.login, RunClient.pass);
                Message message = new Message("remove_first", user);
                network.write(message);
                String outServer = network.read().toString();
                if (outServer.equals("[EMPTY]")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.empty"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                } else if (outServer.equals("[YES]")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Info");
                    alert.setContentText(resources.getString("delete"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                    // undraw_shorty(canvas, canvas.getGraphicsContext2D());
                } else if (outServer.equals("[NOT]")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.objectavailable"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                } else if (outServer.equals("[INDEX]")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText(resources.getString("error.index"));
                    alert.showAndWait().ifPresent(rs -> {
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                // e.printStackTrace();
            }
        });
        remove_at.setOnAction(event ->

        {
            Network network = null;
            if (!ARGFIELD.getText().equals("")) {
                try {
                    network = new Network(RunClient.ip_adress, RunClient.port);
                    User user = new User(RunClient.login, RunClient.pass);
                    Message message = new Message("remove_at", ARGFIELD.getText(), user);
                    network.write(message);
                    String outServer = network.read().toString();
                    if (outServer.equals("[EMPTY]")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("error.empty"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    } else if (outServer.equals("[YES]")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Info");
                        alert.setContentText(resources.getString("delete"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    } else if (outServer.equals("[NOT]")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("error.objectavailable"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    } else if (outServer.equals("[INDEX]")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("error.index"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    }
                } catch (IOException | ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                alert.setTitle("Error");
                alert.setHeaderText("Null");
                alert.setContentText("Null Argument");
                alert.showAndWait().ifPresent(rs -> {
                });
            }
        });
        count_by_difficulty.setOnAction(event ->

        {
            Network network = null;
            if (!ARGFIELD.getText().equals("")) {
                try {
                    network = new Network(RunClient.ip_adress, RunClient.port);
                    User user = new User(RunClient.login, RunClient.pass);
                    Message message = new Message("count_by_difficulty", ARGFIELD.getText(), user);
                    network.write(message);
                    String outServer = network.read().toString();
                    String[] s = outServer.split(",");
                    if (outServer.equals("[EMPTY]")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("error.empty"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Info");
                        alert.setContentText(resources.getString("element") + s[0].replace("[", "") + " = " + s[1].replace("]", ""));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    }
                } catch (IOException | ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                alert.setTitle("Error");
                alert.setHeaderText("Null");
                alert.setContentText("Null Argument");
                alert.showAndWait().ifPresent(rs -> {
                });
            }
        });
        filter_greater.setOnAction(event ->

        {
            Network network = null;
            if (!ARGFIELD.getText().equals("")) {
                try {
                    network = new Network(RunClient.ip_adress, RunClient.port);
                    User user = new User(RunClient.login, RunClient.pass);
                    Message message = new Message("filter_greater", ARGFIELD.getText(), user);
                    network.write(message);
                    String outServer = network.read().toString();

                    if (outServer.equals("[EMPTY]")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("error.empty"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    } else if (outServer.equals("[WRONG]")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText(resources.getString("wrong"));
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Info");
                        alert.setContentText(outServer);
                        alert.showAndWait().ifPresent(rs -> {
                        });
                    }
                } catch (IOException | ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
                alert.setTitle("Error");
                alert.setHeaderText("Null");
                alert.setContentText("Null Argument");
                alert.showAndWait().ifPresent(rs -> {
                });
            }
        });
    }

    private void getClientObjects(ArrayList<LabWork> arrayList) {
        cities.clear();

        for (LabWork el : arrayList) {
            cities.add(new LabworkTable(el.getId(), el.getOwner(), el.getName(), el.getCoordinates().getX(),
                    el.getCoordinates().getY(), el.getCreationDate(), el.getMinimalPoint(), el.getDifficulty(), el.getAuthor().getName(), el.getAuthor().getBirthday(), el.getAuthor().getEyeColor(), el.getAuthor().getNationality()));
        }


        ID.setCellValueFactory(new PropertyValueFactory<LabworkTable, Long>("Id"));
        Owner.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("Owner"));
        Name.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("Name"));
        X.setCellValueFactory(new PropertyValueFactory<LabworkTable, Long>("X"));
        Y.setCellValueFactory(new PropertyValueFactory<LabworkTable, Long>("Y"));
        CreationDate.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("creationDate"));
        MinimalPoint.setCellValueFactory(new PropertyValueFactory<LabworkTable, Double>("MinimalPoint"));
        Difficulty.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("Difficulty"));
        PersonName.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("PersonName"));
        PersonBirth.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("PersonBirth"));
        EyeColor.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("EyeColor"));
        Country.setCellValueFactory(new PropertyValueFactory<LabworkTable, String>("Country"));
        objectTable.setItems(cities);

        FilteredList<LabworkTable> filteredList = new FilteredList<>(cities, b -> true);
        LT.textProperty().addListener((observableValue, old, newval) -> {
            filteredList.setPredicate(city -> {
                if (newval.isEmpty() || newval.isBlank() || newval == null) {
                    return true;
                }
                String search = newval.toLowerCase();
                if (city.getName().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getOwner().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getId().toString().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (String.valueOf(city.getX()).toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (String.valueOf(city.getY()).toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getCreationDate().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getPersonBirth().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getPersonName().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getDifficulty().getName().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getCountry().getName().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (city.getEyeColor().getName().toLowerCase().indexOf(search) > -1) {
                    return true;
                } else if (String.valueOf(city.getMinimalPoint()).toLowerCase().indexOf(search) > -1) {
                    return true;
                } else return false;
            });
        });
        SortedList<LabworkTable> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(objectTable.comparatorProperty());
        objectTable.setItems(sortedList);
    }


}
