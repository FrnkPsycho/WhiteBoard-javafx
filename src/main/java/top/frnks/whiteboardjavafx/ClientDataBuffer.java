package top.frnks.whiteboardjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import top.frnks.whiteboardjavafx.common.Student;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientDataBuffer {
    public static Socket clientSocket;
    public static ObjectInputStream objectInputStream;
    public static ObjectOutputStream objectOutputStream;
    public static String clientIp;
    public static Student currentStudent;
    public static ObservableList<Student> studentsList = FXCollections.observableArrayList();
    public static boolean isOnline = false;
    public static boolean isConnected = false;

    static {
        try {
            clientIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private ClientDataBuffer() {}
}
