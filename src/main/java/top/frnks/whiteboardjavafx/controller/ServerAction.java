package top.frnks.whiteboardjavafx.controller;

import top.frnks.whiteboardjavafx.ServerClientIO;
import top.frnks.whiteboardjavafx.ServerDataBuffer;
import top.frnks.whiteboardjavafx.common.Response;
import top.frnks.whiteboardjavafx.common.ResponseType;
import top.frnks.whiteboardjavafx.gui.GUIApplication;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerAction {
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

    public static void sendResponse(ServerClientIO io, Response response) throws IOException {
        ObjectOutputStream oos = io.getObjectOutputStream();
        oos.writeObject(response);
        oos.flush();
    }

    public static void broadcastResponse(Response response) throws IOException {
        for ( var io : ServerDataBuffer.serverClientIOMap.values() ) {
            ObjectOutputStream oos = io.getObjectOutputStream();
            oos.writeObject(response);
            oos.flush();
        }
    }
}
