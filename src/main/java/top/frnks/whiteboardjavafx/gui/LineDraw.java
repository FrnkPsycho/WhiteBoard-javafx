package top.frnks.whiteboardjavafx.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.EventListener;

public class LineDraw {
    Canvas canvas;
    GraphicsContext graphicsContext;
    Boolean isActive;
    public LineDraw(Canvas canvas, GraphicsContext graphicsContext) {
        this.canvas = canvas;
        this.graphicsContext = graphicsContext;
        isActive = false;
    }

//    private final EventListener pressed = (event) -> {
//        graphicsContext.beginPath();
//        graphicsContext.moveTo(event.ge);
//    }
}
