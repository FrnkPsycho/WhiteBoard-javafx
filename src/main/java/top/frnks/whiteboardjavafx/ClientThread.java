package top.frnks.whiteboardjavafx;

import top.frnks.whiteboardjavafx.common.Response;
import top.frnks.whiteboardjavafx.common.ResponseType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;
public class ClientThread extends Thread {
    public static final Logger LOGGER = Logger.getGlobal();
    @Override
    public void run() {
        connectToServer("localhost", 6657);
        boolean listening = true;
        try {
            while (listening) {
                Response response = (Response) ClientDataBuffer.objectInputStream.readObject();
                ResponseType responseType = response.getResponseType();
                LOGGER.info("Received response from server: " + response + " " + responseType);

//                switch (responseType) {
//                    case CLEAR_CANVAS ->
//                }
            }
        } catch (IOException e) {
            LOGGER.warning("IO Error");
            connectToServer("localhost", 6657);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void connectToServer(String remote, int port) {
        try {
            ClientDataBuffer.clientSocket = new Socket(remote, port);
            ClientDataBuffer.objectOutputStream = new ObjectOutputStream(ClientDataBuffer.clientSocket.getOutputStream());
            ClientDataBuffer.objectInputStream = new ObjectInputStream(ClientDataBuffer.clientSocket.getInputStream());
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
