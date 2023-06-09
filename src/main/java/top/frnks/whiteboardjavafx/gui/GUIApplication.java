package top.frnks.whiteboardjavafx.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import top.frnks.whiteboardjavafx.*;
import top.frnks.whiteboardjavafx.common.*;
import top.frnks.whiteboardjavafx.gui.draw.Pen;
import top.frnks.whiteboardjavafx.gui.draw.PenType;
import top.frnks.whiteboardjavafx.util.ClientAction;
import top.frnks.whiteboardjavafx.util.ServerAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GUIApplication extends Application {
    public static boolean isServer = false;
    public static final HBox rootNode = new HBox();
    public static final Scene rootScene = new Scene(rootNode);
    public static final ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    public static final Canvas whiteBoardCanvas = new Canvas(1000, 650);
    public static final ImageView whiteBoardSnapshotView = new ImageView();
    public static final GraphicsContext whiteBoardGraphicsContext = whiteBoardCanvas.getGraphicsContext2D();
    public static final Slider penThicknessSlider = new Slider(1, 20, 2);
    public static final ComboBox<String> penTypeComboBox = new ComboBox<>();
    public static final Button clearButton = new Button("清屏");
    public static final Pen pen = new Pen();
    public static final ListView<Student> studentListView = new ListView<>();
    public static final TextArea QATextArea = new TextArea();
    public static final TextArea QATypeArea = new TextArea();
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        LoginPrompt.show();

        StackPane whiteBoard = new StackPane();
        whiteBoard.getChildren().add(whiteBoardCanvas);
        whiteBoard.setStyle("-fx-background-color: white");

        whiteBoardSnapshotView.setFitWidth(1000);
        whiteBoardSnapshotView.setFitHeight(650);
        whiteBoardSnapshotView.setStyle("-fx-background-color: white");

        HBox canvasToolsBox = new HBox();
        canvasToolsBox.getChildren().add(colorPicker);
        canvasToolsBox.getChildren().add(penTypeComboBox);
        canvasToolsBox.getChildren().add(penThicknessSlider);
        canvasToolsBox.getChildren().add(clearButton);

        VBox canvasBox = new VBox();
        canvasBox.setSpacing(20.0);
        canvasBox.getChildren().add(whiteBoard);

        if ( isServer ) {
            new ServerSnapshotThread().start();

            pen.initPen(whiteBoardGraphicsContext);

//        colorPicker.setPrefSize();
            colorPicker.setOnAction(event -> {
                pen.setPenColor(whiteBoardGraphicsContext, colorPicker.getValue());
            });

            penThicknessSlider.setBlockIncrement(1);
            penThicknessSlider.setShowTickLabels(true);
            penThicknessSlider.setShowTickMarks(true);
            penThicknessSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                    pen.setPenThickness(whiteBoardGraphicsContext, newValue.doubleValue())
            );

            ObservableList<String> penTypes = FXCollections.observableArrayList(
                    "铅笔",
                    "线",
                    "圆",
                    "矩形"
            );
            penTypeComboBox.setItems(penTypes);
            penTypeComboBox.getSelectionModel().selectFirst();
            penTypeComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                switch (newValue) {
                    case "铅笔" -> pen.setType(PenType.PENCIL);
                    case "线" -> pen.setType(PenType.LINE);
                    case "圆" -> pen.setType(PenType.ROUND);
                    case "矩形" -> pen.setType(PenType.RECT);
                }
                // TODO: not finished
            }));

//        clearButton.setPrefSize();
            clearButton.setOnAction(event -> GUIApplication.clearCanvas());
            whiteBoardCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, GUIApplication::mousePressed);
            whiteBoardCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, GUIApplication::mouseDragged);
            whiteBoardCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, GUIApplication::mouseReleased);

            canvasBox.getChildren().add(canvasToolsBox);
            studentListView.setItems(ServerDataBuffer.studentsList);
            studentListView.setContextMenu(new StudentsListContextMenu());

        } else {
            whiteBoard.getChildren().add(whiteBoardSnapshotView);
            studentListView.setItems(ClientDataBuffer.studentsList);
        }

        studentListView.setPrefSize(200, 400);
        studentListView.setCellFactory(new StudentCellFactory());
        studentListView.setEditable(false);

        QATextArea.setPrefSize(200, 200);
        QATextArea.setEditable(false);
        QATextArea.setWrapText(true);
//        QATextArea.setPrefSize();

        QATypeArea.setPrefSize(150, 75);
        QATypeArea.setWrapText(true);
        QATypeArea.setScrollTop(Double.MAX_VALUE);

        Button sendButton;
        if ( isServer ) {
            sendButton = new Button("回复");
            sendButton.setOnAction(event -> {
                ServerAction.answer(QATypeArea.getText());
                QATypeArea.setText("");
            });
        }
        else {
            sendButton = new Button("举手");
            sendButton.setOnAction(event -> {
                ClientAction.question(QATypeArea.getText());
                QATypeArea.setText("");
            });
        }
        sendButton.setPrefSize(50, 75);
        HBox typeBox = new HBox(QATypeArea, sendButton);
        typeBox.setSpacing(5.0);
        VBox qaBox = new VBox(QATextArea, typeBox);
        qaBox.setSpacing(5.0);

        VBox studentBox = new VBox(studentListView, qaBox);
        studentBox.setSpacing(5.0);

        rootNode.setSpacing(5.0);
        rootNode.getChildren().add(canvasBox);
        rootNode.getChildren().add(studentBox);

        if ( isServer ) stage.setTitle("共享白板 教师端");
        else stage.setTitle("共享白板 学生端");
        stage.setScene(rootScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        if ( !isServer ) ClientAction.logout();
        // TODO: server offline terminate?
    }
    public static void addTextToArea(String text) {
        QATextArea.setText(QATextArea.getText() + "\n" + text);
    }

    public static void addQuestion(Student student, String content) {
        String text = "学生" + student.name + ": " + content;
        addTextToArea(text);
    }
    public static void addAnswer(String content) {
        String text = "教师: " + content;
        addTextToArea(text);
    }

    public static void clearCanvas() {
        whiteBoardGraphicsContext.clearRect(0, 0, whiteBoardCanvas.getWidth(), whiteBoardCanvas.getHeight());
    }
    public static void mousePressed(MouseEvent event) {
        whiteBoardGraphicsContext.beginPath();
        whiteBoardGraphicsContext.moveTo(event.getX(), event.getY());
        whiteBoardGraphicsContext.stroke();
    }
    public static void mouseDragged(MouseEvent event) {
        whiteBoardGraphicsContext.lineTo(event.getX(), event.getY());
        whiteBoardGraphicsContext.stroke();
    }
    public static void mouseReleased(MouseEvent event) {
        whiteBoardGraphicsContext.lineTo(event.getX(), event.getY());
        whiteBoardGraphicsContext.stroke();
    }
    public static Image getSnapshot() {
        return whiteBoardCanvas.snapshot(new SnapshotParameters(), null);
    }
    public static void applySnapshot(Image image) {
        whiteBoardSnapshotView.setImage(image);
    }

    public static void sendFile(Student student) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择文件...");
        File choosedFile = fileChooser.showOpenDialog(primaryStage);
        if ( choosedFile != null ) {
            try {
                FileContent fileContent = new FileContent(Files.readAllBytes(choosedFile.toPath()), choosedFile.getName());
                ServerAction.sendFile(fileContent, student);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void receiveFile(FileContent content) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("教师发来了一个文件...");
        fileChooser.setInitialFileName(content.getFileName());
        Platform.runLater(() -> {
            File outputFile = fileChooser.showSaveDialog(primaryStage);
            if ( outputFile != null ) {
                saveFile(content, outputFile);
            }
        });
    }

    public static void saveFile(FileContent content, File outputFile) {
        byte[] contentBytes = content.getContent();
        try {
            Files.write(outputFile.toPath(), contentBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}