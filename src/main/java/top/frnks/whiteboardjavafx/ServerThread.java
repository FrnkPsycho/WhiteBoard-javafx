package top.frnks.whiteboardjavafx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    @Override
    public void run() {
        Thread listenerThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(6657) ) {
                ServerDataBuffer.serverSocket = serverSocket;
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Thread t = new Thread(new ServerRequestProcessor(clientSocket));
                    t.start();
                }
            } catch ( IOException e ) {
                throw new RuntimeException(e);
            }
        });
        listenerThread.start();
    }
}
