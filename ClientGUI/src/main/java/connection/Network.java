package connection;

import com.example.client.AddressController;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ResourceBundle;

public class Network {

    private String address;
    private int port;
    private Socket socket;

    private static boolean connection;
    private ResourceBundle resourceBundle= AddressController.resourceBundle;


    public Network(String address, int port) throws IOException {

       try {
           socket = new Socket(address,port);
           this.connection = true;
       }catch (ConnectException|IllegalArgumentException e){
          // e.printStackTrace();
           Alert alert = new Alert(Alert.AlertType.ERROR); //если проверка не прошла
           alert.setTitle("Error");
           alert.setHeaderText(resourceBundle.getString("address.null.title"));
           alert.setContentText(resourceBundle.getString("address.null"));
           alert.showAndWait().ifPresent(rs -> {
           });
           // System.exit(0);
       }

    }

    public void write(Object obj) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream send = new ObjectOutputStream(baos);
        send.writeObject(obj);
        byte[] outcoming = baos.toByteArray();
        socket.getOutputStream().write(outcoming);
        send.flush();
        baos.flush();


    }
    public Object read() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Object o = ois.readObject();
        return o;
    }


    public Socket getSocket() {
        return socket;
    }



    public void closeStream() throws IOException {
        socket.close();
        connection = false;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }

    public static boolean isConnection() {
        return connection;
    }
}
