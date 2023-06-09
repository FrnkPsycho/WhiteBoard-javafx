package top.frnks.whiteboardjavafx.controller;

import javafx.scene.input.MouseEvent;
import top.frnks.whiteboardjavafx.ServerClientIO;
import top.frnks.whiteboardjavafx.ServerDataBuffer;
import top.frnks.whiteboardjavafx.common.Request;
import top.frnks.whiteboardjavafx.common.Response;
import top.frnks.whiteboardjavafx.common.ResponseType;
import top.frnks.whiteboardjavafx.common.Student;
import top.frnks.whiteboardjavafx.gui.GUIApplication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class ServerAction {
    public static void login(ServerClientIO io, Request request) throws IOException {
        String name = (String) request.getData("name");
        long id = (long) request.getData("id");

        Student student = new Student(name, id);

        Response response = new Response(ResponseType.LOGIN_SUCCESS);
        ServerDataBuffer.studentsMap.putIfAbsent(id, student);
        ServerDataBuffer.studentsList.add(student);
        ServerDataBuffer.serverClientIOMap.put(id, io);

        List<Student> serializableList = ServerDataBuffer.studentsList.stream().toList();

        response.setData("students", serializableList);
        response.setData("student", student);
        broadcastResponse(response);

        GUIApplication.studentListView.refresh();
    }
    public static void sendMouseEvent(MouseEvent event) {
        Response response = new Response(ResponseType.MOUSE_EVENT);
        response.setData("event", event);
        try {
            broadcastResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void clearCanvas() {
        GUIApplication.clearCanvas();
        Response response = new Response(ResponseType.CLEAR_CANVAS);
        try {
            broadcastResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendFile() {
        // TODO: send file to student
    }

    private static void sendResponse(ServerClientIO io, Response response) throws IOException {
        ObjectOutputStream oos = io.getObjectOutputStream();
        oos.writeObject(response);
        oos.flush();
    }

    private static void broadcastResponse(Response response) throws IOException {
        for ( var io : ServerDataBuffer.serverClientIOMap.values() ) {
            ObjectOutputStream oos = io.getObjectOutputStream();
            oos.writeObject(response);
            oos.flush();
        }
    }
}
