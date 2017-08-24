package Objects;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;

/**
 * Created by Myles on 7/27/17.
 */
public class Server extends Thread{

    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Object object;

    public Server(Object object) {
        this.object = object;
    }

    public Server() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public int receiveAcknowledgement() throws IOException {
        int ack = in.readInt();
        return ack;
    }

    public Object recieveObject() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void sendObject(Object data) throws IOException {
        out.writeObject(data);
        out.flush();
    }

    public void close() {
        try {
            out.close();
            in.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        setPort(1025);
        try {
            connect();
            sendObject(object);
            int ack = receiveAcknowledgement();
            TreeMap<Long, Student> students = (TreeMap<Long, Student>) new ObjectIO(new File("students.sts")).readObject();
            if(ack == students.size()) {
                System.out.println(ack + " Done.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            close();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(new Student());
        server.start();
    }
}
