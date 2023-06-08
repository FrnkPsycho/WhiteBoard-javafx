package top.frnks.whiteboardjavafx.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.frnks.whiteboardjavafx.ClientDataBuffer;
import top.frnks.whiteboardjavafx.common.Student;

public class LoginPrompt {
    private static final VBox root = new VBox();
    private static final Stage stage = new Stage();
    public static final TextField studentNameField = new TextField();
    public static final TextField studentIDField = new TextField();
    public static void show() {
        Label promptLabel = new Label("欢迎使用共享白板，请问您需要使用教师端还是学生端？\n教师可以直接进入，学生需要输入名称和学号");
        Button teacherButton = new Button("教师端");
        Button studentButton = new Button("学生端");
        teacherButton.setOnAction(event -> {
            GUIApplication.isServer = true;
            stage.close();
        });
        studentButton.setOnAction(event -> {
            ClientDataBuffer.currentStudent = new Student(studentNameField.getText(), Long.parseLong(studentIDField.getText()));
            GUIApplication.isServer = false;
            stage.close();
        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(teacherButton);
        buttonBox.getChildren().add(studentButton);

        studentNameField.setPromptText("名字");
        studentIDField.setPromptText("学号");

        root.getChildren().add(promptLabel);
        root.getChildren().add(studentNameField);
        root.getChildren().add(studentIDField);
        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root);
        stage.setTitle("教师端/学生端选择");
        stage.setScene(scene);
        stage.showAndWait();
    }


}
