package top.frnks.whiteboardjavafx.gui;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import top.frnks.whiteboardjavafx.common.Student;

public class StudentCellFactory implements Callback<ListView<Student>, ListCell<Student>> {
    @Override
    public ListCell<Student> call(ListView<Student> param) {
        return new ListCell<>() {
            @Override
            public void updateItem(Student item, boolean empty) {
                super.updateItem(item, empty);
                Platform.runLater(() -> {
                    if ( empty || item == null ) setText(null);
                    else setText(item.getDisplayName());
                });
            }
        };
    }
}
