package top.frnks.whiteboardjavafx.util;

import top.frnks.whiteboardjavafx.ServerClientIO;
import top.frnks.whiteboardjavafx.ServerDataBuffer;
import top.frnks.whiteboardjavafx.common.*;
import top.frnks.whiteboardjavafx.gui.GUIApplication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerAction {
    public static void login(ServerClientIO io, Request request) {
        String name = (String) request.getData("name");
        long id = (long) request.getData("id");

        Student student = new Student(name, id);

        Response response = new Response(ResponseType.BROADCAST_LOGIN);
        ServerDataBuffer.studentsList.add(student);
        ServerDataBuffer.serverClientIOMap.put(id, io);

        List<Student> serializableList = ServerDataBuffer.studentsList.stream().toList();

        response.setData("students", serializableList);
        response.setData("student", student);
        broadcastResponse(response);

        GUIApplication.studentListView.refresh();
    }
    public static boolean logout(ServerClientIO io, Request request, Socket socket) {
        Student student = (Student) request.getData("student");
        ServerDataBuffer.studentsList.removeIf(student1 -> student1.id == student.id);
        ServerDataBuffer.serverClientIOMap.remove(student.id);
        GUIApplication.studentListView.refresh();

        Response response = new Response(ResponseType.BROADCAST_LOGOUT);
        response.setData("student", student);
        broadcastResponse(response);

        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public static void question(Request request) {
        String content = (String) request.getData("content");
        Student student = (Student) request.getData("student");
        Response response = new Response(ResponseType.BROADCAST_QUESTION);
        response.setData("content", content);
        response.setData("student", student);
        broadcastResponse(response);

        GUIApplication.addQuestion(student, content);
    }
    public static void answer(String content) {
        Response response = new Response(ResponseType.ANSWER);
        response.setData("content", content);
        broadcastResponse(response);

        GUIApplication.addAnswer(content);
    }
//    public static void sendMouseEvent(MouseEvent event) {
//        Response response = new Response(ResponseType.MOUSE_EVENT);
//        response.setData("event", event);
//        try {
//            broadcastResponse(response);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public static void clearCanvas() {
//        GUIApplication.clearCanvas();
//        Response response = new Response(ResponseType.CLEAR_CANVAS);
//        broadcastResponse(response);
//    }

    public static void sendSnapshot(SerializableImage image) {
        Response response = new Response(ResponseType.SNAPSHOT);
        response.setData("snapshot", image);
        broadcastResponse(response);
    }

    public static void sendFile(FileContent fileContent, Student student) {
        Response response = new Response(ResponseType.FILE);
        response.setData("file", fileContent);
        ServerClientIO io = ServerDataBuffer.serverClientIOMap.get(student.id);
        sendResponse(io, response);
    }

    private static void sendResponse(ServerClientIO io, Response response) {
        ObjectOutputStream oos = io.getObjectOutputStream();
        try {
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void broadcastResponse(Response response) {
        if ( ServerDataBuffer.studentsList.isEmpty() ) return;
        for ( var io : ServerDataBuffer.serverClientIOMap.values() ) {
            ObjectOutputStream oos = io.getObjectOutputStream();
            try {
                oos.writeObject(response);
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
