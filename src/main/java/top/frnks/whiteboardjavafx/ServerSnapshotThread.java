package top.frnks.whiteboardjavafx;

import javafx.application.Platform;
import javafx.scene.image.Image;
import top.frnks.whiteboardjavafx.common.SerializableImage;
import top.frnks.whiteboardjavafx.util.ServerAction;
import top.frnks.whiteboardjavafx.gui.GUIApplication;

public class ServerSnapshotThread extends Thread {
    @Override
    public void run() {
        System.out.println("Start Snapshot Thread.");
        while ( true ) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
                Image snapshot = GUIApplication.getSnapshot();
                SerializableImage serializableImage = new SerializableImage();
                serializableImage.setImage(snapshot);
                ServerAction.sendSnapshot(serializableImage);
            });
        }
    }
}
