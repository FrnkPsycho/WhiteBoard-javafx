package top.frnks.whiteboardjavafx;

import top.frnks.whiteboardjavafx.common.Response;
import top.frnks.whiteboardjavafx.common.ResponseType;
import top.frnks.whiteboardjavafx.controller.ClientAction;
import top.frnks.whiteboardjavafx.gui.GUIApplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;
public class ClientThread extends Thread {
    public static final Logger LOGGER = Logger.getGlobal();
    @Override
    public void run() {
        connectToServer("localhost", 2333);
        boolean listening = true;
        try {
            while (listening) {
                Response response = (Response) ClientDataBuffer.objectInputStream.readObject();
                ResponseType responseType = response.getResponseType();
//                LOGGER.info("Received response from server: " + response + " " + responseType);

                switch (responseType) {
                    case LOGIN_SUCCESS -> ClientAction.handleLoginResponse(response);
                    case FILE -> ClientAction.handleFileResponse(response);
                    case SNAPSHOT -> ClientAction.handleSnapshotResponse(response);
                }
            }
        } catch (IOException e) {
            LOGGER.warning("IO Error");
            connectToServer("localhost", 2333);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void connectToServer(String remote, int port) {
        try {
            ClientDataBuffer.clientSocket = new Socket(remote, port);
            ClientDataBuffer.objectOutputStream = new ObjectOutputStream(ClientDataBuffer.clientSocket.getOutputStream());
            ClientDataBuffer.objectInputStream = new ObjectInputStream(ClientDataBuffer.clientSocket.getInputStream());
            ClientDataBuffer.isConnected = true;
            LOGGER.info("Connected to " + remote + ":" + port);
        } catch (IOException e) {
            LOGGER.warning("Unable to connect to server: \"" + remote + ":" + port + "\"");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            connectToServer(remote, port);
        }
    }
}
