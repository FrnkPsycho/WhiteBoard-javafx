package top.frnks.whiteboardjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import top.frnks.whiteboardjavafx.common.Student;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ServerDataBuffer {
    public static ServerSocket serverSocket;
    public static Thread serverThread = new ServerThread();
    public static Map<Long, ServerClientIO> serverClientIOMap = new ConcurrentSkipListMap<>();
    public static Map<Long, Student> studentsMap = new ConcurrentSkipListMap<>();
    public static ObservableList<Student> studentsList = FXCollections.observableArrayList();
    private ServerDataBuffer() {}
}
