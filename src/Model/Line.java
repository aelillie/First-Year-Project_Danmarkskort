package Model;

import java.awt.*;
import java.io.Serializable;

public class Line extends Drawable implements Serializable {
    int stroke_id;
    private boolean dashed = false;

    /**
     * Sets up the Model.Line with a shape, color and stroke.
     * @param shape Shape of Model.Line
     * @param color Color of Model.Line
     * @param drawLevel When to draw.
     */
    public Line(Shape shape, Color color, int stroke_id, double drawLevel, int layerVal) {

    }

    /**
     * Draws the Shape with predefined colors and stroke.
     * @param g Graphic 2D with correct transform
     */
    public void draw(Graphics2D g) {

    }

    /**
     * Draws the boundary of the shape with predefined stroke and black.
     * @param g Graphic 2D with correct transform
     */
    public void drawBoundary(Graphics2D g) {

    }


    /**
     * Toggle if the line should be dashed.
     */
    public void setDashed(){
        dashed = true;
    }


}