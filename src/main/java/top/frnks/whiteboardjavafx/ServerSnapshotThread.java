package top.frnks.whiteboardjavafx;

import top.frnks.whiteboardjavafx.gui.GUIApplication;

public class ServerSnapshotThread extends Thread {
    @Override
    public void run() {
        while ( true ) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            GUIApplication.
        }
    }
}
