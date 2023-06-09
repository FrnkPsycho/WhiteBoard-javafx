module top.frnks.whiteboardjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.logging;
    requires java.desktop;


    opens top.frnks.whiteboardjavafx to javafx.fxml;
    exports top.frnks.whiteboardjavafx;
    exports top.frnks.whiteboardjavafx.gui;
    exports top.frnks.whiteboardjavafx.common;
    opens top.frnks.whiteboardjavafx.gui to javafx.fxml;
}