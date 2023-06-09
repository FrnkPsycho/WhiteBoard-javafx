package top.frnks.whiteboardjavafx.gui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.frnks.whiteboardjavafx.ClientDataBuffer;
import top.frnks.whiteboardjavafx.ClientThread;
import top.frnks.whiteboardjavafx.ServerDataBuffer;
import top.frnks.whiteboardjavafx.ServerThread;
import top.frnks.whiteboardjavafx.common.Student;
import top.frnks.whiteboardjavafx.controller.ClientAction;

public class LoginPrompt {
    private static final VBox root = new VBox();
    public static final Stage stage = new Stage();
    public static final TextField studentNameField = new TextField();
    public static final TextField studentIDField = new TextField();
    public static void show() {
        Label promptLabel = new Label("欢迎使用共享白板，请问您需要使用教师端还是学生端？\n教师可以直接进入，学生需要输入名称和学号");
        Button teacherButton = new Button("教师端");
        Button studentButton = new Button("学生端");
        teacherButton.setOnAction(event -> {
            GUIApplication.isServer = true;
            ServerDataBuffer.serverThread.start();
            stage.close();
        });
        studentButton.setOnAction(event -> {
            ClientDataBuffer.clientThread.start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if ( !ClientDataBuffer.isConnected ) {
                new Alert(Alert.AlertType.ERROR, "无法连接到服务端！").show();
                return;
            }
            if ( studentNameField.getText().isBlank() || studentIDField.getText().isBlank() ) {
                new Alert(Alert.AlertType.ERROR, "请输入姓名与学号！").show();
                return;
            }
            long id;
            try {
                id = Long.parseLong(studentIDField.getText());
            } catch ( Exception e ) {
                new Alert(Alert.AlertType.ERROR, "学号格式不正确！").show();
                return;
            }
            ClientDataBuffer.currentStudent = new Student(studentNameField.getText(), id);
            ClientAction.login(studentNameField.getText(), id);
            GUIApplication.isServer = false;
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
