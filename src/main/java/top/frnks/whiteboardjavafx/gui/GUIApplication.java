package top.frnks.whiteboardjavafx.gui;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import top.frnks.whiteboardjavafx.ClientDataBuffer;
import top.frnks.whiteboardjavafx.ClientThread;
import top.frnks.whiteboardjavafx.ServerThread;
import top.frnks.whiteboardjavafx.common.Student;
import top.frnks.whiteboardjavafx.controller.ServerAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class GUIApplication extends Application {
    public static boolean isServer = false;
    public static final HBox rootNode = new HBox();
    public static final Scene rootScene = new Scene(rootNode);
    public static final ColorPicker colorPicker = new ColorPicker(Color.BLACK);
    public static final Canvas whiteBoardCanvas = new Canvas(1000, 650);
    public static final GraphicsContext whiteBoardGraphicsContext = whiteBoardCanvas.getGraphicsContext2D();
    public static final Slider penThicknessSlider = new Slider(1, 20, 2);
//    public static final ComboBox<String> penTypeComboBox = new ComboBox<>();
    public static final Button clearButton = new Button("清屏");
    public static final Pen pen = new Pen();
    public static final ListView<Student> studentListView = new ListView<>();
    public static final TextArea QATextArea = new TextArea();
    public static final TextArea QATypeArea = new TextArea();

    @Override
    public void start(Stage stage) {
        LoginPrompt.show();
        if ( isServer ) new ServerThread().start();
        else new ClientThread().start();

        StackPane whiteBoard = new StackPane();
        whiteBoard.getChildren().add(whiteBoardCanvas);
        whiteBoard.setStyle("-fx-background-color: white");


        if ( isServer ) {
            whiteBoardCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                mousePressed(event);
                ServerAction.sendMouseEvent(event);
            });
            whiteBoardCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
                mouseDragged(event);
                ServerAction.sendMouseEvent(event);
            });
            whiteBoardCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                mouseReleased(event);
                ServerAction.sendMouseEvent(event);
            });
        }

        HBox canvasToolsBox = new HBox();
        canvasToolsBox.getChildren().add(colorPicker);
//        canvasToolsBox.getChildren().add(penTypeComboBox);
        canvasToolsBox.getChildren().add(penThicknessSlider);
        canvasToolsBox.getChildren().add(clearButton);

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

//        ObservableList<String> penTypes = FXCollections.observableArrayList(
//                "任意",
//                "线",
//                "圆",
//                "矩形"
//        );
//        penTypeComboBox.setItems(penTypes);
//        penTypeComboBox.getSelectionModel().selectFirst();
        // TODO: selectable pen type

//        clearButton.setPrefSize();
        clearButton.setOnAction(event -> ServerAction.clearCanvas());

        VBox canvasBox = new VBox();
        canvasBox.setSpacing(20.0);
        canvasBox.getChildren().add(whiteBoard);
        canvasBox.getChildren().add(canvasToolsBox);

        studentListView.setPrefSize(200, 400);
        studentListView.setItems(ClientDataBuffer.studentsList);
        studentListView.setCellFactory(new StudentCellFactory());
        studentListView.setContextMenu(new StudentsListContextMenu());
        studentListView.setEditable(false);

        QATextArea.setPrefSize(200, 200);
        QATextArea.setEditable(false);
        QATextArea.setWrapText(true);
//        QATextArea.setPrefSize();

        QATypeArea.setPrefSize(150, 75);
        QATypeArea.setWrapText(true);
        QATypeArea.setScrollTop(Double.MAX_VALUE);

        Button sendButton;
        if ( isServer ) sendButton = new Button("回复");
        else sendButton = new Button("举手发言");
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
    public static void getSnapshot() {
        WritableImage snapshot = whiteBoardCanvas.snapshot(new SnapshotParameters(), null);
//        BufferedImage bufferedImage = SwingFXUtils.fromFXImage()
//        BufferedImage bufferedImage = SwingFXUtils.

    }
    public static void applySnapshot() {

    }
}