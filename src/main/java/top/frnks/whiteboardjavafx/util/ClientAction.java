package top.frnks.whiteboardjavafx.util;

import javafx.application.Platform;
import top.frnks.whiteboardjavafx.ClientDataBuffer;
import top.frnks.whiteboardjavafx.common.*;
import top.frnks.whiteboardjavafx.gui.GUIApplication;
import top.frnks.whiteboardjavafx.gui.LoginPrompt;

import java.io.IOException;
import java.util.List;

public class ClientAction {

    public static void handleLogoutResponse(Response response) {
        Student student = (Student) response.getData("student");
        if ( student.id == ClientDataBuffer.currentStudent.id ) {
            Platform.exit();
        } else {
            ClientDataBuffer.studentsList.removeIf(student1 -> student1.id == student.id);
            GUIApplication.studentListView.refresh();
        }
    }
    public static void handleAnswerResponse(Response response) {
        String content = (String) response.getData("content");
        GUIApplication.addAnswer(content);
    }
    public static void handleQuestionResponse(Response response) {
        String content = (String) response.getData("content");
        Student student = (Student) response.getData("student");
        GUIApplication.addQuestion(student, content);
    }
    public static void handleFileResponse(Response response) {
        FileContent fileContent = (FileContent) response.getData("file");
        GUIApplication.receiveFile(fileContent);
    }
    public static void handleSnapshotResponse(Response response) {
        SerializableImage image = (SerializableImage) response.getData("snapshot");
        GUIApplication.applySnapshot(image.getImage());
    }
    public static void handleLoginResponse(Response response) {
        Student loginStudent = (Student) response.getData("student");
        List<Student> studentList = (List<Student>) response.getData("students");
        ClientDataBuffer.studentsList.setAll(studentList);
        GUIApplication.studentListView.refresh();
        if ( loginStudent.id == ClientDataBuffer.currentStudent.id ) {
            ClientDataBuffer.isOnline = true;
            Platform.runLater(LoginPrompt.stage::close);
        }
    }
//    public static void handleMouseEventResponse(Response response) {
//        MouseEvent event = (MouseEvent) response.getData("event");
//        String eventType = event.getEventType().toString();
//        switch (eventType) {
//            case "MOUSE_PRESSED" -> GUIApplication.mousePressed(event);
//            case "MOUSE_DRAGGED" -> GUIApplication.mouseDragged(event);
//            case "MOUSE_RELEASED" -> GUIApplication.mouseReleased(event);
//        }
//    }
//    public static void handleClearCanvasResponse(Response response) {
//        GUIApplication.clearCanvas();
//    }
    public static void question(String content) {
        Request request = new Request(RequestType.QUESTION);
        request.setData("content", content);
        request.setData("student", ClientDataBuffer.currentStudent);

        sendRequest(request);
    }

    public static void login(String name, long id) {
        Request request = new Request(RequestType.LOGIN);
        request.setData("name", name);
        request.setData("id", id);

        sendRequest(request);
    }
    public static void logout() {
        Request request = new Request(RequestType.LOGOUT);
        request.setData("student", ClientDataBuffer.currentStudent);
        sendRequest(request);
    }
    private static void sendRequest(Request request) {
        try {
            ClientDataBuffer.objectOutputStream.writeObject(request);
            ClientDataBuffer.objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
