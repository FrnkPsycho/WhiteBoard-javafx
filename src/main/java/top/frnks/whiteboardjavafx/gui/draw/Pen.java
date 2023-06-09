package top.frnks.whiteboardjavafx.gui.draw;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Pen {
    public void initPen(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2);
    }

    public void setPenColor(GraphicsContext graphicsContext, Color color) {
        graphicsContext.setStroke(color);
    }

    public void setPenThickness(GraphicsContext graphicsContext, double thickness) {
        graphicsContext.setLineWidth(thickness);
    }
    public void setType(PenType penType) {
        // TODO: other pen types
    }
}
