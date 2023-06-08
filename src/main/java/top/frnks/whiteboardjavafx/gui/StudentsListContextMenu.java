package top.frnks.whiteboardjavafx.gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import top.frnks.whiteboardjavafx.controller.ServerAction;

public class StudentsListContextMenu extends ContextMenu {
    private static final MenuItem sendFileMenuItem = new MenuItem("发送文件");
    static {
        sendFileMenuItem.setOnAction(event -> ServerAction.sendFile());
    }
    public StudentsListContextMenu() {
        super(sendFileMenuItem);
    }
}
