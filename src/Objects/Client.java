package Objects;

import UI.StudentUI;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Myles on 8/3/17.
 */
public class Client /*extends Task<ObservableList<Student>>*/ {

    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Client() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() throws IOException, ClassNotFoundException {
        socket = new Socket(ip, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public int receiveAcknowledgement() throws IOException {
        int ack = in.readInt();
        return ack;
    }

    public void sendAcknowledgement(int acknowledgement) throws IOException {
        out.writeInt(acknowledgement);
        out.flush();
    }

    public void send(Object data) throws IOException {
        out.writeObject(data);
        out.flush();
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void close(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
