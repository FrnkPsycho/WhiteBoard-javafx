package top.frnks.whiteboardjavafx.gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class StudentsListContextMenu extends ContextMenu {
    private static final MenuItem sendFileMenuItem = new MenuItem("发送文件");
    static {
        sendFileMenuItem.setOnAction(event -> GUIApplication.sendFile(GUIApplication.studentListView.getSelectionModel().getSelectedItem()));
    }
    public StudentsListContextMenu() {
        super(sendFileMenuItem);
    }
}
