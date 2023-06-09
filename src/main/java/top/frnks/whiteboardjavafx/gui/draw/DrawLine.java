package top.frnks.whiteboardjavafx.gui.draw;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawLine {
    Canvas canvas;
    GraphicsContext graphicsContext;
    Boolean isActive;
    public DrawLine(Canvas canvas, GraphicsContext graphicsContext) {
        this.canvas = canvas;
        this.graphicsContext = graphicsContext;
        isActive = false;
    }

//    private final EventListener pressed = (event) -> {
//        graphicsContext.beginPath();
//        graphicsContext.moveTo(event.ge);
//    }
}
