package top.frnks.whiteboardjavafx.controller;

import javafx.application.Platform;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import top.frnks.whiteboardjavafx.ClientDataBuffer;
import top.frnks.whiteboardjavafx.ServerClientIO;
import top.frnks.whiteboardjavafx.common.Request;
import top.frnks.whiteboardjavafx.common.RequestType;
import top.frnks.whiteboardjavafx.common.Response;
import top.frnks.whiteboardjavafx.common.Student;
import top.frnks.whiteboardjavafx.gui.GUIApplication;
import top.frnks.whiteboardjavafx.gui.LoginPrompt;

import java.io.IOException;

public class ClientAction {
    public static void handleLoginResponse(Response response) {
        Student loginStudent = (Student) response.getData("student");
        ClientDataBuffer.studentsList.add(loginStudent);
        GUIApplication.studentListView.refresh();
        if ( loginStudent.id == ClientDataBuffer.currentStudent.id ) {
            ClientDataBuffer.isOnline = true;
            Platform.runLater(LoginPrompt.stage::close);
        }
    }
    public static void handleMouseEventResponse(Response response) {
        MouseEvent event = (MouseEvent) response.getData("event");
        String eventType = event.getEventType().toString();
        switch (eventType) {
            case "MOUSE_PRESSED" -> GUIApplication.mousePressed(event);
            case "MOUSE_DRAGGED" -> GUIApplication.mouseDragged(event);
            case "MOUSE_RELEASED" -> GUIApplication.mouseReleased(event);
        }
    }
    public static void handleClearCanvasResponse(Response response) {
        GUIApplication.clearCanvas();
    }
    public static void raiseHand() {

    }
    public static void login(String name, long id) {
        Request request = new Request(RequestType.LOGIN);
        request.setData("name", name);
        request.setData("id", id);

        try {
            sendRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void sendRequest(Request request) throws IOException {
        ClientDataBuffer.objectOutputStream.writeObject(request);
        ClientDataBuffer.objectOutputStream.flush();
    }
}
