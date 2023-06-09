package top.frnks.whiteboardjavafx;

import top.frnks.whiteboardjavafx.common.*;
import top.frnks.whiteboardjavafx.controller.ServerAction;
import top.frnks.whiteboardjavafx.gui.GUIApplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerRequestProcessor implements Runnable{
    private final Socket currentClientSocket;
    private Request request;
    public ServerRequestProcessor(Socket currentClientSocket) {
        this.currentClientSocket = currentClientSocket;
    }
    @Override
    public void run() {
        boolean listening = true;
        try {
            ServerClientIO currentIO = new ServerClientIO(
                    new ObjectInputStream(currentClientSocket.getInputStream()),
                    new ObjectOutputStream(currentClientSocket.getOutputStream())
            );

            while (listening) {
                request = (Request) currentIO.getObjectInputStream().readObject();
                RequestType requestType = request.getRequestType();
                switch (requestType) {
                    case LOGIN -> ServerAction.login(currentIO, request);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
